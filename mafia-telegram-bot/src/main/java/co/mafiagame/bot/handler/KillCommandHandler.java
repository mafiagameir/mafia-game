/*
 *     Copyright (c) 2018 Isa Hekmatizadeh.
 *     This file is part of mafiagame.
 *
 *     Mafiagame is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Mafiagame is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Mafiagame.  If not, see <http://www.gnu.org/licenses/>.
 */

package co.mafiagame.bot.handler;

import co.mafiagame.bot.Room;
import co.mafiagame.bot.exception.GameNotStartedYetException;
import co.mafiagame.bot.exception.NotTimeOfMafiaKillException;
import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.telegram.*;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class KillCommandHandler extends TelegramCallbackHandler {
	@Override
	protected String getCommandString() {
		return Constants.Command.MAFIA_VOTE;
	}

	@Override
	public void execute(TCallBackQuery callBackQuery) {
		Long killerId = callBackQuery.getFrom().getId();
		Long roomId = gameContainer.roomOfUser(killerId);
		if (Objects.isNull(roomId))
			throw new GameNotStartedYetException();
		Room room = gameContainer.room(roomId);
		if (room.getGame().getGameMood() != GameMood.NIGHT_MAFIA)
			throw new NotTimeOfMafiaKillException();
		String victim = callBackQuery.getData().substring(callBackQuery.getData().indexOf(" ")).trim();
		ElectionResult killResult = room.getGame().kill(new Vote(String.valueOf(killerId).trim(),
				Collections.singleton(victim)));
		client.editMessageText(new EditMessageTextRequest()
				.setChatId(callBackQuery.getMessage().getChat().getId())
				.setMessageId(callBackQuery.getMessage().getId())
				.setReplyMarkup(new TInlineKeyboardMarkup().setInlineKeyboard(Collections.emptyList()))
				.setText(MessageHolder.get("ok", room.getLang()))
		);
		if (Objects.nonNull(killResult))
			if (killResult.isSingleResult())
				handleFinishElection(room, killResult);
			else
				renewKeyboard(room, killResult);
	}

	private void handleFinishElection(Room room, ElectionResult killResult) {
		Long victim = Long.valueOf(killResult.getElects().get(0));
		room.getGame().mafias().stream().map(Player::getUserId)
				.map(Long::valueOf)
				.forEach(id -> client.send(new SendMessage()
						.setText(MessageHolder.get("mafia.decide.to.kill.player", room.getLang(),
								room.findPlayer(victim)
										.orElse(new Account()
												.setFirstName(MessageHolder.get("nobody", room.getLang()))
										).fullName()))
						.setChatId(id)
				));
	}

	private void renewKeyboard(Room room, ElectionResult killResult) {
		room.getGame().mafias().stream().map(Player::getUserId)
				.map(Long::valueOf)
				.forEach(id -> client.send(new SendMessageWithInlineKeyboard()
								.setReplyMarkup(new TInlineKeyboardMarkup()
										.addOptions(
												room.getGame().alivePlayer().stream()
														.map(Player::getUserId)
														.map(Long::valueOf)
														.map(room::findPlayer)
														.map(Optional::get)
														.map(a -> new TInlineKeyboardButton().setText(a.fullName())
																.setCallbackData("kill " + String.valueOf(a.getTelegramUserId())))
														.collect(Collectors.toList())))
								.setChatId(id)
								.setText(MessageHolder.get("you.cant.decide.who.to.kill",
										room.getLang(),
										killResult.getElects().stream().map(Long::valueOf)
												.map(room::findPlayer).map(Optional::get)
												.map(Account::fullName)
												.collect(Collectors.joining(MessageHolder.get("and", room.getLang()))),
										String.valueOf(killResult.getResult().get(killResult.getElects().get(0)))
								))
						)
				);
	}
}

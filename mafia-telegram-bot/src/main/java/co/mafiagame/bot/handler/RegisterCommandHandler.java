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
import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.persistence.domain.Action;
import co.mafiagame.bot.persistence.domain.Audit;
import co.mafiagame.bot.persistence.repository.AuditRepository;
import co.mafiagame.bot.telegram.*;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.GameMood;
import co.mafiagame.engine.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class RegisterCommandHandler extends TelegramCommandHandler {
	@Autowired
	private AuditRepository auditRepository;

	@Override
	protected Collection<String> getCommandString() {
		return Arrays.asList(Constants.Command.REGISTER,
				MessageHolder.get("register", MessageHolder.Lang.FA));
	}

	@Override
	public void execute(TMessage message) {
		if (!message.isGroup()) {
			MessageHolder.Lang lang = getLang(message);
			sendMessage(message, "register.not.allowed.in.private", lang, false);
			return;
		}
		Room room = gameContainer.room(message.getChat().getId());
		if (Objects.isNull(room)) {
			MessageHolder.Lang lang = getLang(message);
			sendMessage(message, "register.before.start.error", lang, true);
			return;
		}
		if (room.getGame().getGameMood() != GameMood.NOT_STARTED) {
			sendMessage(message, "register.after.game.started.error", room.getLang(), true);
			return;
		}
		Account account = accountCache.get(message.getFrom().getId());
		if (Objects.isNull(account))
			account = accountRepository.save(new Account(message.getFrom()).setLang(room.getLang()));
		register(message, room, account);

	}

	private void register(TMessage message, Room room, Account account) {
		boolean started = room.getGame().registerPlayer(String.valueOf(message.getFrom().getId()));
		client.send(new SendMessageWithRemoveKeyboard()
				.setReplyMarkup(new TReplyKeyboardRemove()
						.setSelective(true))
				.setReplyToMessageId(message.getId())
				.setChatId(room.getRoomId())
				.setText(MessageHolder.get("player.successfully.registered", room.getLang(),
						accountCache.get(message.getFrom().getId()).fullName())));
		gameContainer.putUserRoom(message.getFrom().getId(), message.getChat().getId());
		room.getAccounts().add(account);
		accountCache.put(account.getTelegramUserId(), account);
		if (started) {
			sendMessage(message, "game.started", room.getLang(), false);
			room.getAccounts().forEach(a -> {
				auditRepository.save(new Audit()
						.setAction(Action.START_GAME)
						.setActor(a)
						.setDate(new Date())
						.setRoomId(String.valueOf(room.getRoomId()))
				);
				SendMessageResult result = client.send(new SendMessage()
						.setChatId(a.getTelegramUserId())
						.setText(roleMsg(room, a))
				);
				if (!result.isOk() && result.getErrorCode() == 403) {
					client.send(new SendMessage()
							.setChatId(room.getRoomId())
							.setText(MessageHolder.get("bot.has.not.access", room.getLang(), a.fullName())));
				}
			});

		}
	}

	public String roleMsg(Room room, Account a) {
		switch (room.getGame().player(String.valueOf(a.getTelegramUserId())).getRole()) {
			case CITIZEN:
				return MessageHolder.get("your.role.is.citizen", room.getLang());
			case DOCTOR:
				return MessageHolder.get("your.role.is.doctor", room.getLang());
			case DETECTIVE:
				return MessageHolder.get("your.role.is.detective", room.getLang());
			case MAFIA:
				auditRepository.save(new Audit()
						.setAction(Action.BE_MAFIA)
						.setActor(a)
						.setDate(new Date())
						.setRoomId(String.valueOf(room.getRoomId()))
				);
				return MessageHolder.get("your.role.is.mafia", room.getLang()) + "\n" +
						MessageHolder.get("mafia.are.players", room.getLang(),
								room.getGame().mafias().stream()
										.map(Player::getUserId)
										.map(Long::valueOf)
										.map(room::findPlayer)
										.map(Optional::get)
										.map(Account::fullName)
										.collect(Collectors.joining(
												MessageHolder.get("and", room.getLang()))));
		}
		return MessageHolder.get("your.dead", room.getLang());
	}
}

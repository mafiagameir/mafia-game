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
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.Player;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class WhoIsPlayingCommandHandler extends TelegramCommandHandler {
	@Override
	protected Collection<String> getCommandString() {
		return Collections.singleton(Constants.Command.WHO_IS_PLAYING);
	}

	@Override
	public void execute(TMessage message) {
		Long roomId = gameContainer.roomOfUser(message.getFrom().getId());
		if (Objects.isNull(roomId)) {
			sendMessage(message, "game.not.started.yet",
					getLang(message), false);
			return;
		}
		Room room = gameContainer.room(roomId);
		client.send(new SendMessage()
				.setText(room.getGame().alivePlayer().stream()
						.map(Player::getUserId).map(Long::valueOf)
						.map(room::findPlayer)
						.map(Optional::get)
						.map(Account::fullName)
						.collect(Collectors.joining(MessageHolder.get("and", room.getLang()))))
				.setChatId(message.getChat().getId())
		);
	}
}

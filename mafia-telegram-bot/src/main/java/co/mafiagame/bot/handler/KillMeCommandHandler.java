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
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class KillMeCommandHandler extends TelegramCommandHandler {
	@Override
	protected Collection<String> getCommandString() {
		return Collections.singleton(Constants.Command.KILL_ME);
	}

	@Override
	public void execute(TMessage message) {
		Long roomId = gameContainer.roomOfUser(message.getFrom().getId());
		if (Objects.isNull(roomId)) {
			Account account = accountCache.get(message.getFrom().getId());
			sendMessage(message, "game.not.started.yet",
					Objects.isNull(account) ? MessageHolder.Lang.EN : account.getLang(), false);
			return;
		}
		Room room = gameContainer.room(roomId);
		room.getGame().externalKill(String.valueOf(message.getFrom().getId()));
		client.send(new SendMessage()
				.setChatId(room.getRoomId())
				.setText(MessageHolder.get("user.exit.game", room.getLang(), message.getFrom().getFirstName() + " " + message.getFrom().getLastName()))
		);
	}
}

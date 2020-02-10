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
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.GameMood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class WhatIsMyRoleCommandHandler extends TelegramCommandHandler {
	@Autowired
	private RegisterCommandHandler registerCommandHandler;

	@Override
	protected Collection<String> getCommandString() {
		return Collections.singleton(Constants.Command.WHAT_IS_MY_ROLE);
	}

	@Override
	public void execute(TMessage message) {
		if (message.isGroup()) {
			sendMessage(message, "command.not.available.in.group", getLang(message), false);
			return;
		}
		Long roomId = gameContainer.roomOfUser(message.getFrom().getId());
		if (Objects.isNull(roomId)) {
			sendMessage(message, "game.not.started.yet", getLang(message), false);
			return;
		}
		Room room = gameContainer.room(roomId);
		if (room.getGame().getGameMood() == GameMood.NOT_STARTED) {
			sendMessage(message, "game.not.started.yet", getLang(message), false);
			return;
		}
		String msg = registerCommandHandler.roleMsg(room, room.findPlayer(message.getFrom().getId())
				.orElseThrow(IllegalStateException::new));
		client.send(new SendMessage()
				.setChatId(message.getChat().getId())
				.setText(msg));
	}
}

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
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.GameMood;
import co.mafiagame.engine.Role;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class HelpCommandHandler extends TelegramCommandHandler {
	@Override
	protected Collection<String> getCommandString() {
		return Collections.singleton(Constants.Command.HELP);
	}

	@Override
	public void execute(TMessage message) {
		Long roomId = gameContainer.roomOfUser(message.getFrom().getId());
		if (Objects.isNull(roomId)) {
			helpBeforeStart(message);
			return;
		}
		Room room = gameContainer.room(roomId);
		if (room.getGame().getGameMood() == GameMood.DAY)
			helpOnDay(message, room);
		else
			helpOnNight(message, room);
	}

	private void helpBeforeStart(TMessage message) {
		MessageHolder.Lang lang = getLang(message);
		sendMessage(message, "help.before.start", lang, true);
	}

	private void helpOnDay(TMessage message, Room room) {
		if (room.getGame().isElectionStarted())
			sendMessage(message, "help.day.election", room.getLang(), true);
		else
			sendMessage(message, "help.day.normal", room.getLang(), true);
	}

	private void helpOnNight(TMessage message, Room room) {
		if (Objects.equals(room.getRoomId(), message.getChat().getId()))
			sendMessage(message, "help.wait", room.getLang(), true);
		else if (room.getGame().getGameMood() == GameMood.NIGHT_MAFIA)
			if (room.getGame().player(String.valueOf(message.getFrom().getId()).trim()).getRole() == Role.MAFIA)
				sendMessage(message, "help.night.mafia", room.getLang(), true);
			else
				sendMessage(message, "help.wait", room.getLang(), true);
		else if (room.getGame().getGameMood() == GameMood.NIGHT_DOCTOR)
			if (room.getGame().player(String.valueOf(message.getFrom().getId()).trim()).getRole() == Role.DOCTOR)
				sendMessage(message, "help.doctor", room.getLang(), true);
			else
				sendMessage(message, "help.wait", room.getLang(), true);
		else if (room.getGame().getGameMood() == GameMood.NIGHT_DETECTIVE)
			if (room.getGame().player(String.valueOf(message.getFrom().getId()).trim()).getRole() == Role.DETECTIVE)
				sendMessage(message, "help.detective", room.getLang(), true);
			else
				sendMessage(message, "help.wait", room.getLang(), true);
	}
}

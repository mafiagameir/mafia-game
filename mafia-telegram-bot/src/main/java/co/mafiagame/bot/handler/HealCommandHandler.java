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
import co.mafiagame.bot.telegram.EditMessageTextRequest;
import co.mafiagame.bot.telegram.TCallBackQuery;
import co.mafiagame.bot.telegram.TInlineKeyboardMarkup;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class HealCommandHandler extends TelegramCallbackHandler {
	@Override
	protected String getCommandString() {
		return Constants.Command.DOCTOR_HEAL;
	}

	@Override
	public void execute(TCallBackQuery callBackQuery) {
		Long callerId = callBackQuery.getFrom().getId();
		Long roomId = gameContainer.roomOfUser(callerId);
		if (Objects.isNull(roomId))
			throw new GameNotStartedYetException();
		Room room = gameContainer.room(roomId);
		String target = callBackQuery.getData().substring(callBackQuery.getData().indexOf(" ")).trim();
		room.getGame().heal(String.valueOf(callerId), target);
		client.editMessageText(new EditMessageTextRequest()
				.setText(MessageHolder.get("ok",
						room.getLang(),
						room.findPlayer(Long.valueOf(target))
								.orElseThrow(IllegalStateException::new).fullName()))
				.setMessageId(callBackQuery.getMessage().getId())
				.setChatId(callBackQuery.getMessage().getChat().getId())
				.setReplyMarkup(new TInlineKeyboardMarkup().setInlineKeyboard(Collections.emptyList()))
		);
	}
}

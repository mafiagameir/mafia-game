/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.bot.handler;

import co.mafiagame.bot.Room;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.GameMood;
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

    public void helpBeforeStart(TMessage message) {

    }

    public void helpOnDay(TMessage message, Room room) {

    }

    public void helpOnNight(TMessage message, Room room) {

    }
}

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

package co.mafiagame.bot;

import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Esa Hekmatizadeh
 */
public class Room {
    private Game game;
    private MessageHolder.Lang lang;
    private List<Account> accounts = new ArrayList<>();
    private Integer roomId;

    public Game getGame() {
        return game;
    }

    public Room setGame(Game game) {
        this.game = game;
        return this;
    }

    public MessageHolder.Lang getLang() {
        return lang;
    }

    public Room setLang(MessageHolder.Lang lang) {
        this.lang = lang;
        return this;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Room setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        return this;
    }

    public Optional<Account> findPlayer(Integer userId) {
        return accounts.stream().filter(a -> Objects.equals(a.getTelegramUserId(), userId)).findAny();
    }

    public Optional<Account> findPlayer(String fullName) {
        return accounts.stream().filter(a -> a.fullName().trim().equals(fullName.trim())).findAny();
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Room setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}

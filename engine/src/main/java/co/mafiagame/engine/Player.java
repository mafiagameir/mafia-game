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

package co.mafiagame.engine;

import java.util.Objects;

/**
 * this (mutable) class represent a player of game
 *
 * @author Esa Hekmatizadeh
 */
public class Player {
    public final static String NOBODY_USERID = "0";
    public final static Player NOBODY = new Player(NOBODY_USERID);
    private final String userId; // this property come from client system
    private Role role = Role.UNKNOWN;

    Player(String userId) {
        if (Objects.isNull(userId))
            throw new IllegalArgumentException("cannot register a user with null userId");
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    Player setRole(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return userId.equals(player.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "Player(" + userId + ":" + role + ")";
    }
}

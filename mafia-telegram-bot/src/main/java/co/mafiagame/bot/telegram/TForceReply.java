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

package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
public class TForceReply {
    @JsonProperty("force_reply")
    private boolean forceReply=true;
    private boolean selective =true;

    public boolean isForceReply() {
        return forceReply;
    }

    public TForceReply setForceReply(boolean forceReply) {
        this.forceReply = forceReply;
        return this;
    }

    public boolean isSelective() {
        return selective;
    }

    public TForceReply setSelective(boolean selective) {
        this.selective = selective;
        return this;
    }
}

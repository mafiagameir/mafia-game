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

package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author hekmatof
 */
public class TUpdate {
    @JsonProperty("update_id")
    private Long id;
    private TMessage message;
    @JsonProperty("callback_query")
    private TCallBackQuery callBackQuery;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TMessage getMessage() {
        return message;
    }

    public void setMessage(TMessage message) {
        this.message = message;
    }

    public TCallBackQuery getCallBackQuery() {
        return callBackQuery;
    }

    public TUpdate setCallBackQuery(TCallBackQuery callBackQuery) {
        this.callBackQuery = callBackQuery;
        return this;
    }

    @Override
    public String toString() {
        return "TUpdate{" +
                (Objects.isNull(id) ? "" : " id=" + id) +
                (Objects.isNull(message) ? "" : " ,  message=" + message) +
                (Objects.isNull(callBackQuery) ? "" : " ,  callBackQuery=" + callBackQuery) +
                '}';
    }
}

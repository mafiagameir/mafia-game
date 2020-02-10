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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TCallBackQuery {
	private String id;
	private TUser from;
	private TMessage message;
	@JsonProperty("inline_message_id")
	private String inlineMessageId;
	@JsonProperty("chat_instance")
	private String chatInstance;
	private String data;

	public String getId() {
		return id;
	}

	public TCallBackQuery setId(String id) {
		this.id = id;
		return this;
	}

	public TUser getFrom() {
		return from;
	}

	public TCallBackQuery setFrom(TUser from) {
		this.from = from;
		return this;
	}

	public TMessage getMessage() {
		return message;
	}

	public TCallBackQuery setMessage(TMessage message) {
		this.message = message;
		return this;
	}

	public String getInlineMessageId() {
		return inlineMessageId;
	}

	public TCallBackQuery setInlineMessageId(String inlineMessageId) {
		this.inlineMessageId = inlineMessageId;
		return this;
	}

	public String getChatInstance() {
		return chatInstance;
	}

	public TCallBackQuery setChatInstance(String chatInstance) {
		this.chatInstance = chatInstance;
		return this;
	}

	public String getData() {
		return data;
	}

	public TCallBackQuery setData(String data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return "TCallBackQuery{" +
				(Objects.isNull(id) ? "" : " id='" + id + '\'') +
				(Objects.isNull(from) ? "" : " ,  from=" + from) +
				(Objects.isNull(message) ? "" : " ,  message=" + message) +
				(Objects.isNull(inlineMessageId) ? "" : " ,  inlineMessageId='" + inlineMessageId + '\'') +
				(Objects.isNull(chatInstance) ? "" : " ,  chatInstance='" + chatInstance + '\'') +
				(Objects.isNull(data) ? "" : " ,  data='" + data + '\'') +
				'}';
	}
}

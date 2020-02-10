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
public class SendMessage {
	@JsonProperty("chat_id")
	private Long chatId;
	private String text;
	@JsonProperty("parse_mode")
	private String parseMode;
	@JsonProperty("reply_to_message_id")
	private Long replyToMessageId;

	public Long getChatId() {
		return chatId;
	}

	public SendMessage setChatId(Long chatId) {
		this.chatId = chatId;
		return this;
	}

	public String getText() {
		return text;
	}

	public SendMessage setText(String text) {
		this.text = text;
		return this;
	}

	public String getParseMode() {
		return parseMode;
	}

	public SendMessage setParseMode(String parseMode) {
		this.parseMode = parseMode;
		return this;
	}

	public Long getReplyToMessageId() {
		return replyToMessageId;
	}

	public SendMessage setReplyToMessageId(Long replyToMessageId) {
		this.replyToMessageId = replyToMessageId;
		return this;
	}

	@Override
	public String toString() {
		return "SendMessage{" +
				(Objects.isNull(chatId) ? "" : " chatId=" + chatId) +
				(Objects.isNull(text) ? "" : " ,  text='" + text + '\'') +
				(Objects.isNull(replyToMessageId) ? "" : " ,  replyToMessageId=" + replyToMessageId) +
				'}';
	}
}

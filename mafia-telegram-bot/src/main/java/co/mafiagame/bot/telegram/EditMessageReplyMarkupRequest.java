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

/**
 * @author Esa Hekmatizadeh
 */
public class EditMessageReplyMarkupRequest {
	@JsonProperty("chat_id")
	private Long chatId;
	@JsonProperty("message_id")
	private Long messageId;
	@JsonProperty("reply_markup")
	private TInlineKeyboardMarkup replyMarkup;

	public Long getChatId() {
		return chatId;
	}

	public EditMessageReplyMarkupRequest setChatId(Long chatId) {
		this.chatId = chatId;
		return this;
	}

	public Long getMessageId() {
		return messageId;
	}

	public EditMessageReplyMarkupRequest setMessageId(Long messageId) {
		this.messageId = messageId;
		return this;
	}

	public TInlineKeyboardMarkup getReplyMarkup() {
		return replyMarkup;
	}

	public EditMessageReplyMarkupRequest setReplyMarkup(TInlineKeyboardMarkup replyMarkup) {
		this.replyMarkup = replyMarkup;
		return this;
	}
}

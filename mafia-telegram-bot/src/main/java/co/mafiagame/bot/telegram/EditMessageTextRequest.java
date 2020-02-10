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

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageTextRequest {
	@JsonProperty("chat_id")
	private Long chatId;
	@JsonProperty("message_id")
	private Long messageId;
	private String text;
	@JsonProperty("parse_mode")
	private String parseMode;
	@JsonProperty("disable_web_page_preview")
	private Boolean disableWebPagePreview;
	@JsonProperty("reply_markup")
	private TInlineKeyboardMarkup replyMarkup;

	public Long getChatId() {
		return chatId;
	}

	public EditMessageTextRequest setChatId(Long chatId) {
		this.chatId = chatId;
		return this;
	}

	public Long getMessageId() {
		return messageId;
	}

	public EditMessageTextRequest setMessageId(Long messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getText() {
		return text;
	}

	public EditMessageTextRequest setText(String text) {
		this.text = text;
		return this;
	}

	public String getParseMode() {
		return parseMode;
	}

	public EditMessageTextRequest setParseMode(String parseMode) {
		this.parseMode = parseMode;
		return this;
	}

	public Boolean getDisableWebPagePreview() {
		return disableWebPagePreview;
	}

	public EditMessageTextRequest setDisableWebPagePreview(Boolean disableWebPagePreview) {
		this.disableWebPagePreview = disableWebPagePreview;
		return this;
	}

	public TInlineKeyboardMarkup getReplyMarkup() {
		return replyMarkup;
	}

	public EditMessageTextRequest setReplyMarkup(TInlineKeyboardMarkup replyMarkup) {
		this.replyMarkup = replyMarkup;
		return this;
	}
}

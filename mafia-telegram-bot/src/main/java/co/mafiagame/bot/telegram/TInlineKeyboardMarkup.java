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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
public class TInlineKeyboardMarkup {
	@JsonProperty("inline_keyboard")
	private List<List<TInlineKeyboardButton>> inlineKeyboard = new ArrayList<>();

	public TInlineKeyboardMarkup addOptions(List<TInlineKeyboardButton> options) {
		long skipSize = 0L;
		while (options.stream().skip(skipSize).count() > 0) {
			this.inlineKeyboard.add(
					options.stream().skip(skipSize)
							.limit(3L)
							.collect(Collectors.toList()));
			skipSize += 3L;
		}
		return this;
	}

	public List<List<TInlineKeyboardButton>> getInlineKeyboard() {
		return inlineKeyboard;
	}

	public TInlineKeyboardMarkup setInlineKeyboard(List<List<TInlineKeyboardButton>> inlineKeyboard) {
		this.inlineKeyboard = inlineKeyboard;
		return this;
	}
}

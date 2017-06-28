/*
 * Copyright (C) 2015 mafiagame.ir
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hekmatof
 */
public class TReplyKeyboardMarkup {
    private List<List<String>> keyboard = new ArrayList<>();
    @JsonProperty("resize_keyboard")
    private boolean resizeKeyboard = true;
    @JsonProperty("one_time_keyboard")
    private boolean oneTimeKeyboard = true;
    private boolean selective = true;

    public TReplyKeyboardMarkup addOptions(List<String> options) {
        long skipSize = 0L;
        while (options.stream().skip(skipSize).count() > 0) {
            this.getKeyboard().add(
                options.stream().skip(skipSize)
                    .limit(3L)
                    .collect(Collectors.toList()));
            skipSize += 3L;
        }
        return this;
    }

    public List<List<String>> getKeyboard() {
        return keyboard;
    }


    public TReplyKeyboardMarkup setKeyboard(List<List<String>> keyboard) {
        this.keyboard = keyboard;
        return this;
    }

    public boolean isResizeKeyboard() {
        return resizeKeyboard;
    }

    public TReplyKeyboardMarkup setResizeKeyboard(boolean resizeKeyboard) {
        this.resizeKeyboard = resizeKeyboard;
        return this;
    }

    public boolean isOneTimeKeyboard() {
        return oneTimeKeyboard;
    }

    public TReplyKeyboardMarkup setOneTimeKeyboard(boolean oneTimeKeyboard) {
        this.oneTimeKeyboard = oneTimeKeyboard;
        return this;
    }

    public boolean isSelective() {
        return selective;
    }

    public TReplyKeyboardMarkup setSelective(boolean selective) {
        this.selective = selective;
        return this;
    }
}

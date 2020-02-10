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
public class SendMessageResult {
	private boolean ok;
	@JsonProperty("error_code")
	private Long errorCode;
	private String description;
	private TMessage result;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public Long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Long errorCode) {
		this.errorCode = errorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TMessage getResult() {
		return result;
	}

	public void setResult(TMessage result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "SendMessageResult{" +
				"ok=" + ok +
				", errorCode=" + errorCode +
				", description='" + description + '\'' +
				", result=" + result +
				'}';
	}
}

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

import java.util.List;

/**
 * @author Esa Hekmatizadeh
 */
public class TResult {
	private boolean ok;
	private List<TUpdate> result;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public List<TUpdate> getResult() {
		return result;
	}

	public void setResult(List<TUpdate> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "TResult{" +
				"ok=" + ok +
				", result=" + result +
				'}';
	}
}

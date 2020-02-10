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

import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
public class TMessageEntity {
	private String type;
	private Long offset;
	private Long length;
	private String url;
	private TUser user;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TUser getUser() {
		return user;
	}

	public void setUser(TUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "TMessageEntity{" +
				(Objects.isNull(type) ? "" : " type='" + type + '\'') +
				(Objects.isNull(offset) ? "" : " ,  offset=" + offset) +
				(Objects.isNull(length) ? "" : " ,  length=" + length) +
				(Objects.isNull(url) ? "" : " ,  url='" + url + '\'') +
				(Objects.isNull(user) ? "" : " ,  user=" + user) +
				'}';
	}
}

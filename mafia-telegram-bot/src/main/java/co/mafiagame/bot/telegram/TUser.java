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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
public class TUser {
	private Long id;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public boolean hasUsername() {
		return !StringUtils.isEmpty(username);
	}

	@Override
	public String toString() {
		return "TUser{" +
				(Objects.isNull(id) ? "" : " id=" + id) +
				(Objects.isNull(firstName) ? "" : " ,  firstName='" + firstName + '\'') +
				(Objects.isNull(lastName) ? "" : " ,  lastName='" + lastName + '\'') +
				(Objects.isNull(username) ? "" : " ,  username='" + username + '\'') +
				'}';
	}
}

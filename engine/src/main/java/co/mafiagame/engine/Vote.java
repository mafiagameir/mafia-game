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

package co.mafiagame.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * immutable class contain a player who vote(voter)
 * and a list of players which voter give vote to (candidates)
 * remember equality in this class is just based on voter not votes(candidates)
 *
 * @author Esa Hekmatizadeh
 */
public class Vote {
	private final String voterUserId;
	private final List<String> candidateUserIds = new ArrayList<>();

	public Vote(String voterUserId, Collection<String> candidateUserIds) {
		if (Objects.isNull(voterUserId))
			throw new IllegalArgumentException("voter object is null");
		this.voterUserId = voterUserId;
		this.candidateUserIds.addAll(candidateUserIds);
	}

	public String getVoterUserId() {
		return voterUserId;
	}

	public List<String> getCandidateUserIds() {
		return candidateUserIds;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vote vote = (Vote) o;
		return Objects.equals(voterUserId, vote.voterUserId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(voterUserId);
	}
}

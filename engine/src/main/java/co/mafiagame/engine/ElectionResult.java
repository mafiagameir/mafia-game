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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * immutable Class represent a election result
 *
 * @author Esa Hekmatizadeh
 */
public class ElectionResult {
    private final List<Vote> votes = new ArrayList<>();
    private final Map<String, Long> result; //userId -> number of votes

    //list of elected players userId, if singleResult then it should contain just one element
    private final List<String> elects;

    ElectionResult(Collection<Vote> votes) {
        this.votes.addAll(votes);
        result = this.votes.stream().map(Vote::getCandidateUserIds)
                .map(l -> l.stream().filter(id -> !Objects.equals("nobody", id)))
                .flatMap(Function.identity())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Long maxCount = result.values().stream().max(Long::compareTo).orElseThrow(IllegalArgumentException::new);
        elects = result.keySet().stream().filter(p -> Objects.equals(result.get(p), maxCount)).collect(Collectors.toList());
    }

    public Collection<Vote> getVotes() {
        return votes;
    }

    public Map<String, Long> getResult() {
        return result;
    }

    public boolean isSingleResult() {
        return elects.size() == 1;
    }

    public List<String> getElects() {
        return elects;
    }
}

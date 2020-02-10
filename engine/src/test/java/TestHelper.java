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

import co.mafiagame.engine.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Esa Hekmatizadeh
 */
public class TestHelper {
	private List<String> citizens = new ArrayList<>();
	private List<String> mafia = new ArrayList<>();
	private String doctor;
	private String detective;

	public TestHelper(List<Player> players) {
		players.forEach(p -> {
			switch (p.getRole()) {
				case CITIZEN:
					citizens.add(p.getUserId());
					break;
				case MAFIA:
					mafia.add(p.getUserId());
					break;
				case DOCTOR:
					doctor = p.getUserId();
					break;
				case DETECTIVE:
					detective = p.getUserId();
			}
		});
	}

	public String citizen(int index) {
		return citizens.get(index);
	}

	public String mafia(int index) {
		return mafia.get(index);
	}

	public String doctor() {
		return doctor;
	}

	public String detective() {
		return detective;
	}
}

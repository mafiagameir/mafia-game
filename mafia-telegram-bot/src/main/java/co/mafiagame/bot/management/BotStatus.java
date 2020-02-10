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

package co.mafiagame.bot.management;

import java.util.Date;
import java.util.List;

/**
 * @author Esa Hekmatizadeh
 */
public class BotStatus {
	private int playingGames;
	private int playingPlayers;
	private List<Date> lastUpdates;

	public int getPlayingGames() {
		return playingGames;
	}

	public BotStatus setPlayingGames(int playingGames) {
		this.playingGames = playingGames;
		return this;
	}

	public int getPlayingPlayers() {
		return playingPlayers;
	}

	public BotStatus setPlayingPlayers(int playingPlayers) {
		this.playingPlayers = playingPlayers;
		return this;
	}

	public List<Date> getLastUpdates() {
		return lastUpdates;
	}

	public BotStatus setLastUpdates(List<Date> lastUpdates) {
		this.lastUpdates = lastUpdates;
		return this;
	}
}

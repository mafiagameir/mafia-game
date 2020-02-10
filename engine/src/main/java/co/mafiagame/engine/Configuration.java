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

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * the configuration of the client system which should be instantiated before any
 * instantiate of {@link Game} object. all the listeners for various events should be registered in this class
 *
 * @author Esa Hekmatizadeh
 */
public final class Configuration {
	private final BiConsumer<Game, NightResult> sunriseListener;
	private final BiConsumer<Game, GameResult> gameFinishListener;
	private final Consumer<Game> mafiaTurnListener;
	private final Consumer<Game> doctorTurnListener;
	private final Consumer<Game> detectiveTurnListener;
	private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);


	private Configuration(Builder builder) {
		this.sunriseListener = builder.sunriseListener;
		this.gameFinishListener = builder.gameFinishListener;
		this.mafiaTurnListener = builder.mafiaTurnListener;
		this.doctorTurnListener = builder.doctorTurnListener;
		this.detectiveTurnListener = builder.detectiveTurnListener;
	}

	void changeTurn(Game game) {
		switch (game.getGameMood()) {
			case NIGHT_MAFIA:
				mafiaTurnListener.accept(game);
				break;
			case NIGHT_DETECTIVE:
				detectiveTurnListener.accept(game);
				break;
			case NIGHT_DOCTOR:
				doctorTurnListener.accept(game);
				break;
			case DAY:
				sunriseListener.accept(game, new NightResult(game.killCandidate()));
				break;
			case ENDED:
				if (Objects.nonNull(game.killCandidate()))
					sunriseListener.accept(game, new NightResult(game.killCandidate()));
				executorService.schedule(() ->
								gameFinishListener.accept(game, game.gameResult()),
						4, TimeUnit.SECONDS);
		}
	}

	public static class Builder {
		private BiConsumer<Game, NightResult> sunriseListener;
		private BiConsumer<Game, GameResult> gameFinishListener;
		private Consumer<Game> mafiaTurnListener;
		private Consumer<Game> doctorTurnListener;
		private Consumer<Game> detectiveTurnListener;

		public Builder registerSunriseListener(BiConsumer<Game, NightResult> sunriseListener) {
			this.sunriseListener = sunriseListener;
			return this;
		}

		public Builder registerMafiaTurnListener(Consumer<Game> mafiaTurnListener) {
			this.mafiaTurnListener = mafiaTurnListener;
			return this;
		}

		public Builder registerDoctorTurnListener(Consumer<Game> doctorTurnListener) {
			this.doctorTurnListener = doctorTurnListener;
			return this;
		}

		public Builder registerDetectiveTurnListener(Consumer<Game> detectiveTurnListener) {
			this.detectiveTurnListener = detectiveTurnListener;
			return this;
		}

		public Builder registerGameFinishListener(BiConsumer<Game, GameResult> gameFinishListener) {
			this.gameFinishListener = gameFinishListener;
			return this;
		}

		public Configuration build() {
			return new Configuration(this);
		}
	}
}

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

package co.mafiagame.bot;

import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.persistence.domain.Action;
import co.mafiagame.bot.persistence.domain.Audit;
import co.mafiagame.bot.persistence.repository.AuditRepository;
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.SendMessageWithInlineKeyboard;
import co.mafiagame.bot.telegram.TInlineKeyboardButton;
import co.mafiagame.bot.telegram.TInlineKeyboardMarkup;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public final class BotConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

	private Configuration configuration;
	private final TelegramClient client;
	private final GameContainer gameContainer;
	private final AuditRepository auditRepository;

	@Autowired
	public BotConfiguration(TelegramClient client, GameContainer gameContainer, AuditRepository auditRepository) {
		this.client = client;
		this.gameContainer = gameContainer;
		this.auditRepository = auditRepository;
	}

	private static String roleKey(Role role) {
		switch (role) {
			case CITIZEN:
				return "player.was.citizen";
			case MAFIA:
				return "player.was.mafia";
			case DOCTOR:
				return "player.was.doctor";
			case DETECTIVE:
				return "player.was.detective";
		}
		throw new IllegalArgumentException();
	}

	public Configuration configuration() {
		if (Objects.isNull(configuration))
			setConfiguration();
		return configuration;
	}

	private void setConfiguration() {
		configuration = new Configuration.Builder()
				.registerMafiaTurnListener(this::mafiaTurn)
				.registerDetectiveTurnListener(this::detectiveTurn)
				.registerDoctorTurnListener(this::doctorTurn)
				.registerSunriseListener(this::sunriseHandler)
				.registerGameFinishListener(this::finishHandler)
				.build();
	}

	private void mafiaTurn(Game game) {
		Room room = gameContainer.room(Long.valueOf(game.getGameId()));
		client.send(new SendMessage()
				.setChatId(room.getRoomId())
				.setText(MessageHolder.get("night.started.be.silent", room.getLang()))
		);
		room.getGame().mafias().stream()
				.map(Player::getUserId)
				.map(Long::valueOf)
				.forEach(id ->
						client.send(new SendMessageWithInlineKeyboard()
								.setReplyMarkup(new TInlineKeyboardMarkup()
										.addOptions(
												room.getGame().alivePlayer().stream()
														.map(Player::getUserId)
														.map(Long::valueOf)
														.map(room::findPlayer)
														.map(Optional::get)
														.map(a -> new TInlineKeyboardButton().setText(a.fullName())
																.setCallbackData("kill " + a.getTelegramUserId()))
														.collect(Collectors.toList())))
								.setText(MessageHolder.get("mafia.night.started", room.getLang()))
								.setChatId(id)
						));
	}

	private void detectiveTurn(Game game) {
		Room room = gameContainer.room(Long.valueOf(game.getGameId()));
		Optional<Player> detectivePlayerOpt = game.detective();
		detectivePlayerOpt.ifPresent(detective -> client.send(new SendMessageWithInlineKeyboard()
				.setReplyMarkup(new TInlineKeyboardMarkup()
						.addOptions(room.getGame().alivePlayer().stream()
								.map(Player::getUserId)
								.map(Long::valueOf)
								.map(room::findPlayer)
								.map(Optional::get)
								.map(a -> new TInlineKeyboardButton().setText(a.fullName())
										.setCallbackData("ask " + a.getTelegramUserId()))
								.collect(Collectors.toList())))
				.setChatId(Long.valueOf(detective.getUserId()))
				.setText(MessageHolder.get("detective.night.started", room.getLang()))));
	}

	private void doctorTurn(Game game) {
		Room room = gameContainer.room(Long.valueOf(game.getGameId()));
		Optional<Player> doctorPlayerOpt = game.doctor();
		doctorPlayerOpt.ifPresent(doctor -> client.send(new SendMessageWithInlineKeyboard()
				.setReplyMarkup(new TInlineKeyboardMarkup()
						.addOptions(room.getGame().alivePlayer().stream()
								.map(Player::getUserId)
								.map(Long::valueOf)
								.map(room::findPlayer)
								.map(Optional::get)
								.map(a -> new TInlineKeyboardButton().setText(a.fullName())
										.setCallbackData("heal " + a.getTelegramUserId()))
								.collect(Collectors.toList())))
				.setChatId(Long.valueOf(doctor.getUserId()))
				.setText(MessageHolder.get("doctor.night.started", room.getLang()))));
	}

	private void sunriseHandler(Game game, NightResult nightResult) {
		Room room = gameContainer.room(Long.valueOf(game.getGameId()));
		Player killedPlayer = nightResult.getKilledPlayer();
		String text;
		if (Player.NOBODY.equals(killedPlayer))
			text = MessageHolder.get("nobody.was.killed.last.night", room.getLang());
		else {
			text = MessageHolder.get("user.was.killed.last.night", room.getLang(),
					room.findPlayer(Long.valueOf(killedPlayer.getUserId()))
							.orElseThrow(IllegalStateException::new).fullName());
			gameContainer.removeUser(Long.valueOf(killedPlayer.getUserId()));
		}
		client.send(new SendMessage()
				.setChatId(room.getRoomId())
				.setText(text)
		);
	}

	private void finishHandler(Game game, GameResult gameResult) {
		Room room = gameContainer.room(Long.valueOf(game.getGameId()));
		StringBuilder text = new StringBuilder(gameResult == GameResult.MAFIAS_WIN ?
				MessageHolder.get("mafia.win", room.getLang()) :
				MessageHolder.get("citizens.win", room.getLang()));
		text.append("\n");
		Map<String, Role> roles = game.getGameSetup().getPlayers();
		for (String k : roles.keySet()) {
			Account ac = room.findPlayer(Long.valueOf(k)).orElseThrow(IllegalStateException::new);
			Role role = roles.get(k);
			text.append(MessageHolder.get(roleKey(role), room.getLang(), ac.fullName())).append("\n");
			auditRepository.save(new Audit()
					.setRoomId(String.valueOf(room.getRoomId()))
					.setDate(new Date())
					.setActor(ac)
					.setAction(Action.GAME_OVER)
			);
		}
		client.send(new SendMessage()
				.setChatId(room.getRoomId())
				.setText(text.toString())
		);
		clearGame(game);
		logger.info("game with id {} finished with result {}", game.getGameId(), gameResult);
	}

	private void clearGame(Game game) {
		game.getGameSetup().getPlayers().keySet().stream().map(Long::valueOf)
				.forEach(gameContainer::removeUser);
		gameContainer.removeRoom(Long.valueOf(game.getGameId()));
	}
}

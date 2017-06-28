/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.bot.handler;

import co.mafiagame.bot.BotConfiguration;
import co.mafiagame.bot.CommandDispatcher;
import co.mafiagame.bot.Room;
import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.telegram.*;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class StartCommandHandler extends TelegramCommandHandler {

    private Map<Long, StartGameState> states = new HashMap<>();
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    @Autowired
    private BotConfiguration botConfiguration;

    @Override
    protected Collection<String> getCommandString() {
        return Collections.singleton(Constants.Command.START_STASHED_GAME);
    }

    @Override
    public void execute(TMessage message) {
        Account account = accountRepository.findByTelegramUserId(message.getFrom().getId());
        if (Objects.isNull(account))
            welcome(message);
        else if (message.isGroup())
            handleStartGame(message, account.getLang());
        else {
            client.send(new SendMessage()
                .setChatId(message.getChat().getId())
                .setText(MessageHolder.get("add.me.to.group", account.getLang())));
        }
    }

    private void welcome(TMessage message) {
        Account savedAccount = accountRepository.save(new Account(message.getFrom()));
        accountCache.put(savedAccount.getTelegramUserId(), savedAccount);
        client.send(new SendMessageWithInlineKeyboard()
            .setReplyMarkup(
                new TInlineKeyboardMarkup()
                    .setInlineKeyboard(Collections.singletonList(
                        Arrays.asList(
                            new TInlineKeyboardButton().setText("English").setCallbackData("lang EN"),
                            new TInlineKeyboardButton().setText("فارسی").setCallbackData("lang FA")
                        )
                    ))
            )
            .setChatId(message.getChat().getId())
            .setText(MessageHolder.get("welcome.message", MessageHolder.Lang.FA)));
    }

    private void handleStartGame(TMessage message, MessageHolder.Lang lang) {
        if (message.getText().matches("\\d+") ||
            message.getText().equals(MessageHolder.get("no", lang)) ||
            message.getText().equals(MessageHolder.get("yes", lang))) {
            gameInfoProvided(message, lang);
            return;
        }
        if (CommandDispatcher.removeSlash(message.getText())
            .startsWith(Constants.Command.START_STASHED_GAME)) {
            states.put(message.getChat().getId(), new StartGameState());
            client.send(new SendMessageWithForceReply()
                .setText(MessageHolder.get("how.many.citizen.game.has", lang))
                .setReplyToMessageId(message.getId())
                .setChatId(message.getChat().getId()));
            scheduledExecutorService.schedule(() -> {
                states.remove(message.getChat().getId());
                client.send(new SendMessage()
                    .setText(MessageHolder.get("initiate.game.timeout", lang))
                    .setChatId(message.getChat().getId())
                );
            }, 5, TimeUnit.MINUTES);
        }
    }

    private void gameInfoProvided(TMessage message, MessageHolder.Lang lang) {
        StartGameState startGameState = states.get(message.getChat().getId());
        if (Objects.nonNull(startGameState)) {
            if (Objects.isNull(startGameState.citizenNo)) {
                readCitizenNo(startGameState, message, lang);
            } else if (Objects.isNull(startGameState.mafiaNo)) {
                readMafiaNo(startGameState, message, lang);
            } else if (Objects.isNull(startGameState.hasDetective)) {
                readDetective(startGameState, message, lang);
            } else if (Objects.isNull(startGameState.hasDoctor)) {
                readDoctor(startGameState, message, lang);
            }
        }
    }


    private void readCitizenNo(StartGameState startGameState, TMessage message, MessageHolder.Lang lang) {
        try {
            startGameState.citizenNo = Long.valueOf(message.getText());
            client.send(new SendMessageWithForceReply()
                .setReplyMarkup(new TForceReply())
                .setReplyToMessageId(message.getId())
                .setText(MessageHolder.get("how.many.mafia.game.has", lang))
                .setChatId(message.getChat().getId()));
        } catch (NumberFormatException e) {
            client.send(new SendMessageWithForceReply()
                .setReplyMarkup(new TForceReply())
                .setText(MessageHolder.get("how.many.citizen.game.has", lang))
                .setReplyToMessageId(message.getId())
                .setChatId(message.getChat().getId()));
        }
    }

    private void readMafiaNo(StartGameState startGameState, TMessage message, MessageHolder.Lang lang) {
        try {
            startGameState.mafiaNo = Long.valueOf(message.getText());
            client.send(new SendMessageWithReplyKeyboard()
                .setReplyMarkup(new TReplyKeyboardMarkup()
                    .setKeyboard(Collections.singletonList(Arrays.asList(
                        MessageHolder.get("yes", lang),
                        MessageHolder.get("no", lang))
                    ))
                    .setOneTimeKeyboard(true)
                    .setSelective(true)
                )
                .setReplyToMessageId(message.getId())
                .setChatId(message.getChat().getId())
                .setText(MessageHolder.get("game.has.detective", lang)));
        } catch (NumberFormatException e) {
            client.send(new SendMessageWithForceReply()
                .setReplyMarkup(new TForceReply())
                .setText(MessageHolder.get("how.many.mafia.game.has", lang))
                .setReplyToMessageId(message.getId())
                .setChatId(message.getChat().getId()));
        }
    }

    private void readDetective(StartGameState startGameState, TMessage message, MessageHolder.Lang lang) {
        startGameState.hasDetective = message.getText().equals(MessageHolder.get("yes", lang));
        client.send(new SendMessageWithReplyKeyboard()
            .setReplyMarkup(new TReplyKeyboardMarkup()
                .setKeyboard(Collections.singletonList(Arrays.asList(
                    MessageHolder.get("yes", lang),
                    MessageHolder.get("no", lang))
                ))
                .setOneTimeKeyboard(true)
                .setSelective(true)
            )
            .setReplyToMessageId(message.getId())
            .setChatId(message.getChat().getId())
            .setText(MessageHolder.get("game.has.doctor", lang)));
    }


    private void readDoctor(StartGameState startGameState, TMessage message, MessageHolder.Lang lang) {
        startGameState.hasDoctor = message.getText().equals(MessageHolder.get("yes", lang));
        Game game = new Game(String.valueOf(message.getChat().getId()),
            startGameState.citizenNo, startGameState.mafiaNo,
            startGameState.hasDoctor, startGameState.hasDetective, botConfiguration.configuration());
        gameContainer.putRoom(message.getChat().getId(), new Room()
            .setGame(game)
            .setRoomId(message.getChat().getId())
            .setLang(lang));
        states.remove(message.getChat().getId());
        client.send(new SendMessageWithReplyKeyboard()
            .setReplyMarkup(new TReplyKeyboardMarkup()
                .setSelective(false)
                .setOneTimeKeyboard(false)
                .setKeyboard(Collections.singletonList(Collections.singletonList(
                    MessageHolder.get("register", lang)))))
            .setChatId(message.getChat().getId())
            .setText(MessageHolder.get("register.to.start", lang))
        );
    }

    private static class StartGameState {
        private Long citizenNo;
        private Long mafiaNo;
        private Boolean hasDetective;
        private Boolean hasDoctor;
    }
}
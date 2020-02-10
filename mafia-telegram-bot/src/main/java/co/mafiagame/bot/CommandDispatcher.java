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

import co.mafiagame.bot.handler.TelegramCallbackHandler;
import co.mafiagame.bot.handler.TelegramCommandHandler;
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.TCallBackQuery;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.exception.MafiaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class CommandDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);
    private Map<String, TelegramCommandHandler> commandHandlers = new HashMap<>();
    private Map<String, TelegramCallbackHandler> callbackHandlers = new HashMap<>();
    private ExecutorService executorService =
            new ThreadPoolExecutor(5, 10, 5,
                    TimeUnit.MINUTES, new LinkedBlockingQueue<>(40));
    private final TelegramClient client;
    private final AccountCache accountCache;

	@Autowired
	public CommandDispatcher(TelegramClient client, AccountCache accountCache) {
		this.client = client;
		this.accountCache = accountCache;
	}

	public static String removeSlash(String message) {
        if (message.startsWith("/"))
            return message.substring(1).toLowerCase();
        else
            return message;
    }

    public void handleCallback(TCallBackQuery callBackQuery) {
        String command = callBackQuery.getData().substring(0, callBackQuery.getData().indexOf(" "));
        executorService.submit(() -> {
            try {
                callbackHandlers.get(command).execute(callBackQuery);
            } catch (MafiaException e) {
                logger.warn(e.getMessage(), e);
                client.send(new SendMessage()
                        .setChatId(callBackQuery.getFrom().getId())
                        .setText(MessageHolder.get(e.getMessageCode(),
                                accountCache.get(callBackQuery.getFrom().getId()).getLang(),
                                e.getMessageArgs())));
            } catch (IllegalArgumentException | IllegalStateException e) {
                logger.warn(e.getMessage(), e);
                client.send(new SendMessage()
                        .setText(MessageHolder.get("illegal.argument",
                                accountCache.get(callBackQuery.getFrom().getId()).getLang()
                        )));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void handleMessage(TMessage message) {
        if (message.isNotCommand())
            return;
        String command = getCommand(message.getText()).orElse(Constants.Command.START_STASHED_GAME);
        TelegramCommandHandler handler = commandHandlers.get(command.toLowerCase());
        executorService.submit(() -> {
            try {
                handler.execute(message);
            } catch (MafiaException e) {
                logger.warn(e.getMessage(), e);
                client.send(new SendMessage()
                        .setChatId(message.getChat().getId())
                        .setText(MessageHolder.get(e.getMessageCode(),
                                accountCache.get(message.getFrom().getId()).getLang(),
                                e.getMessageArgs())));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void registerCommandHandler(String command, TelegramCommandHandler handler) {
        commandHandlers.put(command, handler);
    }

    public void registerCallbackHandler(String command, TelegramCallbackHandler handler) {
        callbackHandlers.put(command, handler);
    }

    private Optional<String> getCommand(String message) {
        String pureMessage = removeSlash(message);
        return commandHandlers.keySet().stream().filter(pureMessage::startsWith).findAny();
    }
}

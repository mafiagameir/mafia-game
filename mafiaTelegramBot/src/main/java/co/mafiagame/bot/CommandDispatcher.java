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
    @Autowired
    private TelegramClient client;
    @Autowired
    private AccountCache accountCache;

    public void handleCallback(TCallBackQuery callBackQuery) {
        String command = callBackQuery.getData().substring(0, callBackQuery.getData().indexOf(" "));
        executorService.submit(() -> {
            try {
                callbackHandlers.get(command).execute(callBackQuery);
            } catch (MafiaException e) {
                logger.warn(e.getMessage(), e);
                client.send(new SendMessage()
                    .setText(MessageHolder.get(e.getMessageCode(),
                        accountCache.get(callBackQuery.getFrom().getId()).getLang(),
                        e.getMessageArgs())));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void handleMessage(TMessage message) {
        if (message.isNotCommand())
            return;
        String command = getCommand(message.getText()).orElse(Constants.Command.START_STASHED_GAME);
        TelegramCommandHandler handler = commandHandlers.get(command);
        executorService.submit(() -> {
            try {
                handler.execute(message);
            } catch (MafiaException e) {
                logger.warn(e.getMessage(), e);
                client.send(new SendMessage()
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

    public static String removeSlash(String message) {
        if (message.startsWith("/"))
            return message.substring(1).toLowerCase();
        else
            return message;
    }
}

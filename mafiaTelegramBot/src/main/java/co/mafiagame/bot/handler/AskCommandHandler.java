package co.mafiagame.bot.handler;

import co.mafiagame.bot.Room;
import co.mafiagame.bot.exception.GameNotStartedYetException;
import co.mafiagame.bot.exception.NotTimeOfAskException;
import co.mafiagame.bot.exception.NotTimeOfMafiaKillException;
import co.mafiagame.bot.telegram.EditMessageTextRequest;
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.TCallBackQuery;
import co.mafiagame.bot.telegram.TInlineKeyboardMarkup;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.GameMood;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class AskCommandHandler extends TelegramCallbackHandler {
    @Override
    protected String getCommandString() {
        return Constants.Command.DETECTIVE_ASK;
    }

    @Override
    public void execute(TCallBackQuery callBackQuery) {
        Long callerId = callBackQuery.getFrom().getId();
        Long roomId = gameContainer.roomOfUser(callerId);
        if (Objects.isNull(roomId))
            throw new GameNotStartedYetException();
        Room room = gameContainer.room(roomId);
        String target = callBackQuery.getData().substring(callBackQuery.getData().indexOf(" "));
        boolean result = room.getGame().ask(String.valueOf(callerId), target);
        client.editMessageText(new EditMessageTextRequest()
            .setText(MessageHolder.get(result ? "user.role.is.mafia" : "user.role.is.not.mafia",
                room.getLang(),
                room.findPlayer(Long.valueOf(target))
                    .orElseThrow(IllegalStateException::new).fullName()))
            .setMessageId(callBackQuery.getMessage().getId())
            .setChatId(callBackQuery.getMessage().getChat().getId())
            .setReplyMarkup(new TInlineKeyboardMarkup().setInlineKeyboard(Collections.emptyList()))
        );
    }
}

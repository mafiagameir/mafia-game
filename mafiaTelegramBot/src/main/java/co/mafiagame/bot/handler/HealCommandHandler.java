package co.mafiagame.bot.handler;

import co.mafiagame.bot.Room;
import co.mafiagame.bot.exception.GameNotStartedYetException;
import co.mafiagame.bot.telegram.EditMessageTextRequest;
import co.mafiagame.bot.telegram.TCallBackQuery;
import co.mafiagame.bot.telegram.TInlineKeyboardMarkup;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class HealCommandHandler extends TelegramCallbackHandler {
    @Override
    protected String getCommandString() {
        return Constants.Command.DOCTOR_HEAL;
    }

    @Override
    public void execute(TCallBackQuery callBackQuery) {
        Integer callerId = callBackQuery.getFrom().getId();
        Integer roomId = gameContainer.roomOfUser(callerId);
        if (Objects.isNull(roomId))
            throw new GameNotStartedYetException();
        Room room = gameContainer.room(roomId);
        String target = callBackQuery.getData().substring(callBackQuery.getData().indexOf(" "));
        room.getGame().heal(String.valueOf(callerId), target);
        client.editMessageText(new EditMessageTextRequest()
            .setText(MessageHolder.get("ok",
                room.getLang(),
                room.findPlayer(Integer.valueOf(target))
                    .orElseThrow(IllegalStateException::new).fullName()))
            .setMessageId(callBackQuery.getMessage().getId())
            .setChatId(callBackQuery.getMessage().getChat().getId())
            .setReplyMarkup(new TInlineKeyboardMarkup().setInlineKeyboard(Collections.emptyList()))
        );
    }
}

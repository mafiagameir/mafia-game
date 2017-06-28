package co.mafiagame.bot.handler;

import co.mafiagame.bot.Room;
import co.mafiagame.bot.telegram.SendMessageWithReplyKeyboard;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.telegram.TReplyKeyboardMarkup;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import co.mafiagame.engine.Player;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class StartFinalElectionCommandHandler extends TelegramCommandHandler {
    @Override
    protected Collection<String> getCommandString() {
        return Collections.singleton(Constants.Command.START_FINAL_ELECTION);
    }

    @Override
    public void execute(TMessage message) {
        if (!message.isGroup()) {
            sendMessage(message, "start.election.not.allowed.in.private",
                    getLang(message), false);
            return;
        }
        Room room = gameContainer.room(message.getChat().getId());
        if (Objects.isNull(room)) {
            sendMessage(message, "game.not.started.yet", getLang(message), true);
            return;
        }
        room.getGame().startFinalElection();
        List<Player> players = room.getGame().alivePlayer();
        List<String> options = players.stream()
                .map(Player::getUserId)
                .map(Integer::valueOf)
                .map(accountCache::get).map(a ->
                        MessageHolder.get("vote.to", room.getLang()) + " " + a.fullName())
                .collect(Collectors.toList());
        options.add(MessageHolder.get("done", room.getLang()));
        client.send(new SendMessageWithReplyKeyboard()
                .setReplyMarkup(new TReplyKeyboardMarkup()
                        .setSelective(false)
                        .setOneTimeKeyboard(false)
                        .addOptions(options)
                )
                .setChatId(message.getChat().getId())
                .setText(MessageHolder.get("final.election.started", room.getLang()))
        );
    }
}

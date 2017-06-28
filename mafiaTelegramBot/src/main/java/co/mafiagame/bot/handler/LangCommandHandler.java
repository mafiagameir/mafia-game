package co.mafiagame.bot.handler;

import co.mafiagame.bot.persistence.repository.AccountRepository;
import co.mafiagame.bot.telegram.EditMessageReplyMarkupRequest;
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.TCallBackQuery;
import co.mafiagame.bot.telegram.TInlineKeyboardMarkup;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class LangCommandHandler extends TelegramCallbackHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected String getCommandString() {
        return Constants.Command.LANG;
    }


    @Override
    public void execute(TCallBackQuery message) {
        String lang = message.getData().substring(message.getData().indexOf(" ") + 1);
        setLang(message.getFrom().getId(), MessageHolder.Lang.valueOf(lang));
        client.editMessageReplyMarkup(new EditMessageReplyMarkupRequest()
                .setChatId(message.getMessage().getChat().getId())
                .setMessageId(message.getMessage().getId())
                .setReplyMarkup(new TInlineKeyboardMarkup().setInlineKeyboard(Collections.emptyList()))
        );
        client.send(new SendMessage()
                .setChatId(message.getMessage().getChat().getId())
                .setText(MessageHolder.get("add.me.to.group", MessageHolder.Lang.valueOf(lang))));
    }

    private void setLang(Integer userId, MessageHolder.Lang lang) {
        accountRepository.save(
                accountRepository.findByTelegramUserId(userId)
                        .setLang(lang));
    }
}

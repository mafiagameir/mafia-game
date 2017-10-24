package co.mafiagame.bot.handler;

import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.telegram.SendMessageWithInlineKeyboard;
import co.mafiagame.bot.telegram.TInlineKeyboardButton;
import co.mafiagame.bot.telegram.TInlineKeyboardMarkup;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.Constants;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class LandStrCommandHandler extends TelegramCommandHandler {
	@Override
	protected Collection<String> getCommandString() {
		return Collections.singleton(Constants.Command.LANG);
	}

	@Override
	public void execute(TMessage message) {
		MessageHolder.Lang lang = null;
		if (message.getText().toLowerCase().endsWith("en"))
			lang = MessageHolder.Lang.EN;
		else if (message.getText().toLowerCase().endsWith("fa"))
			lang = MessageHolder.Lang.FA;
		else
			sendKeyboard(message.getChat().getId());
		if (Objects.nonNull(lang)) {
			Account account = accountRepository.findByTelegramUserId(message.getFrom().getId());
			if (Objects.isNull(account)) {
				account = accountRepository.save(new Account(message.getFrom()).setLang(lang));
				accountCache.put(account.getTelegramUserId(), account);
			} else
				accountRepository.save(account.setLang(lang));
		}
	}

	private void sendKeyboard(Long chatId) {
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
						.setChatId(chatId)
						.setText(MessageHolder.get("set.lang", MessageHolder.Lang.FA)));
	}
}

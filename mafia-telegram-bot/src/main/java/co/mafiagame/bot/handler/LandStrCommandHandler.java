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

package co.mafiagame.bot.handler;

import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.telegram.*;
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
			} else {
				account = accountRepository.save(account.setLang(lang));
				accountCache.put(account.getTelegramUserId(), account);
			}
			client.send(new SendMessage()
					.setChatId(message.getChat().getId())
					.setText(MessageHolder.get("language.changed", lang))
			);
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

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
import java.util.Objects;

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
		setLang(message.getFrom().getId(), MessageHolder.Lang.valueOf(lang), message);
		client.editMessageReplyMarkup(new EditMessageReplyMarkupRequest()
				.setChatId(message.getMessage().getChat().getId())
				.setMessageId(message.getMessage().getId())
				.setReplyMarkup(new TInlineKeyboardMarkup().setInlineKeyboard(Collections.emptyList()))
		);
		client.send(new SendMessage()
				.setChatId(message.getMessage().getChat().getId())
				.setText(MessageHolder.get("add.me.to.group", MessageHolder.Lang.valueOf(lang))));
	}

	private void setLang(Long userId, MessageHolder.Lang lang, TCallBackQuery message) {
		Account account = accountRepository.findByTelegramUserId(userId);
		if (Objects.isNull(account)) {
			account = accountRepository.save(new Account(message.getFrom()).setLang(lang));
			accountCache.put(account.getTelegramUserId(), account);
		} else
			accountRepository.save(account.setLang(lang));
	}
}

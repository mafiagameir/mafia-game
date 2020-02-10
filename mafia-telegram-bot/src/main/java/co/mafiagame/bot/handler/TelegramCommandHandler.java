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

import co.mafiagame.bot.AccountCache;
import co.mafiagame.bot.CommandDispatcher;
import co.mafiagame.bot.GameContainer;
import co.mafiagame.bot.TelegramClient;
import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.persistence.repository.AccountRepository;
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.util.MessageHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
public abstract class TelegramCommandHandler {
	@Autowired
	private CommandDispatcher commandDispatcher;
	@Autowired
	protected TelegramClient client;
	@Autowired
	protected AccountRepository accountRepository;
	@Autowired
	protected AccountCache accountCache;
	@Autowired
	protected GameContainer gameContainer;

	@PostConstruct
	protected final void init() {
		getCommandString().forEach(command ->
				commandDispatcher.registerCommandHandler(command.toLowerCase(), this));
	}

	protected abstract Collection<String> getCommandString();

	public abstract void execute(TMessage message);

	protected void sendMessage(TMessage message, String code, MessageHolder.Lang lang, boolean reply) {
		client.send(new SendMessage()
				.setText(MessageHolder.get(code, lang))
				.setChatId(message.getChat().getId())
				.setReplyToMessageId(reply ? message.getId() : null)
		);
	}

	protected MessageHolder.Lang getLang(TMessage message) {
		Account account = accountCache.get(message.getFrom().getId());
		MessageHolder.Lang lang = MessageHolder.Lang.EN;
		if (Objects.nonNull(account))
			lang = account.getLang();
		return lang;
	}
}

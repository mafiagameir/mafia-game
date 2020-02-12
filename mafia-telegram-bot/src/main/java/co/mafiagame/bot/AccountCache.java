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

import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.persistence.repository.AccountRepository;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class AccountCache {
	private final CacheManager cacheManager;
	private final AccountRepository accountRepository;

	private Cache<Long, Account> accountCache;

	@Autowired
	public AccountCache(CacheManager cacheManager, AccountRepository accountRepository) {
		this.cacheManager = cacheManager;
		this.accountRepository = accountRepository;
	}


	@PostConstruct
	public void init() {
		accountCache = cacheManager.createCache("accountCache",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, Account.class,
						ResourcePoolsBuilder.heap(1000)));

	}

	public void put(Long userId, Account account) {
		accountCache.put(userId, account);
	}

	public Account get(Long userId) {
		Account account = accountCache.get(userId);
		if (Objects.isNull(account)) {
			account = accountRepository.findByTelegramUserId(userId);
			if (Objects.nonNull(account))
				put(account.getTelegramUserId(), account);
		}
		return account;
	}

}

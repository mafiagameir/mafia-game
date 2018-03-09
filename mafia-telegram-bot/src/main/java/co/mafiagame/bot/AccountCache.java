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
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private AccountRepository accountRepository;

    private Cache<Long, Account> accountCache;


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

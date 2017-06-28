/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.bot.persistence.domain;


import co.mafiagame.bot.telegram.TUser;
import co.mafiagame.bot.util.MessageHolder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author hekmatof
 */
@Entity
public class Account {
    @Id
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", allocationSize = 10)
    private Long id;
    @Column(nullable = false, unique = true)
    private Integer telegramUserId;
    private String username;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private MessageHolder.Lang lang;

    public Account(TUser from) {
        this.username = from.getUsername();
        this.firstName = from.getFirstName();
        this.lastName = from.getLastName();
        this.telegramUserId = from.getId();
    }

    public Account() {
    }

    @Transient
    public String fullName() {
        return (StringUtils.isEmpty(firstName) ? "" : firstName) +
            " " + (StringUtils.isEmpty(lastName) ? "" : lastName);
    }

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getTelegramUserId() {
        return telegramUserId;
    }

    public Account setTelegramUserId(Integer telegramUserId) {
        this.telegramUserId = telegramUserId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Account setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Account setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Account setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public MessageHolder.Lang getLang() {
        return lang;
    }

    public Account setLang(MessageHolder.Lang lang) {
        this.lang = lang;
        return this;
    }
}

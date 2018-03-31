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

package co.mafiagame.bot.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hekmatof
 */
@Entity
public class Audit {
    @Id
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", allocationSize = 10)
    private Long id;
    @ManyToOne
    private Account actor;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String roomId;
    @Enumerated(EnumType.STRING)
    private Action action;

    public Long getId() {
        return id;
    }

    public Audit setId(Long id) {
        this.id = id;
        return this;
    }

    public Account getActor() {
        return actor;
    }

    public Audit setActor(Account actor) {
        this.actor = actor;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Audit setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public Audit setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public Audit setAction(Action action) {
        this.action = action;
        return this;
    }
}

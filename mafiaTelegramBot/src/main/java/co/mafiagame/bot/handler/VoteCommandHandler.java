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

package co.mafiagame.bot.handler;

import co.mafiagame.bot.Room;
import co.mafiagame.bot.exception.GameNotStartedYetException;
import co.mafiagame.bot.persistence.domain.Account;
import co.mafiagame.bot.telegram.SendMessage;
import co.mafiagame.bot.telegram.SendMessageWithRemoveKeyboard;
import co.mafiagame.bot.telegram.TMessage;
import co.mafiagame.bot.telegram.TReplyKeyboardRemove;
import co.mafiagame.bot.util.MessageHolder;
import co.mafiagame.engine.ElectionResult;
import co.mafiagame.engine.GameMood;
import co.mafiagame.engine.Player;
import co.mafiagame.engine.Vote;
import co.mafiagame.engine.exception.PlayerNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class VoteCommandHandler extends TelegramCommandHandler {
    private Map<Integer, List<Account>> votes = new HashMap<>();

    @Override
    protected Collection<String> getCommandString() {
        return Arrays.asList(MessageHolder.get("vote.to", MessageHolder.Lang.FA),
                MessageHolder.get("vote.to", MessageHolder.Lang.EN),
                MessageHolder.get("done", MessageHolder.Lang.EN),
                MessageHolder.get("done", MessageHolder.Lang.FA)
        );
    }

    @Override
    public void execute(TMessage message) {
        if (!message.isGroup()) {
            sendMessage(message, "vote.not.allowed.in.private",
                    getLang(message), false);
            return;
        }
        Room room = gameContainer.room(message.getChat().getId());
        if (Objects.isNull(room))
            throw new GameNotStartedYetException();
        if (!votes.containsKey(message.getFrom().getId()))
            votes.put(message.getFrom().getId(), new ArrayList<>());

        if (MessageHolder.get("done", room.getLang()).equals(message.getText()))
            handleDone(message, room);
        else {
            int length = MessageHolder.get("vote.to", room.getLang()).length();
            String fullName = message.getText().substring(length);
            Account candid = room.findPlayer(fullName).orElseThrow(() -> new PlayerNotFoundException(fullName));
            votes.get(message.getFrom().getId()).add(candid);
        }
    }

    private void handleDone(TMessage message, Room room) {
        List<Account> userVote = votes.get(message.getFrom().getId());
        ElectionResult electionResult = room.getGame().vote(
                new Vote(String.valueOf(message.getFrom().getId()).trim(), userVote.stream()
                        .map(Account::getTelegramUserId)
                        .map(String::valueOf).collect(Collectors.toList())));
        Account voter = room.findPlayer(message.getFrom().getId())
                .orElseThrow(() -> new PlayerNotFoundException(
                        message.getFrom().getFirstName() + " " + message.getFrom().getLastName()));
        client.send(new SendMessageWithRemoveKeyboard()
                .setChatId(message.getChat().getId())
                .setReplyToMessageId(message.getId())
                .setText(userVote.isEmpty() ?
                        MessageHolder.get("user.vote.nobody", room.getLang(), voter.fullName()) :
                        MessageHolder.get("user.vote.another", room.getLang(), voter.fullName(),
                                userVote.stream().map(Account::fullName).collect(Collectors.joining(
                                        " " + MessageHolder.get("and", room.getLang()) + " "
                                )))));
        if (Objects.nonNull(electionResult))
            handleElectionOver(message, room, electionResult);
    }

    private void handleElectionOver(TMessage message, Room room, ElectionResult electionResult) {
        client.send(new SendMessageWithRemoveKeyboard()
                .setReplyMarkup(new TReplyKeyboardRemove().setSelective(false))
                .setChatId(message.getChat().getId())
                .setText(MessageHolder.get("election.result", room.getLang()) +
                        electionResult.getResult().keySet().stream()
                                .map(k -> MessageHolder.get("vote.for.user", room.getLang(),
                                        room.findPlayer(Integer.valueOf(k)).get().fullName(),
                                        String.valueOf(electionResult.getResult().get(k))))
                                .collect(Collectors.joining("\n"))
                ));
        if (room.getGame().getGameMood() == GameMood.NIGHT_MAFIA) {
            if (electionResult.isSingleResult()) {
                if (Player.NOBODY_USERID.equals(electionResult.getElects().get(0)))
                    sendMessage(message, "nobody.was.killed.with.maximum.votes",
                            room.getLang(), false);
                else
                    handlePlayerKilled(room, electionResult);
            } else
                handleNobodyWasKilled(room, electionResult);
        }
    }

    private void handlePlayerKilled(Room room, ElectionResult electionResult) {
        Integer victim = Integer.valueOf(electionResult.getElects().get(0));
        client.send(new SendMessage()
                .setChatId(room.getRoomId())
                .setText(MessageHolder.get("player.was.killed.with.maximum.votes", room.getLang(),
                        room.findPlayer(victim)
                                .orElseThrow(IllegalStateException::new).fullName(),
                        String.valueOf(electionResult.getResult().get(electionResult.getElects().get(0)))
                ))
        );
        gameContainer.removeUser(victim);
    }

    private void handleNobodyWasKilled(Room room, ElectionResult electionResult) {
        client.send(new SendMessage()
                .setChatId(room.getRoomId())
                .setText(MessageHolder.get("nobody.was.killed.because.more.than.one.user.has.equal.vote",
                        room.getLang(),
                        electionResult.getElects().stream().map(room::findPlayer)
                                .map(Optional::get).map(Account::fullName)
                                .collect(Collectors.joining(MessageHolder.get("and", room.getLang()))),
                        String.valueOf(electionResult.getResult().get(electionResult.getElects().get(0)))
                ))
        );
    }
}

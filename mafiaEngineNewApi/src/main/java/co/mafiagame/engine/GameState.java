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

package co.mafiagame.engine;

import co.mafiagame.engine.exception.PlayerNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
class GameState {
    private boolean gameStarted = false;
    private long citizenNo;
    private long mafiaNo;
    private boolean hasDetective;
    private boolean hasDoctor;
    private Map<String, Player> alivePlayersMap = new HashMap<>();

    GameState(long citizenNo, long mafiaNo,
              boolean hasDetective, boolean hasDoctor) {
        this.citizenNo = citizenNo;
        this.mafiaNo = mafiaNo;
        this.hasDetective = hasDetective;
        this.hasDoctor = hasDoctor;
    }

    void registerPlayer(Player player) {
        if (gameStarted)
            throw new IllegalStateException("register a player after game started is impossible");
        alivePlayersMap.put(player.getUserId(), player);
        if (totalPlayer() == alivePlayersMap.size())
            gameStarted = true;
    }

    void killPlayer(String playerUserId) {
        if ("nobody".equals(playerUserId))
            return;
        Player removed = alivePlayersMap.remove(playerUserId);
        switch (removed.getRole()) {
            case CITIZEN:
                citizenNo--;
                break;
            case MAFIA:
                mafiaNo--;
                break;
            case DETECTIVE:
                hasDetective = false;
                break;
            case DOCTOR:
                hasDoctor = false;
                break;
            default: //TODO: should be think about
                throw new IllegalStateException("player's role with id '" + removed.getUserId() + "' is invalid");
        }
    }

    long totalPlayer() {
        return citizenNo + mafiaNo + (hasDetective ? 1 : 0) + (hasDoctor ? 1 : 0);
    }

    List<Player> alivePlayers() {
        return new ArrayList<>(alivePlayersMap.values());
    }

    List<Player> mafiaPlayers() {
        return new ArrayList<>(alivePlayersMap.values().stream().filter(p -> p.getRole() == Role.MAFIA).collect(Collectors.toList()));
    }

    Optional<Player> doctor() {
        return alivePlayersMap.values().stream().filter(p -> p.getRole() == Role.DOCTOR).findAny();
    }

    Optional<Player> detective() {
        return alivePlayersMap.values().stream().filter(p -> p.getRole() == Role.DETECTIVE).findAny();
    }

    Player player(String userId) {
        return alivePlayersMap.get(userId);
    }

    void checkPlayerExist(String userId) throws PlayerNotFoundException {
        if (!alivePlayersMap.containsKey(userId) && !Player.NOBODY.getUserId().equals(userId))
            throw new PlayerNotFoundException(userId);
    }

    long getCitizenNo() {
        return citizenNo;
    }

    long getMafiaNo() {
        return mafiaNo;
    }

    boolean hasDetective() {
        return hasDetective;
    }

    boolean hasDoctor() {
        return hasDoctor;
    }

    public boolean gameStarted() {
        return gameStarted;
    }

    @Override
    public String toString() {
        return "(totalPlayer:" + totalPlayer() +
            ", citizenNo=" + citizenNo +
            ", mafiaNo=" + mafiaNo +
            ", hasDetective=" + hasDetective +
            ", hasDoctor=" + hasDoctor + ')';
    }
}

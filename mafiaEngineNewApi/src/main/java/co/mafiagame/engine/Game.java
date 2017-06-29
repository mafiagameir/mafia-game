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

import co.mafiagame.engine.exception.*;

import java.util.*;

import static co.mafiagame.engine.Constants.Command.*;
import static co.mafiagame.engine.GameMood.*;

/**
 * Main (mutable and thread safe) class for the game which responsible for initialize and manage game state,
 *
 * @author Esa Hekmatizadeh
 */
public class Game {
    private final String gameId; //this property come from client system
    private final Date createdDate;
    private Date lastUpdate;
    private final GameState gameState;
    private GameMood gameMood;
    private final Configuration configuration;

    private final GameSetup gameSetup; //backup of initial  game setup

    public boolean isElectionStarted() {
        return electionStarted;
    }

    //election states
    private boolean electionStarted = false;
    private boolean finalElection = false;
    private final Set<Vote> votes = new HashSet<>();

    private Player killCandidate;
    private GameResult gameResult = GameResult.UNKNOWN;

    /**
     * define a game to play (in NOT_STARTED mode)
     *
     * @param gameId        the game identification from client system
     * @param citizenNo     the number of citizen players
     * @param mafiaNo       the number of mafia players
     * @param hasDoctor     indicate the game contain a doctor
     * @param hasDetective  indicate the game contain a detective
     * @param configuration the client system configuration which indicate listeners of game events
     */
    public Game(String gameId, int citizenNo, int mafiaNo, boolean hasDoctor, boolean hasDetective,
                Configuration configuration) {
        if (mafiaNo <= 0)
            throw new IllegalArgumentException("mafiaNo may not be 0 or lesser");
        if (citizenNo <= 0)
            throw new IllegalArgumentException("citizenNo may not be 0 or lesser");
        this.gameId = gameId;
        createdDate = new Date();
        lastUpdate = new Date();
        gameState = new GameState(citizenNo, mafiaNo,  hasDetective,hasDoctor);
        gameMood = NOT_STARTED;
        this.configuration = configuration;
        gameSetup = new GameSetup(citizenNo, mafiaNo, hasDetective, hasDoctor);
    }

    /**
     * register a new player into game while game is in NOT_STARTED mode.
     * if the game can be started with this new player (number of player will be equal
     * to number of players which define in game setup) return true else return false
     *
     * @param userId player Id to register
     * @return true if game started
     */
    public synchronized boolean registerPlayer(String userId) {
        lastUpdate = new Date();
        gameState.registerPlayer(new Player(userId));
        if (gameState.gameStarted()) {
            assignRoles();
            gameState.alivePlayers().forEach(gameSetup::setPlayerRole);
            gameMood = DAY;
            return true;
        }
        return false;
    }

    /**
     * start a primary election in DAY mood.
     *
     * @throws CommandNotAvailableInThisMoodException if the mood is night
     * @throws ElectionAlreadyStartedException        election already started and no need to this command
     */
    public synchronized void startPrimaryElection() throws ElectionAlreadyStartedException,
            CommandNotAvailableInThisMoodException, NoElectionStartedException {
        lastUpdate = new Date();
        checkElectionCondition(START_ELECTION);
        electionStarted = true;
    }

    /**
     * start a final election in DAY mood.
     *
     * @throws CommandNotAvailableInThisMoodException if the mood is night
     * @throws ElectionAlreadyStartedException        election already started and no need to this command
     */
    public synchronized void startFinalElection()
            throws ElectionAlreadyStartedException, CommandNotAvailableInThisMoodException, NoElectionStartedException {
        lastUpdate = new Date();
        checkElectionCondition(START_FINAL_ELECTION);
        electionStarted = true;
        finalElection = true;
    }

    /**
     * cast a player's vote, if this is a vote of last player, election is over and the result returned,
     * else return null
     *
     * @param vote the vote to cast
     * @return ElectionResult if the election is over, otherwise null
     * @throws CommandNotAvailableInThisMoodException if the mood is night
     * @throws NoElectionStartedException             there isn't any election started until now
     * @throws PlayerNotFoundException                userId of voter or one of the candidate not found in alive player
     */
    public synchronized ElectionResult vote(Vote vote) throws ElectionAlreadyStartedException,
            CommandNotAvailableInThisMoodException, NoElectionStartedException, PlayerNotFoundException {
        lastUpdate = new Date();
        checkElectionCondition(VOTE);
        gameState.checkPlayerExist(vote.getVoterUserId());
        for (String candidate : vote.getCandidateUserIds())
            gameState.checkPlayerExist(candidate);
        votes.add(vote);
        if (gameState.totalPlayer() == votes.size()) {
            ElectionResult result = new ElectionResult(votes);
            if (finalElection && result.isSingleResult()) {
                if (!Player.NOBODY_USERID.equals(result.getElects().get(0)))
                    gameState.killPlayer(result.getElects().get(0));
                if (!checkGameOver())
                    gameMood = nextMood();
                configuration.changeTurn(this);
            }
            clearElection();
            return result;
        }
        return null;
    }

    /**
     * cast mafia kill vote,if this is a vote of last mafia, election is over and the result returned,
     * return null otherwise
     *
     * @param vote of the mafia
     * @return ElectionResult if the election is over, otherwise null
     * @throws PlayerNotFoundException                userId of voter or one of the candidate not found in alive player
     * @throws CommandNotAvailableInThisMoodException it's not MAFIA_NIGHT
     * @throws YouAreNotMafiaException                if the voter role is not mafia
     */
    public synchronized ElectionResult kill(Vote vote) throws YouAreNotMafiaException,
            ElectionAlreadyStartedException, CommandNotAvailableInThisMoodException,
            NoElectionStartedException, PlayerNotFoundException {
        lastUpdate = new Date();
        checkElectionCondition(MAFIA_VOTE);
        gameState.checkPlayerExist(vote.getVoterUserId());
        for (String candidate : vote.getCandidateUserIds())
            gameState.checkPlayerExist(candidate);

        if (gameState.player(vote.getVoterUserId()).getRole() != Role.MAFIA)
            throw new YouAreNotMafiaException();
        votes.add(vote);
        if (votes.size() == gameState.getMafiaNo()) {
            ElectionResult electionResult = new ElectionResult(votes);
            if (electionResult.isSingleResult()) {
                killCandidate = gameState.player(electionResult.getElects().get(0));
                gameMood = nextMood();
                configuration.changeTurn(this);
            }
            clearElection();
            return electionResult;
        }
        return null;
    }

    /**
     * ask command which should be called on right time for right person, command check
     * if the requester is really a detective and if its OK to ask now
     *
     * @param requesterUserId the person who ask - detective but engine check the role
     * @param whomUserId      the player user id, detective wants to know him/her role
     * @return true if whomUserId is mafia and false if not
     * @throws CommandNotAvailableInThisMoodException if game mood is not NIGHT_DETECTIVE
     * @throws PlayerNotFoundException                requester or whom not found in alive player
     * @throws YouAreNotDetectiveException            if the requester role is not DETECTIVE
     */

    public synchronized boolean ask(String requesterUserId, String whomUserId)
            throws CommandNotAvailableInThisMoodException, YouAreNotDetectiveException, PlayerNotFoundException {
        lastUpdate = new Date();
        if (gameMood != NIGHT_DETECTIVE)
            throw new CommandNotAvailableInThisMoodException(DETECTIVE_ASK, this.gameMood);
        gameState.checkPlayerExist(whomUserId);
        Player realDetective = gameState.detective().orElseThrow(YouAreNotDetectiveException::new);
        if (!Objects.equals(realDetective.getUserId(), requesterUserId))
            throw new YouAreNotDetectiveException();
        gameMood = nextMood();
        configuration.changeTurn(this);
        return gameState.player(whomUserId).getRole() == Role.MAFIA;
    }

    /**
     * heal command which should be called on NIGHT_DOCTOR by doctor
     *
     * @param requesterUserId the player who send command
     * @param whomUserId      the player userId doctor want to heal
     * @throws CommandNotAvailableInThisMoodException if it's not NIGHT_DOCTOR
     * @throws PlayerNotFoundException                if the requesterUserId or whomUserId not found in alive player
     * @throws YouAreNotDoctorException               if the requesterUserId role is not DOCTOR
     */
    public synchronized void heal(String requesterUserId, String whomUserId)
            throws CommandNotAvailableInThisMoodException,
            PlayerNotFoundException, YouAreNotDoctorException {
        lastUpdate = new Date();
        if (gameMood != NIGHT_DOCTOR)
            throw new CommandNotAvailableInThisMoodException(DOCTOR_HEAL, this.gameMood);
        gameState.checkPlayerExist(whomUserId);
        Player realDoctor = gameState.doctor().orElseThrow(YouAreNotDoctorException::new);
        if (!Objects.equals(realDoctor.getUserId(), requesterUserId))
            throw new YouAreNotDoctorException();
        if (killCandidate.getUserId().equals(whomUserId))
            killCandidate = Player.NOBODY;
        gameMood = nextMood();
        configuration.changeTurn(this);
    }

    /**
     * kill a user abnormally usually cause user cancel
     *
     * @param userId user to kill
     * @throws PlayerNotFoundException if the userId is not found in alive players
     */
    public synchronized void externalKill(String userId) throws PlayerNotFoundException {
        lastUpdate = new Date();
        gameState.checkPlayerExist(userId);
        gameState.killPlayer(userId);
        checkGameOver();
    }

    private boolean checkGameOver() {
        long citizenSum = gameState.totalPlayer() - gameState.getMafiaNo();
        if (gameState.getMafiaNo() >= citizenSum)
            gameResult = GameResult.MAFIAS_WIN;
        else if (gameState.getMafiaNo() == 0)
            gameResult = GameResult.CITIZEN_WIN;
        if (gameResult != GameResult.UNKNOWN) {
            gameMood = ENDED;
            configuration.changeTurn(this);
            return true;
        }
        return false;
    }

    private GameMood nextMood() {
        switch (gameMood) {
            case DAY:
                return NIGHT_MAFIA;
            case NIGHT_MAFIA:
                if (gameState.hasDetective())
                    return NIGHT_DETECTIVE;
            case NIGHT_DETECTIVE:
                if (gameState.hasDoctor())
                    return NIGHT_DOCTOR;
            case NIGHT_DOCTOR:
            default:
                //sunrise
                gameState.killPlayer(killCandidate.getUserId());
                if (checkGameOver())
                    return ENDED;
                return DAY;
        }
    }

    private void assignRoles() {
        List<Player> players = gameState.alivePlayers();
        Collections.shuffle(players);
        int assigned = 0;
        for (int i = 0; i < gameState.getCitizenNo(); i++)
            players.get(assigned + i).setRole(Role.CITIZEN);
        assigned += gameState.getCitizenNo();
        for (int i = 0; i < gameState.getMafiaNo(); i++)
            players.get(assigned + i).setRole(Role.MAFIA);
        assigned += gameState.getMafiaNo();
        if (gameState.hasDetective()) {
            players.get(assigned).setRole(Role.DETECTIVE);
            assigned++;
        }
        if (gameState.hasDoctor())
            players.get(assigned).setRole(Role.DOCTOR);
    }

    private void checkElectionCondition(String command)
            throws CommandNotAvailableInThisMoodException, ElectionAlreadyStartedException,
            NoElectionStartedException {
        if ((!Objects.equals(command, MAFIA_VOTE) && gameMood != DAY) ||
                (Objects.equals(command, MAFIA_VOTE) && gameMood != NIGHT_MAFIA))
            throw new CommandNotAvailableInThisMoodException(command, gameMood);
        if ((Objects.equals(command, START_ELECTION) ||
                Objects.equals(command, START_FINAL_ELECTION)) && electionStarted)
            throw new ElectionAlreadyStartedException();
        if (Objects.equals(command, VOTE) && !electionStarted)
            throw new NoElectionStartedException();
    }

    private void clearElection() {
        finalElection = false;
        electionStarted = false;
        votes.clear();
    }

    //accessors
    Player killCandidate() {
        return killCandidate;
    }

    /**
     * return the {@link Player} object related to this game and userId provided
     *
     * @param userId userId of player
     * @return player
     * @throws PlayerNotFoundException if player not found in alive players
     */
    public Player player(String userId) throws PlayerNotFoundException {
        gameState.checkPlayerExist(userId);
        return gameState.player(userId);
    }

    /**
     * get the list of alive players
     *
     * @return list of alive players
     */
    public List<Player> alivePlayer() {
        return gameState.alivePlayers();
    }

    /**
     * get the list of mafia players
     *
     * @return list of mafia players
     */
    public List<Player> mafias() {
        return gameState.mafiaPlayers();
    }

    /**
     * get detective
     *
     * @return optional of detective player
     */
    public Optional<Player> detective() {
        return gameState.detective();
    }

    /**
     * get doctor
     *
     * @return optional of doctor player
     */
    public Optional<Player> doctor() {
        return gameState.doctor();
    }

    /**
     * get game initial setup
     *
     * @return game initial setup
     */
    public GameSetup getGameSetup() {
        return gameSetup;
    }

    GameResult gameResult() {
        return gameResult;
    }

    public GameMood getGameMood() {
        return gameMood;
    }

    public String getGameId() {
        return gameId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public boolean isFinalElection() {
        return finalElection;
    }

    @Override
    public String toString() {
        return "Game " + gameId + ": (" +
                ", gameState=" + gameState +
                ", gameMood=" + gameMood +
                ", gameSetup=" + gameSetup +
                ", gameResult=" + gameResult +
                ')';
    }
}

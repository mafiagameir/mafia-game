package co.mafiagame.bot.management;

import java.util.Date;
import java.util.List;

/**
 * @author Esa Hekmatizadeh
 */
public class BotStatus {
    private int playingGames;
    private int playingPlayers;
    private List<Date> lastUpdates;

    public int getPlayingGames() {
        return playingGames;
    }

    public BotStatus setPlayingGames(int playingGames) {
        this.playingGames = playingGames;
        return this;
    }

    public int getPlayingPlayers() {
        return playingPlayers;
    }

    public BotStatus setPlayingPlayers(int playingPlayers) {
        this.playingPlayers = playingPlayers;
        return this;
    }

    public List<Date> getLastUpdates() {
        return lastUpdates;
    }

    public BotStatus setLastUpdates(List<Date> lastUpdates) {
        this.lastUpdates = lastUpdates;
        return this;
    }
}

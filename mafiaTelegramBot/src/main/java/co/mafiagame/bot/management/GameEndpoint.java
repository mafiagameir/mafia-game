package co.mafiagame.bot.management;


import co.mafiagame.bot.GameContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.stereotype.Component;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class GameEndpoint extends AbstractEndpoint<BotStatus> {
    @Autowired
    private GameContainer container;

    public GameEndpoint() {
        super("bot", false, true);
    }

    @Override
    public BotStatus invoke() {
        return new BotStatus()
                .setPlayingGames(container.roomNumber())
                .setPlayingPlayers(container.playerNumber())
                .setLastUpdates(container.lastUpdates());
    }
}

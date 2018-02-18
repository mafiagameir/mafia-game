package co.mafiagame.bot;

import co.mafiagame.bot.persistence.domain.Action;
import co.mafiagame.bot.persistence.domain.Audit;
import co.mafiagame.bot.persistence.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class GameContainer {
    private final Map<Long, Long> userRooms = new HashMap<>(); //userId -> roomId
    private final Map<Long, Room> rooms = new HashMap<>(); //roomId-> Room
    private ScheduledExecutorService sExecutorService = new ScheduledThreadPoolExecutor(1);
    @Autowired
    private AuditRepository auditRepository;

    @PostConstruct
    private void init() {
        //cleanup should run on 10 AM/PM O'clock
        Calendar now = Calendar.getInstance();
        long initialDelayTime = (12 + (10 - now.get(Calendar.HOUR))) % 12;
        sExecutorService.scheduleAtFixedRate(this::cleanUp, initialDelayTime, 12, TimeUnit.HOURS);
    }

    private void cleanUp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -12);
        rooms.values().stream()
                .filter(room -> room.getGame().getLastUpdate().before(cal.getTime()))
                .forEach(room -> {
                    room.getAccounts().forEach(a -> {
                        auditRepository.save(new Audit()
                                .setAction(Action.GAME_TIMEOUT)
                                .setActor(a)
                                .setDate(new Date())
                                .setRoomId(String.valueOf(room.getRoomId())));
                        this.removeUser(a.getTelegramUserId());
                    });
                    removeRoom(Long.valueOf(room.getGame().getGameId()));

                });
    }

    public Long roomOfUser(Long userId) {
        return userRooms.get(userId);
    }

    public Room room(Long roomId) {
        return rooms.get(roomId);
    }

    public void putRoom(Long roomId, Room room) {
        rooms.put(roomId, room);
    }

    public void putUserRoom(Long userId, Long roomId) {
        userRooms.put(userId, roomId);
    }

    public void removeUser(Long userId) {
        userRooms.remove(userId);
    }

    public void removeRoom(Long roomId) {
        rooms.remove(roomId);
    }

    public int roomNumber() {
        return rooms.size();
    }

    public int playerNumber() {
        return userRooms.size();
    }

    public List<Date> lastUpdates() {
        return rooms.values().stream().map(room -> room.getGame().getLastUpdate())
                .collect(Collectors.toList());
    }

}

package co.mafiagame.bot;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class GameContainer {
    private final Map<Long, Long> userRooms = new HashMap<>(); //userId -> roomId
    private final Map<Long, Room> rooms = new HashMap<>(); //roomId-> Room

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

}

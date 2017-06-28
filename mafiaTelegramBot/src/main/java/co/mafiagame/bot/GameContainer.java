package co.mafiagame.bot;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class GameContainer {
    private final Map<Integer, Integer> userRooms = new HashMap<>(); //userId -> roomId
    private final Map<Integer, Room> rooms = new HashMap<>(); //roomId-> Room

    public Integer roomOfUser(Integer userId) {
        return userRooms.get(userId);
    }

    public Room room(Integer roomId) {
        return rooms.get(roomId);
    }

    public void putRoom(Integer roomId, Room room) {
        rooms.put(roomId, room);
    }

    public void putUserRoom(Integer userId, Integer roomId) {
        userRooms.put(userId, roomId);
    }

    public void removeUser(Integer userId) {
        userRooms.remove(userId);
    }

    public void removeRoom(Integer roomId) {
        rooms.remove(roomId);
    }

}

package co.mafiagame.bot;

import co.mafiagame.bot.persistence.domain.Action;
import co.mafiagame.bot.persistence.domain.Audit;
import co.mafiagame.bot.persistence.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class GameContainer {
	private final Map<Long, Long> userRooms = new HashMap<>(); //userId -> roomId
	private final Map<Long, Room> rooms = new HashMap<>(); //roomId-> Room
	@Autowired
	private AuditRepository auditRepository;

	@PostConstruct
	private void init() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, 1);
		now.set(Calendar.HOUR_OF_DAY, 12);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				cleanUp();
			}
		}, now.getTime(), 24L * 60 * 60 * 1000);
	}

	private void cleanUp() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		rooms.values().forEach(room -> {
			if (room.getGame().getLastUpdate().before(cal.getTime())) {
				room.getAccounts().forEach(a -> {
					auditRepository.save(new Audit()
									.setAction(Action.GAME_TIMEOUT)
									.setActor(a)
									.setDate(new Date())
									.setRoomId(String.valueOf(room.getRoomId())));
					this.removeUser(a.getTelegramUserId());
				});
				removeRoom(Long.valueOf(room.getGame().getGameId()));
			}
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

}

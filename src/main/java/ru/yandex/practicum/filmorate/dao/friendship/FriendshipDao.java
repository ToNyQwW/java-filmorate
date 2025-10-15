package ru.yandex.practicum.filmorate.dao.friendship;

import java.util.List;
import java.util.Map;

public interface FriendshipDao {

    void addFriend(Long userId, Long friendId);

    List<Long> getFriends(Long userId);

    Map<Long, List<Long>> getFriendsByUserIds(List<Long> userIds);

    List<Long> getCommonFriends(Long id, Long otherId);

    void removeFriend(Long userId, Long friendId);
}

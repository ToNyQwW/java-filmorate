package ru.yandex.practicum.filmorate.dao.friendship;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FriendshipDao {

    void addFriend(Long userId, Long friendId);

    Set<Long> getFriends(Long userId);

    Map<Long, Set<Long>> getFriendsByUserIds(List<Long> userIds);

    List<Long> getCommonFriends(Long id, Long otherId);

    void removeFriend(Long userId, Long friendId);
}

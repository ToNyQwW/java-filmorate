package ru.yandex.practicum.filmorate.dao.friendship;

import ru.yandex.practicum.filmorate.entity.Friendship;

import java.util.List;
import java.util.Map;

public interface FriendshipDao {

    void addFriendship(Long userId, Long friendId);

    Friendship getFriends(Long userId);

    Map<Long, Friendship> getFriendsByUserIds(List<Long> userIds);

    List<Long> getCommonFriends(Long id, Long otherId);

    void removeFriendship(Long userId, Long friendId);
}

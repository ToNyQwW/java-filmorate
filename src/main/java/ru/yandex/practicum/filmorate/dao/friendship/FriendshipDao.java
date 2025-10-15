package ru.yandex.practicum.filmorate.dao.friendship;

import ru.yandex.practicum.filmorate.entity.Friendship;

import java.util.List;
import java.util.Map;

public interface FriendshipDao {

    void addFriend(Long userId, Long friendId);

    Friendship getFriends(Long userId);

    Map<Long, Friendship> getFriendsByUserIds(List<Long> userIds);

    List<Long> getCommonFriends(Long id, Long otherId);

    void removeFriend(Long userId, Long friendId);
}

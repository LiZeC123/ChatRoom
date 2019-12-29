package top.lizec.server.dao;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import top.lizec.core.annotation.Component;
import top.lizec.server.entity.Friend;

@Component
public class FriendDao extends BaseDao<Friend> {

    List<String> findFriendByName(String name) {
        return mList.stream().map(friend -> {
            if (friend.getUser1().equals(name)) {
                return friend.getUser2();
            } else if (friend.getUser2().equals(name)) {
                return friend.getUser1();
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }


}
package top.lizec.server.dao;

import java.util.List;
import java.util.stream.Collectors;

import top.lizec.core.annotation.Component;
import top.lizec.server.entity.MessageR;

@Component
public class MessageDao extends BaseDao<MessageR> {
    public MessageR getRecentMessageOf(String user1, String user2) {
        for (int i = mList.size() - 1; i >= 0; i--) {
            MessageR messageR = mList.get(i);
            if ((messageR.getUsername().equals(user1) && messageR.getReceiver().equals(user2)) ||
                    (messageR.getUsername().equals(user2) && messageR.getReceiver().equals(user1))) {
                return messageR;
            }
        }
        return null;
    }

    public List<MessageR> getAllMessageOf(String user1, String user2) {
        return mList.stream().filter(messageR -> (messageR.getUsername().equals(user1) && messageR.getReceiver().equals(user2)) ||
                (messageR.getUsername().equals(user2) && messageR.getReceiver().equals(user1)))
                .collect(Collectors.toList());
    }
}

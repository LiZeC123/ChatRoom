package top.lizec.server.biz;

import java.util.List;
import java.util.stream.Collectors;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.Message;
import top.lizec.core.exception.MessageNotSendException;
import top.lizec.core.exception.UserLogoutException;
import top.lizec.server.dao.MessageDao;
import top.lizec.server.dao.TokenDao;
import top.lizec.server.entity.MessageR;
import top.lizec.server.push.MessagePush;

@Component
public class MessageBiz {
    @Automatique
    private TokenDao tokenDao;
    @Automatique
    private MessageDao messageDao;
    @Automatique
    private MessagePush messagePush;

    public boolean forward(Message message) {
        message.setToken(null); // token不能转发

        // 1. 尝试给自己发送消息, 如果不能成功则抛弃这条消息（发送端已经下线）
        try {
            messagePush.pushTo(message, message.getUsername());
            messageDao.insert(new MessageR(message));
        } catch (UserLogoutException | MessageNotSendException e) {
            return false;
        }

        // 2. 尝试给接受者发送消息
        try {
            messagePush.pushTo(message, message.getReceiver());
        } catch (UserLogoutException | MessageNotSendException e) {
            // 如果不能发送, 则保存消息等以后发送
            return false;
        }

        return true;
    }

    private Message toMessage(MessageR messageR) {
        return new Message(messageR.getUsername(), null, messageR.getContent(), messageR.getReceiver());
    }


    public List<Message> getMessageFor(Message message) {
        if (tokenDao.checkUserToken(message.getUsername(), message.getToken())) {
            List<MessageR> messageRs = messageDao.getAllMessageOf(message.getUsername(), message.getReceiver());
            return messageRs.stream().map(this::toMessage).collect(Collectors.toList());
        }

        System.err.println("Token 验证失败, 无法请求此接口");
        return null;
    }
}

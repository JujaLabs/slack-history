package juja.slack.hitory.dao.api;

/**
 * User: viktor
 * Date: 1/14/16
 */
public interface MessageDao {

    void saveMessageWithChannel(String channelId, String channelName, String messageContent);
}

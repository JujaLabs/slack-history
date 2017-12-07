package juja.slack.hitory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import juja.slack.hitory.dao.api.MessageDao;
import juja.slack.hitory.di.HistoryModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;


/**
 * User: viktor
 * Date: 1/14/16
 */
public class HistoryCollector {

    SlackSession slackSession;

    @Inject
    MessageDao messageDao;

    @Inject
    @Named("admin.token")
    String token;

    @Inject
    public HistoryCollector(SlackSession slackSession) throws IOException {
        this.slackSession = slackSession;
        slackSession.addMessagePostedListener((event, session) -> {
            if (isPublicChannel(event.getChannel())) {
                archiveMessage(event.getChannel(), event);
            }
        });
        slackSession.connect();
    }

    private void archiveMessage(SlackChannel channel, SlackMessagePosted event) {
        String messageContent = event.getJsonSource().toJSONString();
        messageDao.saveMessageWithChannel(channel.getId(), channel.getName(), messageContent);
        System.out.println(messageContent);
    }

    private boolean isPublicChannel(SlackChannel channel) {
        return !channel.isDirect();
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            System.setProperty("property.location", args[0]);
        }

        Injector injector = Guice.createInjector(new HistoryModule());
        HistoryCollector historyCollector = injector.getInstance(HistoryCollector.class);
    }
}

package juja.slack.hitory.di;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import juja.slack.hitory.dao.api.MessageDao;
import juja.slack.hitory.dao.file.FileMessageDao;
import juja.slack.hitory.utils.PropertiesHolder;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * User: viktor
 * Date: 1/12/16
 */
public class HistoryModule implements Module {

    @Override
    public void configure(Binder binder) {
        Names.bindProperties(binder, PropertiesHolder.getProperties());
    }

    @Singleton
    @Provides
    protected SlackSession provideSlackSession(@Named("admin.token") String token) {
        SlackSession slackSession = SlackSessionFactory.createWebSocketSlackSession(token);
        return slackSession;
    }

    @Singleton
    @Provides
    protected MessageDao provideMessageDao(@Named("storage.root") String storageRoot) throws IOException {
        File storageRootDir = new File(storageRoot);
        return new FileMessageDao(storageRootDir);
    }

}

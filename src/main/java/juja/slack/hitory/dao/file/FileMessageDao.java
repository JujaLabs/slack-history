package juja.slack.hitory.dao.file;

import juja.slack.hitory.dao.api.MessageDao;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.Files.*;
import static java.nio.file.Files.exists;

/**
 * User: viktor
 * Date: 1/14/16
 */
public class FileMessageDao implements MessageDao {

    private File storageRoot;

    @Inject
    public FileMessageDao(File storageRoot) throws IOException {
        this.storageRoot = storageRoot;
        Path storageRootPath = storageRoot.toPath();
        if (!exists(storageRootPath)) {
            createDirectories(storageRootPath);
        }
    }

    @Override
    public void saveMessageWithChannel(String channelId, String channelName, String messageContent) {
        Path storagePath = createFileIfNeeded(storageRoot, channelId, channelName);
        writeToFile(messageContent, storagePath);
    }

    private void writeToFile(final String content, Path storagePath) {
        try {
            write(storagePath, content.getBytes("UTF-8"), StandardOpenOption.APPEND);
            write(storagePath, "\n".getBytes("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            // TODO create custom exceptions
            throw new RuntimeException(e);
        }
    }

    private Path createFileIfNeeded(File storageRoot, String channelId, String channelName) {
        try {
            String channelHistoryFileName = channelName + "_" + channelId + ".txt";
            File channelHistoryFile = new File(storageRoot, channelHistoryFileName);

            Path channelPath = channelHistoryFile.toPath();
            if (!exists(channelPath)) {
                createFile(channelPath);
            }
            return channelPath;
        } catch (IOException e) {
            // TODO create custom exceptions
            throw new RuntimeException(e);
        }
    }
}

package juja.slack.hitory.dao.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: viktor
 * Date: 1/14/16
 */
public class FileMessageDaoTest {

    @Rule
    public TemporaryFolder root = new TemporaryFolder();

    @Test
    public void createFileAndSaveMessage() throws Exception {
        //Given
        File storageFolderRoot = root.newFolder();
        FileMessageDao fileMessageDao = new FileMessageDao(storageFolderRoot);

        //When
        fileMessageDao.saveMessageWithChannel("channelId", "channelName", "message");

        //Then
        Path actualPath = new File(storageFolderRoot, "channelName_channelId.txt").toPath();
        assertThat(exists(actualPath), is(true));
        assertThat(readAllLines(actualPath).get(0), is("message"));
    }

    @Test
    public void createChannelRootFolderIfNotExists() throws Exception {
        //Given
        File storageFolderRoot = new File(root.getRoot(), "tempFolder");

        //When
        new FileMessageDao(storageFolderRoot);

        //Then
        assertThat(exists(storageFolderRoot.toPath()), is(true));
    }

    @Test
    public void appendToTheEndOfTheFileNewMessage() throws Exception {
        //Given
        File storageFolderRoot = root.newFolder();
        Path channelPath = new File(storageFolderRoot, "channelName_channelId.txt").toPath();
        Files.createFile(channelPath);
        Files.write(channelPath, "filrstLine\n".getBytes());

        FileMessageDao fileMessageDao = new FileMessageDao(storageFolderRoot);

        //When
        fileMessageDao.saveMessageWithChannel("channelId", "channelName", "message");

        //Then
        Path actualPath = new File(storageFolderRoot, "channelName_channelId.txt").toPath();
        assertThat(exists(actualPath), is(true));
        assertThat(readAllLines(actualPath).get(0), is("filrstLine"));
        assertThat(readAllLines(actualPath).get(1), is("message"));
    }

    @Test
    public void appendTwoMessages() throws Exception {
        //Given
        File storageFolderRoot = root.newFolder();
        System.out.println(storageFolderRoot.getAbsolutePath());
        FileMessageDao fileMessageDao = new FileMessageDao(storageFolderRoot);

        //When
        fileMessageDao.saveMessageWithChannel("channelId", "channelName", "message");
        fileMessageDao.saveMessageWithChannel("channelId", "channelName", "message2");

        //Then
        Path actualPath = new File(storageFolderRoot, "channelName_channelId.txt").toPath();
        assertThat(readAllLines(actualPath).get(0), is("message"));
        assertThat(readAllLines(actualPath).get(1), is("message2"));
    }
}
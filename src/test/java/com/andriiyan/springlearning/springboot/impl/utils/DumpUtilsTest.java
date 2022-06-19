package com.andriiyan.springlearning.springboot.impl.utils;

import com.andriiyan.springlearning.springboot.api.dao.EventDao;
import com.andriiyan.springlearning.springboot.api.dao.TicketDao;
import com.andriiyan.springlearning.springboot.api.dao.UserAccountDao;
import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.api.facade.BookingFacade;
import com.andriiyan.springlearning.springboot.api.model.Ticket;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import com.andriiyan.springlearning.springboot.impl.model.TicketEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserAccountEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import com.andriiyan.springlearning.springboot.impl.utils.file.Serializer;
import com.andriiyan.springlearning.springboot.impl.utils.file.serializer.ByteSerializer;
import com.andriiyan.springlearning.springboot.impl.utils.file.serializer.JsonSerializer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
public class DumpUtilsTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private UserAccountDao userAccountDao;

    @Rule
    public final TemporaryFolder temporaryFolder = TemporaryFolder.builder().assureDeletion().build();

    private void testRead(Serializer serializer) throws IOException {
        File eventFile = temporaryFolder.newFile("eventFilePath.json");
        File userFile = temporaryFolder.newFile("userFilePath.json");
        File userAccountFile = temporaryFolder.newFile("userAccountFilePath.json");
        File ticketFile = temporaryFolder.newFile("ticketFilePath.json");
        // saving some data into files

        UserEntity userEntity = new UserEntity(1, "Test #1", "Email #1", "pass", "ADMIN");
        EventEntity eventEntity = new EventEntity(1, "Event #2", new Date(), 150);
        UserAccountEntity userAccountEntity = new UserAccountEntity(1, userEntity.getId(), 500);
        TicketEntity ticketEntity = new TicketEntity(1, eventEntity.getId(), userEntity.getId(), Ticket.Category.PREMIUM, 2);

        FileOutputStream userFileOutputStream = new FileOutputStream(userFile);
        serializer.serialize(Collections.singleton(userEntity), userFileOutputStream);
        userFileOutputStream.close();

        FileOutputStream userAccountFileOutputStream = new FileOutputStream(userAccountFile);
        serializer.serialize(Collections.singleton(userAccountEntity), userAccountFileOutputStream);
        userAccountFileOutputStream.close();

        FileOutputStream eventFileOutputStream = new FileOutputStream(eventFile);
        serializer.serialize(Collections.singleton(eventEntity), eventFileOutputStream);
        eventFileOutputStream.close();

        FileOutputStream ticketFileOutputStream = new FileOutputStream(ticketFile);
        serializer.serialize(Collections.singleton(ticketEntity), ticketFileOutputStream);
        ticketFileOutputStream.close();

        // reading saved data
        final DumpUtils dumpUtils = new DumpUtils(
                eventFile.getAbsolutePath(),
                userFile.getAbsolutePath(),
                userAccountFile.getAbsolutePath(),
                ticketFile.getAbsolutePath(),
                serializer,
                true,
                true,
                1,
                userDao,
                eventDao,
                userAccountDao,
                ticketDao
        );
        dumpUtils.init();

        // verify data is same
        EventEntity event = eventDao.findById(1L).get();
        Assert.assertEquals(eventEntity.getTicketPrice(), event.getTicketPrice(), 0.01);
        Assert.assertEquals(eventEntity.getTitle(), event.getTitle());
        Assert.assertEquals(userEntity, userDao.findById(1L).get());
        Assert.assertEquals(ticketEntity, ticketDao.findById(1L).get());
    }

    private void testStore(Serializer serializer) throws IOException {
        File eventFile = temporaryFolder.newFile("eventFilePath.json");
        File userFile = temporaryFolder.newFile("userFilePath.json");
        File userAccountFile = temporaryFolder.newFile("userAccountFilePath.json");
        File ticketFile = temporaryFolder.newFile("ticketFilePath.json");
        // saving some data info files

        // reading saved data
        final DumpUtils dumpUtils = new DumpUtils(
                eventFile.getAbsolutePath(),
                userFile.getAbsolutePath(),
                userAccountFile.getAbsolutePath(),
                ticketFile.getAbsolutePath(),
                serializer,
                false,
                true,
                1,
                userDao,
                eventDao,
                userAccountDao,
                ticketDao
        );
        dumpUtils.setItemCount(1);
        dumpUtils.init();

        // verify data is present

        FileInputStream userFileOutputStream = new FileInputStream(userFile);
        Assert.assertEquals(1, serializer.deserialize(userFileOutputStream, UserEntity.class).size());
        userFileOutputStream.close();

        FileInputStream userAccountFileOutputStream = new FileInputStream(userAccountFile);
        Assert.assertEquals(1, serializer.deserialize(userAccountFileOutputStream, UserAccountEntity.class).size());
        userAccountFileOutputStream.close();

        FileInputStream eventFileOutputStream = new FileInputStream(eventFile);
        Assert.assertEquals(1, serializer.deserialize(eventFileOutputStream, EventEntity.class).size());
        eventFileOutputStream.close();

        FileInputStream ticketFileOutputStream = new FileInputStream(ticketFile);
        Assert.assertEquals(1, serializer.deserialize(ticketFileOutputStream, TicketEntity.class).size());
        ticketFileOutputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        testRead(new JsonSerializer());
        clean();
        testRead(new ByteSerializer());
    }

    private void clean() throws IOException {
        ticketDao.deleteAll();
        userDao.deleteAll();
        userAccountDao.deleteAll();
        eventDao.deleteAll();
        temporaryFolder.delete();
        temporaryFolder.create();
    }

    @Test
    public void testStore() throws IOException {
        testStore(new JsonSerializer());
        clean();
        testStore(new ByteSerializer());
    }

}

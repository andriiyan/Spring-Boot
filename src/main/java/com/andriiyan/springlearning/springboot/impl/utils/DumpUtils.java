package com.andriiyan.springlearning.springboot.impl.utils;

import com.andriiyan.springlearning.springboot.api.dao.EventDao;
import com.andriiyan.springlearning.springboot.api.dao.TicketDao;
import com.andriiyan.springlearning.springboot.api.dao.UserAccountDao;
import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import com.andriiyan.springlearning.springboot.impl.model.TicketEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserAccountEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import com.andriiyan.springlearning.springboot.impl.utils.file.FileUtils;
import com.andriiyan.springlearning.springboot.impl.utils.file.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for dumping objects into the file.
 */
@Component
@PropertySource("classpath:application-dump.properties")
class DumpUtils {

    private static final Logger logger = LoggerFactory.getLogger(DumpUtils.class);

    private final String eventFilePath;
    private final String userFilePath;
    private final String userAccountFilePath;
    private final String ticketFilePath;
    @NonNull
    private final Serializer serializer;
    private boolean read;
    private boolean enabled;

    // dump optional fields
    private int itemCount;

    // store optional fields
    @Nullable
    private UserDao userDao;
    @Nullable
    private EventDao eventDao;
    @Nullable
    private UserAccountDao userAccountDao;
    @Nullable
    private TicketDao ticketDao;

    /**
     * Creates an instance configured to read data from the dump and populate the database.
     *
     * @param eventFilePath       specifies path to the file where to read events.
     * @param userFilePath        specifies path to the file where to read users.
     * @param userAccountFilePath specifies path to the file where to read user's accounts data.
     * @param ticketFilePath      specifies path to the file where to read tickets.
     * @param serializer          specifies how to serialize/deserialize objects.
     *
     * @see EventDao
     * @see UserDao
     * @see UserAccountDao
     * @see TicketDao
     * @see EventEntity
     * @see UserEntity
     * @see UserAccountEntity
     * @see TicketEntity
     * @see Serializer
     */
    DumpUtils(@NonNull String eventFilePath,
              @NonNull String userFilePath,
              @NonNull String userAccountFilePath,
              @NonNull String ticketFilePath,
              @NonNull Serializer serializer,
              @Nullable UserDao userDao,
              @Nullable EventDao eventDao,
              @Nullable UserAccountDao userAccountDao,
              @Nullable TicketDao ticketDao
    ) {
        this(eventFilePath, userFilePath, userAccountFilePath, ticketFilePath, serializer, true, true, 0, userDao, eventDao, userAccountDao, ticketDao);
    }

    /**
     * Creates an instance configured to generate and write data to the dump.
     *
     * @param eventFilePath       specifies path to the file where to write events.
     * @param userFilePath        specifies path to the file where to write users.
     * @param userAccountFilePath specifies path to the file where to write user's accounts data.
     * @param ticketFilePath      specifies path to the file where to write tickets.
     * @param serializer          specifies how to serialize/deserialize objects.
     * @param itemCount           defines number of item that would be generated in case object is configured to dump data.
     *
     * @see EventDao
     * @see UserDao
     * @see UserAccountDao
     * @see TicketDao
     * @see EventEntity
     * @see UserEntity
     * @see UserAccountEntity
     * @see TicketEntity
     * @see Serializer
     */
    DumpUtils(@NonNull String eventFilePath,
              @NonNull String userFilePath,
              @NonNull String userAccountFilePath,
              @NonNull String ticketFilePath,
              @NonNull Serializer serializer,
              int itemCount
    ) {
        this(eventFilePath, userFilePath, userAccountFilePath, ticketFilePath, serializer, true, true, itemCount, null, null, null, null);
    }


    /**
     * @param eventFilePath       specifies path to the file where to write/read events.
     * @param userFilePath        specifies path to the file where to write/read users.
     * @param userAccountFilePath specifies path to the file where to write/read user's accounts data.
     * @param ticketFilePath      specifies path to the file where to write/read tickets.
     * @param serializer          specifies how to serialize/deserialize objects.
     * @param read                defines if dump should read and populate data or just create a set of data and dump them.
     * @param enabled             defines whether dump is enabled.
     * @param itemCount           defines number of item that would be generated in case object is configured to dump data.
     * @param userDao             user database access object, used to store entities in the database.
     * @param eventDao            event database access object, used to store entities in the database.
     * @param userAccountDao      user's accounts data database access object, used to store entities in the database.
     * @param ticketDao           ticket database access object, used to store entities in the database.
     *
     * @see EventDao
     * @see UserDao
     * @see UserAccountDao
     * @see TicketDao
     * @see EventEntity
     * @see UserEntity
     * @see UserAccountEntity
     * @see TicketEntity
     * @see Serializer
     */
    @Autowired
    DumpUtils(
            @Value("${initial-date-population.event_file_path}") String eventFilePath,
            @Value("${initial-date-population.user_file_path}") String userFilePath,
            @Value("${initial-date-population.user_account_file_path}") String userAccountFilePath,
            @Value("${initial-date-population.ticket_file_path}") String ticketFilePath,
            @NonNull @Autowired Serializer serializer,
            @Value("${initial-date-population.read}") boolean read,
            @Value("${initial-date-population.enabled}") boolean enabled,
            @Value("${initial-date-population.item_count}") int itemCount,
            @Nullable @Autowired(required = false) UserDao userDao,
            @Nullable @Autowired(required = false) EventDao eventDao,
            @Nullable @Autowired(required = false) UserAccountDao userAccountDao,
            @Nullable @Autowired(required = false) TicketDao ticketDao
    ) {
        this.eventFilePath = eventFilePath;
        this.userFilePath = userFilePath;
        this.userAccountFilePath = userAccountFilePath;
        this.ticketFilePath = ticketFilePath;
        this.serializer = serializer;
        this.read = read;
        this.enabled = enabled;
        this.itemCount = itemCount;
        this.userDao = userDao;
        this.eventDao = eventDao;
        this.userAccountDao = userAccountDao;
        this.ticketDao = ticketDao;
    }

    /**
     * Method that invokes after bean has been constructed.
     *
     * If it's enabled It generates and dumps some objects into the files or reads dumps from the files and populates the database with
     * read objects.
     * If it's disabled it does nothing.
     */
    @PostConstruct
    void init() {
        if (enabled) {
            if (read) {
                readAndPopulateDatabase();
            } else {
                generateAndDump();
            }
        }
    }

    /**
     * Generates models and writes them into files.
     *
     * @return DumpResult with all created models.
     */
    private DumpResult generateAndDump() {
        try {
            final Collection<UserEntity> users = generateAndWriteUsers();
            logger.info("Users {} were dumped into the {}", users, userFilePath);
            final Collection<EventEntity> events = generateAndWriteEvents();
            logger.info("Events {} were dumped into the {}", events, eventFilePath);
            final Collection<TicketEntity> tickets = generateAndWriteTickets();
            logger.info("Tickets {} were dumped into the {}", tickets, ticketFilePath);
            final Collection<UserAccountEntity> userAccountEntities = generateAndWriteUserAccounts();
            logger.info("UserAccounts {} were dumped into the {}", userAccountEntities, userAccountFilePath);
            return new DumpResult(events, users, tickets, userAccountEntities);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new DumpResult();
    }

    /**
     * Utility method to check nullability and print warning message.
     *
     * @param target entity to check.
     * @param entityName entity's log name
     * @return true if target is null, otherwise - false.
     */
    private boolean checkIsNullAndWarn(@Nullable Object target, @NonNull String entityName) {
        if (target == null) {
            logger.warn("DumpUtils configured to read data and populate the database, but {} was not provided.", entityName);
            return true;
        }
        return false;
    }

    /**
     * Reads some data from the files and populates the database with read entities.
     *
     * Make sure that all DAOs are set and properly configured.
     */
    private void readAndPopulateDatabase() {
        if (checkIsNullAndWarn(userDao, "userDao")
                || checkIsNullAndWarn(userAccountDao, "userAccountDao")
                || checkIsNullAndWarn(eventDao, "eventDao")
                || checkIsNullAndWarn(ticketDao, "ticketDao")) {
            return;
        }
        try {
            Iterable<UserEntity> userEntities = FileUtils.readFromFile(serializer, new File(userFilePath), UserEntity.class);
            Iterable<UserAccountEntity> userAccountEntities = FileUtils.readFromFile(serializer, new File(userAccountFilePath), UserAccountEntity.class);
            Iterable<EventEntity> eventEntities = FileUtils.readFromFile(serializer, new File(eventFilePath), EventEntity.class);
            Iterable<TicketEntity> ticketEntities = FileUtils.readFromFile(serializer, new File(ticketFilePath), TicketEntity.class);
            userEntities = userDao.saveAll(userEntities);
            userAccountEntities = userAccountDao.saveAll(userAccountEntities);
            eventEntities = eventDao.saveAll(eventEntities);
            ticketEntities = ticketDao.saveAll(ticketEntities);

            logger.info("Data successfully populated, with \n\tusers: {},\n\n\tuserAccounts: {},\n\n\tevents: {}, \n\n\ttickets: {}", userEntities, userAccountEntities, eventEntities, ticketEntities);
        } catch (IOException e) {
            logger.error("Exception while reading the dumped data", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates abd writes event models into the specified file.
     *
     * @throws IOException in case any IO exception during the operation.
     */
    private Collection<EventEntity> generateAndWriteEvents() throws IOException {
        Collection<EventEntity> events = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            events.add(new EventEntity("Test #" + i, new Date(), i + 20));
        }
        FileUtils.writeIntoFile(serializer, eventFilePath, events);
        return events;
    }

    /**
     * Generates abd writes ticket models into the specified file.
     *
     * @throws IOException in case any IO exception during the operation.
     */
    private Collection<TicketEntity> generateAndWriteTickets() throws IOException {
        Collection<TicketEntity> tickets = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            tickets.add(new TicketEntity(i, i + 1, i + 1, TicketEntity.Category.PREMIUM, i));
        }
        FileUtils.writeIntoFile(serializer, ticketFilePath, tickets);
        return tickets;
    }

    /**
     * Generates abd writes user models into the specified file.
     *
     * @throws IOException in case any IO exception during the operation.
     */
    private Collection<UserEntity> generateAndWriteUsers() throws IOException {
        Collection<UserEntity> events = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            events.add(new UserEntity(i, "Test #" + i, "email #" + i));
        }
        FileUtils.writeIntoFile(serializer, userFilePath, events);
        return events;
    }

    /**
     * Generates abd writes userAccount models into the specified file.
     *
     * @throws IOException in case any IO exception during the operation.
     */
    private Collection<UserAccountEntity> generateAndWriteUserAccounts() throws IOException {
        Collection<UserAccountEntity> events = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            events.add(new UserAccountEntity(i, i * 10));
        }
        FileUtils.writeIntoFile(serializer, userAccountFilePath, events);
        return events;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setEventDao(@Nullable EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setTicketDao(@Nullable TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public void setUserAccountDao(@Nullable UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    public void setUserDao(@Nullable UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Represents a result of the generated and stored into the file values.
     */
    public static class DumpResult {
        final List<EventEntity> dumpedEvents;
        final List<UserEntity> dumpedUsers;
        final List<TicketEntity> dumpedTickets;
        final List<UserAccountEntity> dumpedUserAccounts;

        public DumpResult() {
            this.dumpedEvents = Collections.emptyList();
            this.dumpedUsers = Collections.emptyList();
            this.dumpedTickets = Collections.emptyList();
            this.dumpedUserAccounts = Collections.emptyList();
        }

        public DumpResult(
                Collection<EventEntity> dumpedEvents,
                Collection<UserEntity> dumpedUsers,
                Collection<TicketEntity> dumpedTickets,
                Collection<UserAccountEntity> userAccountEntities
        ) {
            this.dumpedEvents = new ArrayList<>(dumpedEvents);
            this.dumpedUsers = new ArrayList<>(dumpedUsers);
            this.dumpedTickets = new ArrayList<>(dumpedTickets);
            this.dumpedUserAccounts = new ArrayList<>(userAccountEntities);
        }

        public List<EventEntity> getDumpedEvents() {
            return dumpedEvents;
        }

        public List<TicketEntity> getDumpedTickets() {
            return dumpedTickets;
        }

        public List<UserEntity> getDumpedUsers() {
            return dumpedUsers;
        }
    }
}

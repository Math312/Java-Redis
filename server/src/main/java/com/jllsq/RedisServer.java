package com.jllsq;

import com.jllsq.common.channel.RedisConnChannel;
import com.jllsq.common.entity.*;
import com.jllsq.common.basic.map.Dict;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.handler.RedisServerHandler;
import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandEnum;
import com.jllsq.codec.RedisObjectDecoder;
import com.jllsq.codec.RedisObjectEncoder;
import com.jllsq.holder.RedisServerDbHolder;
import com.jllsq.holder.RedisServerEventLoopHolder;
import com.jllsq.holder.RedisServerStateHolder;
import com.jllsq.log.RedisAofLog;
import com.jllsq.log.RedisLog;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.jllsq.common.basic.list.List;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.jllsq.log.RedisLog.*;

/**
 * @author Math312
 * */
@Data
public class RedisServer {

    private static String TIME_OUT = "timeout";
    private static String PORT = "port";
    private static String BIND = "bind";
    private static String SAVE = "save";
    private static String LOG_LEVEL = "loglevel";
    private static String LOG_FILE = "logfile";
    private static String DATABASES = "databases";
    private static String MAX_CLIENTS = "maxclients";
    private static String MAX_MEMORY = "maxmemory";
    private static String GLUE_OUTPUT_BUF = "glueoutputbuf";
    private static String SHARE_OBJECTS = "shareobjects";
    private static String RDB_COMPRESSION = "rdbcompression";
    private static String SHARE_OBJECTS_POOL_SIZE = "shareobjectspoolsize";
    private static String DAEMONIZE = "daemonize";
    private static String APPEND_ONLY = "appendonly";
    private static String APPEND_FSYNC = "appendfsync";
    private static String REQUIRE_PASS = "requirepass";
    private static String PID_FILE = "pidfile";
    private static String DB_FILE_NAME = "dbfilename";

    private static String STDOUT = "stdout";

    private static int REDIS_DEFAULT_DBNUM = 16;
    private static int REDIS_SERVERPORT = 6379;
    private static int REDIS_VERBOSE = 1;
    private static int REDIS_MAXIDLETIME = 5 * 60;

    private static int APPENDFSYNC_NO = 0;
    private static int APPENDFSYNC_ALWAYS = 1;
    private static int APPENDFSYNC_EVERYSEC = 2;

    private static int REDIS_EXPIRELOOKUPS_PER_CRON = 100;
    private String configFileName;

    private int cronloops;
    private Date unixTime;

    private int port;
    private Dict<SDS, Object> sharingPool;
    private List clients;
    private Date lastSave;
    private Date stateStartTime;
    private long stateNumCommands;
    private long stateNumConnections;
    private int verbosity;
    private int glueOutputBuf;
    private int maxIdleTime;
    private int daemonize;
    private int appendOnly;
    private int appendFsync;
    private Date lastFsync;
    private SDS pidFile;
    private SDS bgWriteBuf;
    private SaveParam[] saveParams;
    private int saveParamLen;
    private SDS logFile;
    private SDS bindAddr;
    private SDS dbFilename;
    private SDS appendFileName;
    private SDS requirePass;
    private int shareObjects;
    private int rdbCompression;
    private FileChannel devnull;

    private ServerBootstrap b;
    private HashMap<SDS, RedisCommand> redisCommandTable;

    private long cronLoops;
    private int maxClients;
    private long maxMemory;
    private Shared shared;

    public RedisServer(String configFileName) {
        this.configFileName = configFileName;
    }

    public RedisServer() {

    }

    private void start() throws Exception {
        initServerConfig();
        if (this.configFileName != null) {
            resetServerSaveParams();
            loadServerConfig(this.configFileName);
        }
        initServer();
        RedisServerEventLoopHolder holder = RedisServerEventLoopHolder.getInstance();
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        holder.setEventLoopGroup(group);
        try {
            ScheduledFuture<?> scheduledFuture = group.scheduleAtFixedRate(
                    () -> {
                        cronLoops++;
                        RedisDb[] db = RedisServerDbHolder.getInstance().getDb();
                        RedisServerStateHolder.getInstance().updateUnixTime();
                        for (int i = 0; i < db.length; i++) {
                            int size = db[i].getDict().getSize();
                            int used = db[i].getDict().getUsed();
                            int vkeys = db[i].getExpires().getUsed();

                            if (cronLoops % 5 == 0 && (used > 0 || vkeys > 0)) {
                                try {
                                    RedisLog.getInstance().log(RedisLog.LOG_LEVEL_VERBOSE, "DB %d: %d keys (%d volatile) in %d slots HT.", i, used, vkeys, size);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        for (int i = 0;i < db.length;i ++) {
                            int expired = 0;
                            Dict<RedisObject,RedisObject> expires = db[i].getExpires();
                            do {
                                int num = expires.getUsed();
                                long time = RedisServerStateHolder.getInstance().getUnixTime();

                                if (num > REDIS_EXPIRELOOKUPS_PER_CRON) {
                                    num = REDIS_EXPIRELOOKUPS_PER_CRON;
                                }
                                while (num -- > 0) {
                                    DictEntry<RedisObject,RedisObject> entry = expires.dictGetRandomKey();
                                    if (entry == null){
                                        break;
                                    } else {
                                        long expireTime = (long)(entry.getValue().getPtr());
                                        if (expireTime < time) {
                                            DictEntry<RedisObject,RedisObject> dataEntry = null;
                                            dataEntry = db[i].getDict().delete(entry.getKey());
                                            dataEntry.getKey().destructor();
                                            dataEntry.getValue().destructor();
                                            dataEntry = expires.delete(entry.getKey());
                                            dataEntry.getKey().destructor();
                                            dataEntry.getValue().destructor();
                                            expired ++;
                                        }
                                    }
                                }
                            } while (expired > REDIS_EXPIRELOOKUPS_PER_CRON / 4);

                        }

                    }, 0, 1, TimeUnit.SECONDS);
            holder.setScheduledFuture(scheduledFuture);
            this.b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .option(ChannelOption.SO_BACKLOG,300)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new RedisObjectDecoder(), new RedisObjectEncoder(), new RedisServerHandler(RedisServer.this));
                        }
                    });
            holder.setBootstrap(b);
            ChannelFuture f = b.bind().sync();
            holder.setChannelFuture(f);
            RedisLog.getInstance().log(RedisLog.LOG_LEVEL_VERBOSE,RedisServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private void initServer() {
        if (System.getProperty("os.name").equals("Linux")) {
            File file = new File("/dev/null");
            try {
                this.devnull = new FileOutputStream(file).getChannel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.cronLoops = 0;
        if (appendFileName != null) {
            try {
                if (RedisAofLog.getInstance().init(appendFileName.getContent())) {
                    java.util.List<RedisClient> list = RedisAofLog.getInstance().readClient();
                    for (RedisClient client:list) {
                        client.setDb(RedisServerDbHolder.getInstance().getSelectedDb());
                        RedisCommand command = RedisCommandEnum.getCommandByKey((SDS) (client.getArgv()[0].getPtr())).getCommand();
                        if (command != null) {
                            command.process(client);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void loadServerConfig(String configFileName) {
        Path path = Paths.get(configFileName);
        if (Files.isReadable(path)) {
            try {
                java.util.List<String> list = Files.readAllLines(path);
                for (String line : list) {
                    if (line.startsWith("#") || line.length() == 0) {
                        continue;
                    }
                    String[] config = line.split("\\s{1,}");
                    if (config[0].equals(TIME_OUT) && config.length == 2) {
                        this.maxIdleTime = Integer.parseInt(config[1]);
                    } else if (config[0].equals(PORT) && config.length == 2) {
                        this.port = Integer.parseInt(config[1]);
                        if (this.port < 1 || this.port > 65535) {
                            String errorLog = String.format("Attribute [ port ] must be between 1 and 65535!",config[0]);
                            RedisLog.getInstance().log(LOG_LEVEL_WARNING,errorLog);
                            System.exit(1);
                        }
                    } else if (config[0].equals(BIND) && config.length == 2) {
                        this.bindAddr = new SDS(config[1]);
                    } else if (config[0].equals(SAVE) && config.length == 3) {
                        long seconds = Long.parseLong(config[1]);
                        long changes = Long.parseLong(config[2]);
                        if (seconds < 1 || changes < 0) {
                            String errorLog = String.format("Attribute [ save ] config error");
                            RedisLog.getInstance().log(LOG_LEVEL_WARNING,errorLog);
                            System.exit(1);
                        }
                        appendServerSaveParams(seconds, changes);
                    } else if (config[0].equals(LOG_LEVEL) && config.length == 2) {
                        if (config[1].equals(LOG_LEVEL_DEBUG_STR)) {
                            this.verbosity = LOG_LEVEL_DEBUG;
                        } else if (config[1].equals(LOG_LEVEL_NOTICE_STR)) {
                            this.verbosity = LOG_LEVEL_NOTICE;
                        } else if (config[1].equals(LOG_LEVEL_WARNING_STR)) {
                            this.verbosity = LOG_LEVEL_WARNING;
                        } else if (config[1].equals(LOG_LEVEL_VERBOSE_STR)) {
                            this.verbosity = LOG_LEVEL_VERBOSE;
                        } else {
                            String errorLog = String.format("Attribute [ verbosity ] config error");
                            RedisLog.getInstance().log(LOG_LEVEL_WARNING,errorLog);
                            System.exit(1);
                        }
                    } else if (config[0].equals(LOG_FILE) && config.length == 2) {
                        if (config[1].equals(STDOUT)) {
                            this.logFile = null;
                        } else {
                            this.logFile = new SDS(config[1]);
                            if (!RedisLog.getInstance().init(logFile.getContent())) {
                                RedisLog.getInstance().log(LOG_LEVEL_WARNING,"Logfile [ "+ this.logFile.getContent() +" ] can't be open!");
                            }
                        }
                    } else if (config[0].equals(DATABASES) && config.length == 2) {
                        int dbNum = Integer.parseInt(config[1]);
                        if (dbNum < 1) {
                            String errorLog = String.format("Attribute [ dbNum ] > 0");
                            RedisLog.getInstance().log(LOG_LEVEL_WARNING,errorLog);
                            System.exit(1);
                        }
                        RedisServerDbHolder.getInstance().setDbNum(Integer.parseInt(config[1])); ;

                    } else if (config[0].equals(MAX_CLIENTS) && config.length == 2) {
                        this.maxClients = Integer.parseInt(config[1]);
                    } else if (config[0].equals(MAX_MEMORY) && config.length == 2) {
                        this.maxMemory = Long.parseLong(config[1]);
                    } else if (config[0].equals(GLUE_OUTPUT_BUF) && config.length == 2) {
                        if ("yes".equals(config[1])) {
                            this.glueOutputBuf = 1;
                        } else if (config[1].equals("no")) {
                            this.glueOutputBuf = 0;
                        }
                    } else if (config[0].equals(SHARE_OBJECTS) && config.length == 2) {
                        this.shareObjects = Integer.parseInt(config[1]);
                    } else if (config[0].equals(RDB_COMPRESSION) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.rdbCompression = 1;
                        } else if (config[1].equals("no")) {
                            this.rdbCompression = 0;
                        }
                    } else if (config[0].equals(SHARE_OBJECTS_POOL_SIZE) && config.length == 2) {

                    } else if (config[0].equals(DAEMONIZE) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.daemonize = 1;
                        } else if (config[1].equals("no")) {
                            this.daemonize = 0;
                        }
                    } else if (config[0].equals(APPEND_ONLY) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.appendOnly = 1;
                        } else if (config[1].equals("no")) {
                            this.appendOnly = 0;
                        }
                    } else if (config[0].equals(APPEND_FSYNC) && config.length == 2) {
                        if (config[1].equals("no")) {
                            this.appendFsync = APPENDFSYNC_NO;
                        } else if (config[1].equals("always")) {
                            this.appendFsync = APPENDFSYNC_ALWAYS;
                        } else if (config[1].equals("everysec")) {
                            this.appendFsync = APPENDFSYNC_EVERYSEC;
                        }
                    } else if (config[0].equals(REQUIRE_PASS) && config.length == 2) {
                        this.requirePass = new SDS(config[1]);
                    } else if (config[0].equals(PID_FILE) && config.length == 2) {
                        this.pidFile = new SDS(config[1]);
                    } else if (config[0].equals(DB_FILE_NAME) && config.length == 2) {
                        this.dbFilename = new SDS(config[1]);
                    } else {
                        String errorLog = String.format("Attribute [ %s ] isn't redis configuration!",config[0]);
                        RedisLog.getInstance().log(LOG_LEVEL_WARNING,errorLog);
                        System.exit(1);
                    }
                }
            } catch (IOException e) {
                String errorLog = String.format("config file can't open");
                try {
                    RedisLog.getInstance().log(LOG_LEVEL_WARNING,errorLog);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(1);
            }
        }
    }

    private void initServerConfig() {
        this.stateStartTime = new Date();
        this.port = REDIS_SERVERPORT;
        this.verbosity = REDIS_VERBOSE;
        this.maxIdleTime = REDIS_MAXIDLETIME;
        this.saveParams = null;
        this.logFile = null;
        this.bindAddr = null;
        this.glueOutputBuf = 1;
        this.daemonize = 0;
        this.appendOnly = 0;
        this.appendFsync = APPENDFSYNC_ALWAYS;
        this.lastFsync = new Date();
        this.pidFile = new SDS("/var/run/redis-java.pid");
        this.dbFilename = new SDS("dump.rdb");
        this.appendFileName = new SDS("appendonly.aof");
        this.requirePass = null;
        this.shareObjects = 0;
        this.rdbCompression = 1;
        this.maxClients = 0;
        this.maxMemory = 0;

        resetServerSaveParams();
        appendDefaultServerSaveParams();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            new RedisServer(args[0]).start();
        } else if (args.length == 0) {
            new RedisServer().start();
        } else {
            System.err.println("Usage: java -jar server-1.0-SNAPSHOT.jar [/path/to/redis.conf]\n ");
        }

    }

    private void resetServerSaveParams() {
        this.saveParams = new SaveParam[0];
        this.saveParamLen = 0;
    }

    private void appendDefaultServerSaveParams() {
        this.saveParams = new SaveParam[3];
        this.saveParams[0] = new SaveParam(60 * 60, 1);
        this.saveParams[1] = new SaveParam(300, 100);
        this.saveParams[2] = new SaveParam(60, 10000);
        this.saveParamLen = 3;
    }

    private void appendServerSaveParams(long seconds, long changes) {
        SaveParam newParam = new SaveParam(seconds, changes);
        SaveParam[] temp = new SaveParam[this.saveParamLen + 1];
        System.arraycopy(this.saveParams, 0, temp, 0, this.saveParamLen);
        temp[this.saveParamLen] = newParam;
        this.saveParams = temp;
        this.saveParamLen = this.saveParamLen + 1;
    }

}

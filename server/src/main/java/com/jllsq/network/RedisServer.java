package com.jllsq.network;

import com.jllsq.common.entity.*;
import com.jllsq.common.map.Dict;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.decoder.RedisCommandDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.jllsq.common.list.List;
import io.netty.util.HashedWheelTimer;
import lombok.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static com.jllsq.common.entity.RedisObject.REDIS_ENCODING_RAW;
import static com.jllsq.common.entity.RedisObject.REDIS_STRING;

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

    private static String LOG_LEVEL_DEBUG_STR = "debug";
    private static String LOG_LEVEL_VERBOSE_STR = "verbose";
    private static String LOG_LEVEL_NOTICE_STR = "notice";
    private static String LOG_LEVEL_WARNING_STR = "warning";

    private static int LOG_LEVEL_DEBUG = 0;
    private static int LOG_LEVEL_VERBOSE = 1;
    private static int LOG_LEVEL_NOTICE = 2;
    private static int LOG_LEVEL_WARNING = 3;

    private static String STDOUT = "stdout";

    private static int REDIS_DEFAULT_DBNUM = 16;
    private static int REDIS_SERVERPORT = 6379;
    private static int REDIS_VERBOSE = 1;
    private static int REDIS_MAXIDLETIME = 5 * 60;

    private static int APPENDFSYNC_NO = 0;
    private static int APPENDFSYNC_ALWAYS = 1;
    private static int APPENDFSYNC_EVERYSEC = 2;

    private String configFileName;

    private int port;
    private RedisDb[] db;
    private int dbNum;
    private Dict<SDS,Object> sharingPool;
    private long dirty;
    private List clients;
    private NioEventLoop nioEventLoop;
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
    private List objFreeList;
    private ServerBootstrap b;
    private HashMap<SDS, RedisCommand> redisCommandTable;

    private long cronLoops;
    private int maxClients;
    private long maxMemory;
    private Shared shared;

    public void start() throws Exception {
        initServerConfig();
        if (this.configFileName != null) {
            resetServerSaveParams();
            loadServerConfig(this.configFileName);
        }
        initServer();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            this.b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline()
                                    .addLast(new RedisCommandDecoder())
                                    .addLast(
                                    new RedisServerHandler(RedisServer.this));
                        }
                    });

            ChannelFuture f = b.bind().sync();            //8
            System.out.println(RedisServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();            //9
        } finally {
            group.shutdownGracefully().sync();            //10
        }
    }

    private void initServer() {
        File file = new File("/dev/null");
        try {
            this.devnull = new FileOutputStream(file).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createShareObjects();
        initCommand();
        this.db = new RedisDb[this.dbNum];
        for (int i = 0;i < this.dbNum;i ++) {
            this.db[i] = new RedisDb(i);
        }
        this.cronLoops = 0;
    }

    private RedisObject createObject(byte type, Object ptr) {
        RedisObject redisObject = null;
        if (this.objFreeList.length() > 0){
            // Get the first node.
            // Change the data of the node.
            // Remove the node from objFreeList.
        } else {
            redisObject = new RedisObject(type,ptr);
        }
        return redisObject;
    }

    private void createShareObjects() {
        this.shared = Shared.getInstance();
        this.shared.setCrlf(createObject(REDIS_STRING,new SDS("\r\n")));
        this.shared.setOk(createObject(REDIS_STRING,new SDS("+OK\r\n")));
        this.shared.setErr(createObject(REDIS_STRING,new SDS("-ERR\r\n")));
        this.shared.setEmptybulk(createObject(REDIS_STRING,new SDS("$0\r\n\r\n")));
        this.shared.setCzero(createObject(REDIS_STRING,new SDS(":0\r\n")));
        this.shared.setCone(createObject(REDIS_STRING,new SDS(":1\r\n")));
        this.shared.setNullbulk(createObject(REDIS_STRING,new SDS("$-1\r\n")));
        this.shared.setNullmultibulk(createObject(REDIS_STRING,new SDS("*-1\r\n")));
        this.shared.setEmptymultibulk(createObject(REDIS_STRING,new SDS("*0\r\n")));
        this.shared.setPong(createObject(REDIS_STRING,new SDS("+PONG\r\n")));
        this.shared.setQueued(createObject(REDIS_STRING,new SDS("+QUEUED\r\n")));
        this.shared.setWrongtypeerr(createObject(REDIS_STRING,new SDS("-ERR Operation against a key holding the wrong kind of value\r\n")));
        this.shared.setNokeyerr(createObject(REDIS_STRING,new SDS("-ERR no such key\r\n")));
        this.shared.setSyntaxerr(createObject(REDIS_STRING,new SDS("-ERR syntax error\r\n")));
        this.shared.setOutofrangeerr(createObject(REDIS_STRING,new SDS("-ERR source and destination objects are the same\r\n")));
        this.shared.setSpace(createObject(REDIS_STRING,new SDS(" ")));
        this.shared.setColon(createObject(REDIS_STRING,new SDS(":")));
        this.shared.setPlus(createObject(REDIS_STRING,new SDS("+")));
        this.shared.setSelect0(createObject(REDIS_STRING,new SDS("select 0\r\n")));
        this.shared.setSelect1(createObject(REDIS_STRING,new SDS("select 1\r\n")));
        this.shared.setSelect2(createObject(REDIS_STRING,new SDS("select 2\r\n")));
        this.shared.setSelect3(createObject(REDIS_STRING,new SDS("select 3\r\n")));
        this.shared.setSelect4(createObject(REDIS_STRING,new SDS("select 4\r\n")));
        this.shared.setSelect5(createObject(REDIS_STRING,new SDS("select 5\r\n")));
        this.shared.setSelect6(createObject(REDIS_STRING,new SDS("select 6\r\n")));
        this.shared.setSelect7(createObject(REDIS_STRING,new SDS("select 7\r\n")));
        this.shared.setSelect8(createObject(REDIS_STRING,new SDS("select 8\r\n")));
        this.shared.setSelect9(createObject(REDIS_STRING,new SDS("select 9\r\n")));
    }

    private void initCommand() {
        redisCommandTable = new HashMap<>();
        redisCommandTable.put(new SDS("set"), new RedisCommand(new SDS("SET"),1) {
            @Override
            public RedisCommandResponse process(RedisClient client) {
                int db = client.getDictId();
                RedisCommandResponse response = new RedisCommandResponse();
                RedisObject[] objects = new RedisObject[1];
                if (getDb()[db].getDict().add(client.getArgv()[1],client.getArgv()[2])) {
                    objects[0] = getShared().getCone();
                } else {
                    objects[0] = getShared().getCzero();
                }
                response.setResponse(objects);
                return response;
            }
        });
    }

    private void loadServerConfig(String configFileName) {
        Path path = Paths.get(configFileName);
        if (Files.isReadable(path)){
            try {
                java.util.List<String> list = Files.readAllLines(path);
                for (String line : list){
                    if (line.startsWith("#") || line.length() == 0){
                        continue;
                    }
                    String[] config = line.split( " ");
                    if (config[0].equals(TIME_OUT) && config.length == 2) {
                        this.maxIdleTime = Integer.parseInt(config[1]);
                    }
                    else if (config[0].equals(PORT) && config.length == 2) {
                        this.port = Integer.parseInt(config[1]);
                        if (this.port < 1 || this.port > 65535) {
                            throw new RuntimeException();
                        }
                    }
                    else if (config[0].equals(BIND) && config.length == 2) {
                        this.bindAddr = new SDS(config[1]);
                    }
                    else if (config[0].equals(SAVE) && config.length == 3) {
                        long seconds = Long.parseLong(config[1]);
                        long changes = Long.parseLong(config[2]);
                        if (seconds < 1 || changes > 0) {
                            throw new RuntimeException();
                        }
                        appendServerSaveParams(seconds,changes);
                    }
                    else if (config[0].equals(LOG_LEVEL) && config.length == 2) {
                        if (config[1].equals(LOG_LEVEL_DEBUG_STR)) {
                            this.verbosity = LOG_LEVEL_DEBUG;
                        }
                        else if (config[1].equals(LOG_LEVEL_NOTICE_STR)) {
                            this.verbosity = LOG_LEVEL_NOTICE;
                        }
                        else if (config[1].equals(LOG_LEVEL_WARNING_STR)) {
                            this.verbosity = LOG_LEVEL_WARNING;
                        }
                        else if (config[1].equals(LOG_LEVEL_VERBOSE_STR)) {
                            this.verbosity = LOG_LEVEL_VERBOSE;
                        }
                        else {
                            throw new RuntimeException();
                        }
                    }
                    else if (config[0].equals(LOG_FILE) && config.length == 2) {
                        if (config[1].equals(STDOUT)) {
                            this.logFile = null;
                        } else {
                            File file = new File(config[1]);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.close();
                        }
                    }
                    else if (config[0].equals(DATABASES) && config.length == 2) {
                        this.dbNum = Integer.parseInt(config[1]);
                        if (this.dbNum < 1) {
                            throw new RuntimeException();
                        }
                    }
                    else if (config[0].equals(MAX_CLIENTS) && config.length == 2) {
                        this.maxClients = Integer.parseInt(config[1]);
                    }
                    else if (config[0].equals(MAX_MEMORY) && config.length == 2 ) {
                        this.maxMemory = Long.parseLong(config[1]);
                    }
                    else if (config[0].equals(GLUE_OUTPUT_BUF) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.glueOutputBuf = 1;
                        }else if (config[1].equals("no")) {
                            this.glueOutputBuf = 0;
                        }
                    }
                    else if (config[0].equals(SHARE_OBJECTS) && config.length == 2) {
                        this.shareObjects = Integer.parseInt(config[1]);
                    }
                    else if (config[0].equals(RDB_COMPRESSION) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.rdbCompression = 1;
                        }else if (config[1].equals("no")) {
                            this.rdbCompression = 0;
                        }
                    }
                    else if (config[0].equals(SHARE_OBJECTS_POOL_SIZE) && config.length == 2) {

                    }
                    else if (config[0].equals(DAEMONIZE) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.daemonize = 1;
                        }else if (config[1].equals("no")) {
                            this.daemonize = 0;
                        }
                    }
                    else if (config[0].equals(APPEND_ONLY) && config.length == 2) {
                        if (config[1].equals("yes")) {
                            this.appendOnly = 1;
                        }else if (config[1].equals("no")) {
                            this.appendOnly = 0;
                        }
                    }
                    else if (config[0].equals(APPEND_FSYNC) && config.length == 2) {
                        if (config[1].equals("no")) {
                            this.appendFsync = APPENDFSYNC_NO;
                        }else if (config[1].equals("always")) {
                            this.appendFsync = APPENDFSYNC_ALWAYS;
                        }else if (config[1].equals("everysec")) {
                            this.appendFsync = APPENDFSYNC_EVERYSEC;
                        }
                    }
                    else if (config[0].equals(REQUIRE_PASS) && config.length == 2) {
                        this.requirePass = new SDS(config[1]);
                    }
                    else if (config[0].equals(PID_FILE) && config.length == 2) {
                        this.pidFile = new SDS(config[1]);
                    }
                    else if (config[0].equals(DB_FILE_NAME) && config.length == 2) {
                        this.dbFilename = new SDS(config[1]);
                    }else {
                        throw new RuntimeException();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initServerConfig() {
        this.stateStartTime = new Date();
        this.dbNum = REDIS_DEFAULT_DBNUM;
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
        this.objFreeList = new List();

        resetServerSaveParams();
        appendDefaultServerSaveParams();
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println(
//                    "Usage: " + EchoServer.class.getSimpleName() +
//                            " <port>");
//            return;
//        }
        new RedisServer().start();                //2
    }

    void resetServerSaveParams() {
        this.saveParams = new SaveParam[0];
        this.saveParamLen = 0;
    }

    void appendDefaultServerSaveParams() {
        this.saveParams = new SaveParam[3];
        this.saveParams[0] = new SaveParam(60*60, 1);
        this.saveParams[1] = new SaveParam(300, 100);
        this.saveParams[2] = new SaveParam(60, 10000);
        this.saveParamLen = 3;
    }

    void appendServerSaveParams(long seconds, long changes) {
        SaveParam newParam = new SaveParam(seconds,changes);
        SaveParam[] temp = new SaveParam[this.saveParamLen+1];
        System.arraycopy(this.saveParams,0,temp,0,this.saveParamLen);
        temp[this.saveParamLen] = newParam;
        this.saveParams = temp;
        this.saveParamLen = this.saveParamLen+1;
    }

}

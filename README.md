# Java-Redis

This is the redis implemented by Java.

## How to run?

As of now, only the server can be used normally. So there is the way to run the server.

```
git clone https://github.com/Math312/Java-Redis.git
cd Java-Redis
mvn package install
cd server/target
java -jar server-1.0-SNAPSHOT.jar
```

You can use the `redis-cli` provided by `redis` to connecting the port 6379.

## Command Support

1. GET
2. SET
3. APPEND
4. EXPIRE
5. EXISTS
6. TTL
7. PING
8. DEL

I will support all commands provided by redis.
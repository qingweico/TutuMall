package cn.qingweico.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @author zqw
 * @date 2022/7/12
 */
public class Snowflake {
    private static long workId;
    private static long sequence = 0L;
    private static final long SERVICE_ID = Math.abs(System.getenv().hashCode()) % 32;
    private static long lastTime = System.currentTimeMillis();
    private static final long MAX_SEQUENCE = (1 << 12) - 1;

    static {
        try {
            workId = Math.abs(Inet4Address.getLocalHost().getHostAddress().hashCode()) % 32;
        } catch (UnknownHostException e) {
            workId = new Random().nextLong() % 32;
        }
    }

    private Snowflake() {
    }

    public synchronized static Long nextId() {
        long l = System.currentTimeMillis();
        if (lastTime == l) {
            ++sequence;
        } else {
            lastTime = l;
            sequence = 0;
        }
        if (sequence > MAX_SEQUENCE) {
            nextId();
        }
        return lastTime << 22 | SERVICE_ID << 17 | workId << 12 | sequence;
    }
}
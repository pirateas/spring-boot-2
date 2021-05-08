package com.yty.boot2.mq.support;

/**
 * 延时等级为：1s，5s，10s，30s，1m，2m，3m，4m，5m，6m，7m，8m，9m，10m，20m，30m，1h，2h。
 * level=0，表示不延时
 * level=1，表示 1 级延时，对应延时 1s
 * level=2，表示 2 级延时，对应5s
 * ...
 *
 * @author yangtianyu
 */
public enum MessageDelayLevel {

    /**
     * MessageDelayTime
     */
    LEVEL_1(1, "1s"),
    LEVEL_2(2, "5s"),
    LEVEL_3(3, "10s"),
    LEVEL_4(4, "30s"),
    LEVEL_5(5, "1m"),
    LEVEL_6(6, "2m"),
    LEVEL_7(7, "3"),
    LEVEL_8(8, "4m"),
    LEVEL_9(9, "5m"),
    LEVEL_10(10, "6m"),
    LEVEL_11(11, "7m"),
    LEVEL_12(12, "8m"),
    LEVEL_13(13, "9m"),
    LEVEL_14(14, "10m"),
    LEVEL_15(15, "20m"),
    LEVEL_16(16, "30m"),
    LEVEL_17(17, "1h"),
    LEVEL_18(18, "2h");

    private final int level;
    private final String time;

    MessageDelayLevel(int level, String time) {
        this.level = level;
        this.time = time;
    }

    public int getLevel() {
        return this.level;
    }
}

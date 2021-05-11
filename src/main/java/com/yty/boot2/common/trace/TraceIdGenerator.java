package com.yty.boot2.common.trace;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tianyu.yang created on 2021/5/11
 * @version $Id$
 */
public class TraceIdGenerator {

    public static final String TRACE_ID = "TRACE_ID";

    private static final InetAddress HOST_IP = getINetAddress();
    private static final int PROCESS_ID = getProcessID();
    private static AtomicInteger SEQ = new AtomicInteger();

    private TraceIdGenerator() {
    }

    public static String generate() {
        //ip + pid + timestamp + seq
        byte[] data = new byte[16];

        byte[] ip = HOST_IP.getAddress();
        System.arraycopy(ip, ip.length - 4, data, 0, 4);
        writeInt(data, 4, PROCESS_ID);
        writeInt(data, 8, (int) (System.currentTimeMillis() / 1000));
        writeInt(data, 12, SEQ.incrementAndGet());

        return toHexString(data);
    }

    private static String toHexString(byte[] b) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex);
        }
        return ret.toString();
    }

    private static void writeInt(byte[] data, int i, int val) {
        data[i + 3] = (byte) val;
        data[i + 2] = (byte) (val >> 8);
        data[i + 1] = (byte) (val >> 16);
        data[i] = (byte) (val >> 24);
    }

    private static void writeShort(byte[] data, int i, short val) {
        data[i + 1] = (byte) val;
        data[i + 0] = (byte) (val >> 8);
    }

    private static InetAddress getINetAddress() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        return ip;
                    }
                }
            }
            return ip;
        } catch (SocketException e) {
            throw new RuntimeException("can not get valid IP Address", e);

        }
    }

    private static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]);
    }

    private static short getThreadID() {
        return (short) Thread.currentThread().getId();
    }
}

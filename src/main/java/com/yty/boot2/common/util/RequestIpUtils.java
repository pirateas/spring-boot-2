package com.yty.boot2.common.util;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;

/**
 * @author tianyu.yang created on 2021/5/11
 */
public class RequestIpUtils {

    private static final String HEADER_X_REAL_IP = "X-Real-IP";

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader(HEADER_X_REAL_IP);
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            // 这是一个可以伪造的头
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        // 最后一个为RemoteAddr
        int pos = ip.lastIndexOf(',');
        if (pos >= 0) {
            ip = ip.substring(pos);
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static String getPathInfo(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        return request.getRequestURI().substring(request.getContextPath().length());
    }

    /**
     * 是否内网请求
     *
     * @param request
     * @return
     */
    public static boolean isIntranet(HttpServletRequest request) {
        String xRealIp = request.getHeader(HEADER_X_REAL_IP);
        // 直接访问，一般是内网访问
        if (xRealIp == null || xRealIp.length() == 0) {
            return true;
        }
        try {
            InetAddress address = InetAddress.getByName(xRealIp);
            return address.isSiteLocalAddress() || address.isLoopbackAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * URL是否属于指定域名
     *
     * @param host
     * @param url
     * @return
     */
    public static boolean match(String host, String url) {
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("//")) {
            url = url.substring(url.indexOf('/') + 2);
        }

        int idx = url.indexOf('/');
        if (idx > 0) {
            return url.substring(0, idx).endsWith(host);
        }
        return url.endsWith(host);
    }

    public static boolean match(String hosts[], String url) {
        if (Strings.isNullOrEmpty(url)) {
            return false;
        }

        if (hosts == null || hosts.length == 0) {
            return true;
        }

        for (String host : hosts) {
            if (match(host, url)) {
                return true;
            }
        }
        return false;
    }

    private static final Splitter pattern = Splitter.on('&').trimResults().omitEmptyStrings();

    public static boolean validRedirect(String url) {
        return validRedirect(url, "", "r", "cbt");
    }

    public static boolean validRedirect(String url, String secret, String dataKey, String tokenKey) {
        if (Strings.isNullOrEmpty(url)) {
            return false;
        }

        int start = url.indexOf("?");
        if (start < 0) {
            return false;
        }

        String d = dataKey + "=";
        String t = tokenKey + "=";

        String data = null;
        String token = null;

        for (String str : pattern.split(url.substring(start + 1))) {
            if (str.startsWith(d)) { // r=
                data = str.substring(d.length());
                continue;
            }
            if (str.startsWith(t)) {
                token = str.substring(t.length());
            }
        }

        try {
            data = URLDecoder.decode(data, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException ignored) {
        }

        return validRedirect(data, secret, token);
    }

    public static boolean validRedirect(String data, String secret, String token) {
        return rtoken(data, secret).equals(token);
    }

    public static String rtoken(String data, String secret) {
        // s => bytes => md5_bytes => md5_bytes => hex string
        byte[] bytes = (data + secret).getBytes(Charsets.UTF_8);
        return Hashing.md5().hashBytes(Hashing.md5().hashBytes(bytes).asBytes()).toString();
    }

    public static Cookie cookieOf(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}

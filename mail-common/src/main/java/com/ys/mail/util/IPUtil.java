package com.ys.mail.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 2021-5-15
 *
 * @author ghdhj
 */
public class IPUtil {

    /**
     * 获取请求 IP (WEB 服务)
     *
     * @return IP 地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            String localityIp = "127.0.0.1";
            if (ip.equals(localityIp)) {
                //根据网卡取本机配置的 IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照','分割
        String comma = ",";
        int fifteen = 15;
        if (ip != null && ip.length() > fifteen) {
            if (ip.indexOf(comma) > 0) {
                ip = ip.substring(0, ip.indexOf(comma));
            }
        }
        return ip;
    }

    /**
     * 获取当前计算机 IP
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    /**
     * 获取当前计算机名称
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "未知";
    }

//    public static void main(String[] args) {
//        // 测试云闪付回调
//        String url = "http://117.48.192.183:8880/cgi-bin/n_get_pay_result.api";
//        Map<String, String> map = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
//        map.put("syscode", UnionPayConstant.JIH_SYS_CODE);
//        map.put("trans_time", new SimpleDateFormat(DateUtil.DT_LONG).format(new Date()));
//        map.put("account", UnionPayConstant.JIH_ACCOUNT);
//        map.put("app_id", "202112242012228944096");
//        String signData = WebPay.getSignContent(map);
//        String sing = SignUtils.sign(UnionPayConstant.JIU_SIGN_KEY, signData);
//        map.put("sign", sing);
//        String respStr = HttpClientUtil.sendPost(url, map);
//        System.out.println(respStr);
//    }

    /**
     * 获取当前机器的内网IP
     *
     * @return /
     */
    public static String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface anInterface = interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddress.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddress;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return "";
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(getHostIp());
        System.out.println(getLocalIp());
        System.out.println(getIpAddress());
    }

}

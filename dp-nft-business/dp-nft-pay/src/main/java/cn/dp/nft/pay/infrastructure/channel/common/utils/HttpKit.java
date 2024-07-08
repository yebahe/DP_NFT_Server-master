package cn.dp.nft.pay.infrastructure.channel.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;


import com.google.common.collect.Maps;
import com.ijpay.core.http.AbstractHttpDelegate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Http 工具类
 *
 * @author yebahe
 */
public class HttpKit {

    private static AbstractHttpDelegate delegate = new DefaultHttpKit();

    public static AbstractHttpDelegate getDelegate() {
        return delegate;
    }

    public static void setDelegate(AbstractHttpDelegate delegate) {
        HttpKit.delegate = delegate;
    }

    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将同步通知的参数转化为Map
     *
     * @param request {@link HttpServletRequest}
     * @return 转化后的 Map
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMapWithExpectedSize(2);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
}

/**
 * 使用 huTool 实现的 Http 工具类
 *
 * @author Javen
 */
class DefaultHttpKit extends AbstractHttpDelegate {
}

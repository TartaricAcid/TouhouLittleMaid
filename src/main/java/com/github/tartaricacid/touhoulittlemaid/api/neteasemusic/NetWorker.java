package com.github.tartaricacid.touhoulittlemaid.api.neteasemusic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * @author 内个球
 */
public class NetWorker {
    public static String get(String url, Map<String, String> requestPropertyData) throws IOException {
        StringBuilder result = new StringBuilder();

        URL urlConnect = new URL(url);
        URLConnection connection = urlConnect.openConnection();

        Collection<String> keys = requestPropertyData.keySet();
        for (String key : keys) {
            String val = requestPropertyData.get(key);
            connection.setRequestProperty(key, val);
        }
        connection.setConnectTimeout(12000);
        connection.setDoInput(true);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        bufferedReader.close();

        return result.toString();
    }

    @Nullable
    public static String getRedirectUrl(String url, Map<String, String> requestPropertyData) throws IOException {
        URL urlConnect = new URL(url);
        URLConnection connection = urlConnect.openConnection();
        Collection<String> keys = requestPropertyData.keySet();
        for (String key : keys) {
            String val = requestPropertyData.get(key);
            connection.setRequestProperty(key, val);
        }
        connection.setConnectTimeout(3_000);
        connection.setReadTimeout(5_000);
        return connection.getHeaderField("Location");
    }

    public static String post(String url, String param, Map<String, String> requestPropertyData) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader;
        PrintWriter printWriter;

        URL urlConnect = new URL(url);
        URLConnection connection = urlConnect.openConnection();

        Collection<String> keys = requestPropertyData.keySet();
        for (String key : keys) {
            String val = requestPropertyData.get(key);
            connection.setRequestProperty(key, val);
        }
        connection.setConnectTimeout(12000);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        printWriter = new PrintWriter(connection.getOutputStream());
        printWriter.print(param);
        printWriter.flush();

        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        bufferedReader.close();
        printWriter.close();

        return result.toString();
    }
}

package net.tickmc.lccutils.utilities;

import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpsHelper {
    public static String get(String url) {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Debug.error(e);
        }
        return null;
    }

    public static JsonObject getAsJson(String url) {
        return GeneralUtilities.parseJson(get(url));
    }

    /**
     * Downloads a file from the given URL.
     *
     * @param url  The URL to download from.
     * @param path The path to save the file to.
     * @return Whether the download was successful.
     */
    public static boolean download(URL url, String path) {
        try {
            String fileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
            Path fullPath = Paths.get(path, fileName);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(fullPath.toString());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return true;
        } catch (IOException e) {
            Debug.error(e);
            return false;
        }
    }
}

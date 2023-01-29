package net.tickmc.lccutils.managers.updates;

import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.utilities.Debug;
import net.tickmc.lccutils.utilities.HttpsHelper;

import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {
    private static URL repoApiUrl;
    private static URL downloadUrl;
    private static Version latestVersion;

    static {
        try {
            repoApiUrl = new URL("https://api.github.com/repos/0tickpulse/lcc-utils/releases/latest");
        } catch (MalformedURLException e) {
            Debug.error(e);
        }

        try {
            String assetsUrl = HttpsHelper.getAsJson(repoApiUrl.toString()).getAsJsonObject().get("assets_url").getAsString();
            String downloadUrl = HttpsHelper.getAsJson(assetsUrl).getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            UpdateManager.downloadUrl = new URL(downloadUrl);
        } catch (MalformedURLException e) {
            Debug.error(e);
        }
    }

    public static Version updateLatestVersion() {
        latestVersion = new Version(HttpsHelper.getAsJson(repoApiUrl.toString()).getAsJsonObject().get("tag_name").getAsString());
        return latestVersion;
    }

    public static Version getLatestVersion() {
        return latestVersion == null ? updateLatestVersion() : latestVersion;
    }

    public static boolean isLatestVersion() {
        return LccUtils.getVersion().compareTo(getLatestVersion()) >= 0;
    }

    public static URL getRepoApiUrl() {
        return repoApiUrl;
    }

    /**
     * Updates the jar file for the plugin, and returns the new file's path.
     */
    public static String updateJar() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}

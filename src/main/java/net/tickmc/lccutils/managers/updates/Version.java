package net.tickmc.lccutils.managers.updates;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A convenience class that manages a versioning system similar to semantic versioning, using the following format:
 * <pre>
 * <i>major</i>.<i>minor</i>.<i>patch</i>(-SNAPSHOT-<i>build</i>)
 * </pre>
 * Not recommended for general use as it is very strict.
 *
 * @see <a href="https://semver.org/">Semantic Versioning 2.0.0</a>
 */
public class Version implements Comparable<Version> {
    private int major;
    private int minor;
    private int patch;
    private int snapshot;

    public enum Identifier {
        MAJOR, MINOR, PATCH, SNAPSHOT
    }

    private static final Pattern VERSION_REGEX = Pattern.compile("^(?<major>\\d+)\\.(?<minor>\\d+)\\.(?<patch>\\d+)(-SNAPSHOT-(?<snapshot>\\d+))?$");

    /**
     * This constructor deserializes a version string into a  object.
     *
     * @throws IllegalArgumentException if the version string is not valid.
     */
    public Version(@NotNull String version) {
        Matcher matcher = VERSION_REGEX.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid version format " + version + "! Must match " + VERSION_REGEX);
        }
        String[] split = version.split("\\.");
        major = Integer.parseInt(matcher.group("major"));
        minor = Integer.parseInt(matcher.group("minor"));
        patch = Integer.parseInt(matcher.group("patch"));
        String snapshotString = matcher.group("snapshot");
        if (snapshotString != null) {
            snapshot = Integer.parseInt(snapshotString);
        }
    }

    public String toString() {
        return major + "." + minor + "." + patch + (isSnapshot() ? "-SNAPSHOT-" + snapshot : "");
    }

    public int[] getParts() {
        return new int[]{major, minor, patch};
    }

    /**
     * Gets the major version number.
     */
    public int getMajor() {
        return major;
    }

    /**
     * Gets the minor version number.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets the patch version number.
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Gets the snapshot version number.
     */
    public int getSnapshot() {
        return snapshot;
    }

    /**
     * Gets whether this version is a snapshot.
     */
    public boolean isSnapshot() {
        return snapshot > 0;
    }

    /**
     * Gets whether this version is a release.
     */
    public boolean isRelease() {
        return !isSnapshot();
    }

    /**
     * Sets the major version number.
     * * @return The version object after the transformation (useful for method chaining).
     */
    public Version setMajor(int major) {
        this.major = major;
        return this;
    }

    /**
     * Sets the minor version number.
     *
     * @return The version object after the transformation (useful for method chaining).
     */
    public Version setMinor(int minor) {
        this.minor = minor;
        return this;
    }

    /**
     * Sets the patch version number.
     *
     * @return The version object after the transformation (useful for method chaining).
     */
    public Version setPatch(int patch) {
        this.patch = patch;
        return this;
    }

    /**
     * Sets the snapshot version number.
     *
     * @return The version object after the transformation (useful for method chaining).
     */
    public Version setSnapshot(int snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    /**
     * Increments the version.
     *
     * @param identifier the identifier to increment.
     * @return The version object after the transformation (useful for method chaining).
     */
    public Version increment(Identifier identifier, int amount) {
        boolean resetSnapshot = true;
        switch (identifier) {
            case MAJOR -> major += amount;
            case MINOR -> minor += amount;
            case PATCH -> patch += amount;
            case SNAPSHOT -> {
                snapshot += amount;
                resetSnapshot = false;
            }
        }
        if (resetSnapshot) {
            snapshot = 0;
        }
        return this;
    }

    @Override
    public int compareTo(@NotNull Version that) {
        if (this.major != that.major) {
            return this.major - that.major;
        }
        if (this.minor != that.minor) {
            return this.minor - that.minor;
        }
        if (this.patch != that.patch) {
            return this.patch - that.patch;
        }
        if (this.snapshot != that.snapshot) {
            return this.snapshot - that.snapshot;
        }
        return 0;
    }

    public String toJSONString() {
        return "{\"result\":\"" + this + "\",\"major\":" + major + ",\"minor\":" + minor + ",\"patch\":" + patch + ",\"snapshot\":" + snapshot + "}";
    }
}

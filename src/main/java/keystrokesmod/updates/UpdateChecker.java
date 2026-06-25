package keystrokesmod.updates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import keystrokesmod.Raven;
import keystrokesmod.utility.Mc;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

// update checker for modrinth + github
public class UpdateChecker {
    private static final String USER_AGENT = "Raven-bS-UpdateChecker/2.0";
    private static final String MODRINTH_SLUG = "raven-bs";
    private static final String GITHUB_REPO = "olziw/Raven-B21";

    private static final Logger LOGGER = LoggerFactory.getLogger("Raven-bS/Updates");

    private final String currentVersion;
    private boolean updateAvailable;
    private String latestVersion = "";
    private String modrinthUrl = "";
    private String githubUrl = "";

    private volatile RemoteVersion modrinthResult;
    private volatile RemoteVersion githubResult;

    private final Map<UUID, LocalDate> notifiedPlayers = new HashMap<>();

    public UpdateChecker() {
        this.currentVersion = Raven.VERSION;
        checkForUpdates().thenAccept(hasUpdate -> {
            if (hasUpdate) {
                displayConsoleUpdateMessage();
            }
        }).exceptionally(ex -> {
            LOGGER.warn("Failed to check for updates: {}", ex.getMessage());
            return null;
        });

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft client) {
        if (client.player == null || !updateAvailable) {
            return;
        }
        if (!client.player.hasPermissions(2)) {
            return;
        }
        UUID id = client.player.getUUID();
        LocalDate today = LocalDate.now();
        notifiedPlayers.entrySet().removeIf(e -> e.getValue().isBefore(today));
        if (notifiedPlayers.containsKey(id) && notifiedPlayers.get(id).isEqual(today)) {
            return;
        }
        sendUpdateNotification(client);
        notifiedPlayers.put(id, today);
    }

    private void displayConsoleUpdateMessage() {
        LOGGER.info("────────────────────────────────────────────────────");
        LOGGER.info(" Raven bS++ Update Available");
        LOGGER.info(" Current: {}  Latest: {}", currentVersion, latestVersion);
        if (!modrinthUrl.isEmpty()) LOGGER.info(" Modrinth: {}", modrinthUrl);
        if (!githubUrl.isEmpty()) LOGGER.info(" GitHub: {}", githubUrl);
        LOGGER.info("────────────────────────────────────────────────────");
    }

    public CompletableFuture<Boolean> checkForUpdates() {
        modrinthResult = null;
        githubResult = null;
        return CompletableFuture.allOf(fetchModrinthVersion(), fetchGitHubVersion()).thenApply(ignored -> {
            Version current = new Version(currentVersion);
            Version latest = Version.ZERO;
            String bestLabel = "";
            if (modrinthResult != null && modrinthResult.version.compareTo(latest) > 0) {
                latest = modrinthResult.version;
                bestLabel = modrinthResult.versionLabel;
            }
            if (githubResult != null && githubResult.version.compareTo(latest) > 0) {
                latest = githubResult.version;
                bestLabel = githubResult.versionLabel;
            }
            if (modrinthResult != null) modrinthUrl = modrinthResult.pageUrl;
            if (githubResult != null) githubUrl = githubResult.pageUrl;
            if (latest.compareTo(current) > 0) {
                latestVersion = bestLabel.isEmpty() ? latest.toString() : bestLabel;
                updateAvailable = true;
                return true;
            }
            updateAvailable = false;
            return false;
        });
    }

    private record RemoteVersion(Version version, String versionLabel, String pageUrl) {}

    private CompletableFuture<Void> fetchModrinthVersion() {
        return CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("https://api.modrinth.com/v2/project/" + MODRINTH_SLUG + "/version");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                if (connection.getResponseCode() != 200) return;
                JsonArray versions = JsonParser.parseString(readResponse(connection)).getAsJsonArray();
                if (versions.isEmpty()) return;
                JsonObject chosen = pickNewestByDate(versions, true);
                if (chosen == null) chosen = pickNewestByDate(versions, false);
                if (chosen == null) return;
                String versionNumber = chosen.get("version_number").getAsString();
                String versionId = chosen.get("id").getAsString();
                modrinthResult = new RemoteVersion(new Version(versionNumber), versionNumber,
                        "https://modrinth.com/plugin/" + MODRINTH_SLUG + "/version/" + versionId);
            } catch (Exception e) {
                LOGGER.warn("Modrinth update check error: {}", e.getMessage());
            }
        });
    }

    private CompletableFuture<Void> fetchGitHubVersion() {
        return CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("https://api.github.com/repos/" + GITHUB_REPO + "/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                if (connection.getResponseCode() != 200) return;
                JsonObject release = JsonParser.parseString(readResponse(connection)).getAsJsonObject();
                if (!release.has("tag_name")) return;
                String tagName = release.get("tag_name").getAsString();
                String versionLabel = tagName.replaceAll("^v", "");
                String pageUrl = release.has("html_url") ? release.get("html_url").getAsString()
                        : "https://github.com/" + GITHUB_REPO + "/releases/latest";
                githubResult = new RemoteVersion(new Version(versionLabel), versionLabel, pageUrl);
            } catch (Exception e) {
                LOGGER.warn("GitHub update check error: {}", e.getMessage());
            }
        });
    }

    private JsonObject pickNewestByDate(JsonArray versions, boolean releaseOnly) {
        JsonObject newest = null;
        for (JsonElement element : versions) {
            JsonObject version = element.getAsJsonObject();
            if (releaseOnly && !"release".equals(version.get("version_type").getAsString())) continue;
            if (newest == null || version.get("date_published").getAsString()
                    .compareTo(newest.get("date_published").getAsString()) > 0) {
                newest = version;
            }
        }
        return newest;
    }

    private String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void sendUpdateNotification(Minecraft client) {
        if (client.player == null) return;
        client.player.displayClientMessage(net.minecraft.network.chat.Component.literal("----- Raven bS++ Update -----").withStyle(ChatFormatting.AQUA), false);
        client.player.displayClientMessage(net.minecraft.network.chat.Component.literal("New update available!").withStyle(ChatFormatting.GREEN), false);
        client.player.displayClientMessage(net.minecraft.network.chat.Component.literal("Current: " + currentVersion + "  Latest: " + latestVersion)
                .withStyle(ChatFormatting.GRAY), false);
        if (!modrinthUrl.isEmpty()) {
            client.player.displayClientMessage(net.minecraft.network.chat.Component.literal("[Download on Modrinth]").withStyle(ChatFormatting.GOLD)
                    .withStyle(s -> s.withClickEvent(new ClickEvent.OpenUrl(java.net.URI.create(modrinthUrl)))
                            .withHoverEvent(new HoverEvent.ShowText(net.minecraft.network.chat.Component.literal("Open Modrinth release")))), false);
        }
        if (!githubUrl.isEmpty()) {
            client.player.displayClientMessage(net.minecraft.network.chat.Component.literal("[View on GitHub]").withStyle(ChatFormatting.GOLD)
                    .withStyle(s -> s.withClickEvent(new ClickEvent.OpenUrl(java.net.URI.create(githubUrl)))
                            .withHoverEvent(new HoverEvent.ShowText(net.minecraft.network.chat.Component.literal("Open GitHub release")))), false);
        }
        client.player.playSound(SoundEvents.PLAYER_LEVELUP, 0.8f, 1.2f);
    }

    private static final class Version implements Comparable<Version> {
        static final Version ZERO = new Version("0");
        private final int[] parts;

        Version(String raw) {
            String cleaned = raw.replaceAll("[^0-9.]", "");
            String[] split = cleaned.split("\\.");
            parts = new int[Math.max(split.length, 1)];
            for (int i = 0; i < split.length; i++) {
                try { parts[i] = Integer.parseInt(split[i]); } catch (NumberFormatException ignored) { parts[i] = 0; }
            }
        }

        @Override
        public int compareTo(Version o) {
            int len = Math.max(parts.length, o.parts.length);
            for (int i = 0; i < len; i++) {
                int a = i < parts.length ? parts[i] : 0;
                int b = i < o.parts.length ? o.parts[i] : 0;
                if (a != b) return Integer.compare(a, b);
            }
            return 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) sb.append('.');
                sb.append(parts[i]);
            }
            return sb.toString();
        }
    }
}

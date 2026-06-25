package keystrokesmod.script.classes;

import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.HashMap;

public class NetworkPlayer {
    private final PlayerInfo playerInfo;
    private static final HashMap<String, NetworkPlayer> cache = new HashMap<>();

    public NetworkPlayer(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public String getCape() {
        return "";
    }

    public String getDisplayName() {
        return getName();
    }

    public String getName() {
        return playerInfo == null ? "" : playerInfo.getProfile().name();
    }

    public int getPing() {
        return playerInfo == null ? 0 : playerInfo.getLatency();
    }

    public String getSkinData() {
        return "";
    }

    public String getUUID() {
        return playerInfo == null ? "" : playerInfo.getProfile().id().toString();
    }

    public static NetworkPlayer convert(PlayerInfo playerInfo) {
        if (playerInfo == null) {
            return null;
        }
        String name = playerInfo.getProfile().name();
        return cache.computeIfAbsent(name, ignored -> new NetworkPlayer(playerInfo));
    }

    public static void clearCache() {
        cache.clear();
    }
}

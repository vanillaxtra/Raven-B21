package keystrokesmod.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProfileUtils {
    public static String getMojangProfile(String name) {
        String response = NetworkUtils.getTextFromURL("https://api.mojang.com/users/profiles/minecraft/" + name, false, false);
        if (response.isEmpty()) {
            return "";
        }
        try {
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            return json.has("id") ? json.get("id").getAsString() : "";
        } catch (Exception ignored) {
            return "";
        }
    }

    public static int[] getHypixelStats(String playerName, DM mode) {
        int[] stats = new int[]{0, 0, 0};
        String uuid = getMojangProfile(playerName);
        if (uuid.isEmpty()) {
            stats[0] = -1;
            return stats;
        }
        String body = NetworkUtils.getTextFromURL("https://api.hypixel.net/player?key=" + NetworkUtils.API_KEY + "&uuid=" + uuid, false, false);
        if (body.isEmpty()) {
            return null;
        }
        if (body.equals("{\"success\":true,\"player\":null}")) {
            stats[0] = -1;
            return stats;
        }
        JsonObject duels;
        try {
            JsonObject player = parseJson(body).getAsJsonObject("player");
            duels = player.getAsJsonObject("stats").getAsJsonObject("Duels");
        } catch (NullPointerException ignored) {
            return stats;
        }
        switch (mode) {
            case OVERALL -> {
                stats[0] = getValueAsInt(duels, "wins");
                stats[1] = getValueAsInt(duels, "losses");
                stats[2] = getValueAsInt(duels, "current_winstreak");
            }
            case BRIDGE -> {
                stats[0] = getValueAsInt(duels, "bridge_duel_wins");
                stats[1] = getValueAsInt(duels, "bridge_duel_losses");
                stats[2] = getValueAsInt(duels, "current_winstreak_mode_bridge_duel");
            }
            case UHC -> {
                stats[0] = getValueAsInt(duels, "uhc_duel_wins");
                stats[1] = getValueAsInt(duels, "uhc_duel_losses");
                stats[2] = getValueAsInt(duels, "current_winstreak_mode_uhc_duel");
            }
            case SKYWARS -> {
                stats[0] = getValueAsInt(duels, "sw_duel_wins");
                stats[1] = getValueAsInt(duels, "sw_duel_losses");
                stats[2] = getValueAsInt(duels, "current_winstreak_mode_sw_duel");
            }
            case CLASSIC -> {
                stats[0] = getValueAsInt(duels, "classic_duel_wins");
                stats[1] = getValueAsInt(duels, "classic_duel_losses");
                stats[2] = getValueAsInt(duels, "current_winstreak_mode_classic_duel");
            }
            case SUMO -> {
                stats[0] = getValueAsInt(duels, "sumo_duel_wins");
                stats[1] = getValueAsInt(duels, "sumo_duel_losses");
                stats[2] = getValueAsInt(duels, "current_winstreak_mode_sumo_duel");
            }
            case OP -> {
                stats[0] = getValueAsInt(duels, "op_duel_wins");
                stats[1] = getValueAsInt(duels, "op_duel_losses");
                stats[2] = getValueAsInt(duels, "current_winstreak_mode_op_duel");
            }
        }
        return stats;
    }

    public static JsonObject parseJson(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    private static int getValueAsInt(JsonObject jsonObject, String key) {
        try {
            return jsonObject.get(key).getAsInt();
        } catch (NullPointerException ignored) {
            return 0;
        }
    }

    public enum DM {
        OVERALL,
        BRIDGE,
        UHC,
        SKYWARS,
        CLASSIC,
        SUMO,
        OP
    }
}

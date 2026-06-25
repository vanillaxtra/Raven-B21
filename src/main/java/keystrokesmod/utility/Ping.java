package keystrokesmod.utility;

import keystrokesmod.event.ClientChatReceivedEvent;
import keystrokesmod.event.SubscribeEvent;

public class Ping {
    private static boolean sendChat = false;
    private static long sendTime = 0L;
    private static boolean sendChatCC = false;
    private static long sendTimeCC = 0L;

    @SubscribeEvent
    public void onChatMessageRecieved(ClientChatReceivedEvent event) {
        if ((sendChat ^ sendChatCC) && Mc.nullCheck()) {
            if (Utils.stripColor(event.message.getString()).startsWith("Unknown")) {
                event.setCanceled(true);
                getPing(sendChatCC);
                sendChat = false;
                sendChatCC = false;
            }
        }
    }

    public static void checkPing(boolean isChat) {
        if (isChat) {
            Utils.sendMessage("&7[&fping&7] &7Checking...");
        } else {
            Commands.print("Â§3Checking...", 1);
        }
        if (sendChat) {
            if (isChat) {
                Utils.sendMessage("&7[&fping&7] &7Please wait.");
            } else {
                Commands.print("Â§cPlease wait.", 0);
            }
            return;
        }
        Mc.player().connection.sendCommand("...");
        if (isChat) {
            sendChatCC = true;
            sendTimeCC = System.currentTimeMillis();
        } else {
            sendChat = true;
            sendTime = System.currentTimeMillis();
        }
    }

    private void getPing(boolean isChat) {
        int ping = (int) (System.currentTimeMillis() - (isChat ? sendTimeCC : sendTime)) - 20;
        if (ping < 0) {
            ping = 0;
        }
        if (isChat) {
            Utils.sendMessage("&7[&fping&7] &7Your ping: &b" + ping + "&7ms.");
        } else {
            Commands.print("Your ping: " + ping + "ms", 0);
        }
        reset(isChat);
    }

    public static void reset(boolean isChat) {
        if (isChat) {
            sendChatCC = false;
            sendTimeCC = 0L;
        } else {
            sendChat = false;
            sendTime = 0L;
        }
    }
}

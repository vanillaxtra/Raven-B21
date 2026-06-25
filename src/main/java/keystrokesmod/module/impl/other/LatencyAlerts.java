package keystrokesmod.module.impl.other;

import keystrokesmod.event.ReceivePacketEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;

import java.util.List;

public class LatencyAlerts extends Module {
    private SliderSetting interval;
    private SliderSetting highLatency;
    private ButtonSetting ignoreLimbo;
    private long lastPacket;
    private long lastAlert;

    public LatencyAlerts() {
        super("Latency Alerts", category.other);
        this.registerSetting(new DescriptionSetting("Detects packet loss."));
        this.registerSetting(interval = new SliderSetting("Alert interval", " second", 3.0, 0.0, 5.0, 0.1));
        this.registerSetting(highLatency = new SliderSetting("High latency", " second", 0.5, 0.1, 5.0, 0.1));
        this.registerSetting(ignoreLimbo = new ButtonSetting("Ignore limbo", true));
        this.closetModule = true;
    }

    @SubscribeEvent
    public void onPacketReceive(ReceivePacketEvent e) {
        lastPacket = System.currentTimeMillis();
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        if (mc.hasSingleplayerServer() || (ignoreLimbo.isToggled() && inLimbo())) {
            lastPacket = System.currentTimeMillis();
            lastAlert = System.currentTimeMillis();
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastPacket >= highLatency.getInput() * 1000 && now - lastAlert >= interval.getInput() * 1000) {
            Utils.sendMessage("&7Packet loss detected: §c" + Math.abs(now - lastPacket) + "&7ms");
            lastAlert = now;
        }
    }

    public void onDisable() {
        lastPacket = 0;
        lastAlert = 0;
    }

    public void onEnable() {
        lastPacket = System.currentTimeMillis();
    }

    public boolean inLimbo() {
        List<String> scoreboard = Utils.getSidebarLines();
        if (scoreboard == null || scoreboard.isEmpty()) {
            return mc.level.dimension().identifier().getPath().contains("end");
        }
        return false;
    }
}

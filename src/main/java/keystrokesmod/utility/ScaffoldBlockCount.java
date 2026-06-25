package keystrokesmod.utility;

import keystrokesmod.event.RavenEventBus;
import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.ModuleManager;
import net.minecraft.network.chat.Component;

public class ScaffoldBlockCount {
    private Timer fadeTimer;
    private Timer fadeInTimer;
    private float previousAlpha;

    public ScaffoldBlockCount() {
        this.fadeTimer = null;
        this.fadeInTimer = new Timer(150);
        this.fadeInTimer.start();
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        if (previousAlpha <= 10 && fadeInTimer == null) {
            onDisable();
            return;
        }
        if (!Mc.nullCheck() || !ModuleManager.scaffold.showBlockCount.isToggled()) {
            return;
        }
        if (event.phase != RenderTickEvent.Phase.END || Mc.mc().screen != null) {
            return;
        }
        int blocks = ModuleManager.scaffold.totalBlocks();
        String color = "Â§";
        if (blocks <= 5) {
            color += "c";
        } else if (blocks <= 15) {
            color += "6";
        } else if (blocks <= 25) {
            color += "e";
        } else {
            color = "";
        }
        float alpha = fadeTimer == null ? 255 : 255 - fadeTimer.getValueInt(0, 255, 1);
        if (fadeInTimer != null) {
            alpha = fadeInTimer.getValueFloat(10, 255, 1);
            if (alpha == 255) {
                fadeInTimer = null;
            }
        }
        previousAlpha = alpha;
        int colorAlpha = Utils.mergeAlpha(-1, (int) previousAlpha);
        int x = Mc.mc().getWindow().getGuiScaledWidth() / 2 + 8;
        int y = Mc.mc().getWindow().getGuiScaledHeight() / 2 + 4;
        RenderUtils.drawStringWithShadow(color + blocks + " Â§rblock" + (blocks == 1 ? "" : "s"), x, y, colorAlpha);
    }

    public void beginFade() {
        this.fadeTimer = new Timer(150);
        this.fadeTimer.start();
        this.fadeInTimer = null;
    }

    public void onDisable() {
        RavenEventBus.unregister(this);
        fadeInTimer = null;
        fadeTimer = null;
    }
}

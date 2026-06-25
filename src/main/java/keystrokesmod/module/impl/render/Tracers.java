package keystrokesmod.module.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.world.AntiBot;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class Tracers extends Module {
    private SliderSetting red;
    private SliderSetting green;
    private SliderSetting blue;
    private SliderSetting lineWidth;
    private ButtonSetting rainbow;
    private ButtonSetting showInvis;

    public Tracers() {
        super("Tracers", category.render, 0);
        this.registerSetting(red = new SliderSetting("Red", 255, 0, 255, 1));
        this.registerSetting(green = new SliderSetting("Green", 255, 0, 255, 1));
        this.registerSetting(blue = new SliderSetting("Blue", 255, 0, 255, 1));
        this.registerSetting(lineWidth = new SliderSetting("Line width", 1.0, 0.5, 5.0, 0.5));
        this.registerSetting(rainbow = new ButtonSetting("Rainbow", false));
        this.registerSetting(showInvis = new ButtonSetting("Show invis", true));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        int rgb = rainbow.isToggled() ? Utils.getChroma(2L, 0L) : ((int) red.getInput() << 16) | ((int) green.getInput() << 8) | (int) blue.getInput();
        for (Entity en : mc.level.entitiesForRendering()) {
            if (!(en instanceof Player player) || player == mc.player || !player.isAlive()) {
                continue;
            }
            if (!showInvis.isToggled() && player.isInvisible()) {
                continue;
            }
            if (AntiBot.isBot(player)) {
                continue;
            }
            RenderUtils.drawTracer(player, rgb, (float) lineWidth.getInput(), e.tickDelta);
        }
    }
}

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
import net.minecraft.world.entity.player.Player;

public class PlayerESP extends Module {
    private SliderSetting red;
    private SliderSetting green;
    private SliderSetting blue;
    private ButtonSetting rainbow;
    private ButtonSetting twoD;
    private ButtonSetting skeleton;

    public PlayerESP() {
        super("PlayerESP", category.render, 0);
        this.registerSetting(red = new SliderSetting("Red", 255, 0, 255, 1));
        this.registerSetting(green = new SliderSetting("Green", 0, 0, 255, 1));
        this.registerSetting(blue = new SliderSetting("Blue", 0, 0, 255, 1));
        this.registerSetting(rainbow = new ButtonSetting("Rainbow", false));
        this.registerSetting(twoD = new ButtonSetting("2D", false));
        this.registerSetting(skeleton = new ButtonSetting("Skeleton", false));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive() || AntiBot.isBot(player)) {
                continue;
            }
            int rgb = rainbow.isToggled() ? Utils.getChroma(2L, 0L) : ((int) red.getInput() << 16) | ((int) green.getInput() << 8) | (int) blue.getInput();
            RenderUtils.drawEntityBox(player, rgb, twoD.isToggled());
            if (skeleton.isToggled()) {
                RenderUtils.drawSkeleton(player, rgb);
            }
        }
    }
}

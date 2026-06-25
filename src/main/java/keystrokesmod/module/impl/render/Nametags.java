package keystrokesmod.module.impl.render;

import keystrokesmod.utility.RenderUtils;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.world.AntiBot;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.player.Player;

public class Nametags extends Module {
    private SliderSetting scale;
    private ButtonSetting health;
    private ButtonSetting background;
    private ButtonSetting invisibles;

    public Nametags() {
        super("Nametags", category.render, 0);
        this.registerSetting(scale = new SliderSetting("Scale", 1.0, 0.5, 2.0, 0.1));
        this.registerSetting(health = new ButtonSetting("Health", true));
        this.registerSetting(background = new ButtonSetting("Background", true));
        this.registerSetting(invisibles = new ButtonSetting("Invisibles", true));
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive() || AntiBot.isBot(player)) {
                continue;
            }
            if (!invisibles.isToggled() && player.isInvisible()) {
                continue;
            }
            var pos = player.position().add(0, player.getBbHeight() + 0.5, 0);
            var screen = RenderUtils.worldToScreen(pos);
            if (screen == null) {
                continue;
            }
            String name = player.getDisplayName().getString();
            if (health.isToggled()) {
                name += " " + Utils.getHealthStr(player, false);
            }
            float s = (float) scale.getInput();
            RenderUtils.drawTextWithShadow(mc.font, name, (float) screen.x / s, (float) screen.y / s, -1);
        }
    }
}

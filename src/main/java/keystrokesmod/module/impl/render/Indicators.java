package keystrokesmod.module.impl.render;

import keystrokesmod.utility.RenderUtils;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class Indicators extends Module {
    private SliderSetting size;
    private ButtonSetting renderOnlyOffScreen;
    private ButtonSetting players;

    public Indicators() {
        super("Indicators", category.render, 0);
        this.registerSetting(size = new SliderSetting("Size", 8, 4, 16, 1));
        this.registerSetting(renderOnlyOffScreen = new ButtonSetting("Only offscreen", true));
        this.registerSetting(players = new ButtonSetting("Players", true));
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        int cx = mc.getWindow().getGuiScaledWidth() / 2;
        int cy = mc.getWindow().getGuiScaledHeight() / 2;
        for (Entity en : mc.level.entitiesForRendering()) {
            if (en == mc.player) {
                continue;
            }
            if (players.isToggled() && !(en instanceof Player)) {
                continue;
            }
            var screen = Utils.worldToScreen(en.position().add(0, en.getBbHeight() / 2, 0));
            if (renderOnlyOffScreen.isToggled() && screen != null) {
                continue;
            }
            double dx = en.getX() - mc.player.getX();
            double dz = en.getZ() - mc.player.getZ();
            float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - mc.player.getYRot() - 90;
            double rad = Math.toRadians(angle);
            int x = cx + (int) (Math.cos(rad) * 60);
            int y = cy + (int) (Math.sin(rad) * 60);
            RenderUtils.drawTextWithShadow(mc.font, "*", x, y, Utils.getChroma(2L, 0L));
        }
    }
}

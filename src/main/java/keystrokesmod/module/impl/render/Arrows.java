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

public class Arrows extends Module {
    private SliderSetting size;
    private ButtonSetting rainbow;

    public Arrows() {
        super("Arrows", category.render, 0);
        this.registerSetting(size = new SliderSetting("Size", 10, 5, 20, 1));
        this.registerSetting(rainbow = new ButtonSetting("Rainbow", false));
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        int cx = mc.getWindow().getGuiScaledWidth() / 2;
        int cy = mc.getWindow().getGuiScaledHeight() / 2;
        for (Player player : mc.level.players()) {
            if (player == mc.player || AntiBot.isBot(player)) {
                continue;
            }
            double dx = player.getX() - mc.player.getX();
            double dz = player.getZ() - mc.player.getZ();
            float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - mc.player.getYRot() - 90;
            int color = rainbow.isToggled() ? Utils.getChroma(2L, 0L) : -1;
            double rad = Math.toRadians(angle);
            int s = (int) size.getInput();
            int x = cx + (int) (Math.cos(rad) * 40);
            int y = cy + (int) (Math.sin(rad) * 40);
            RenderUtils.drawTextWithShadow(mc.font, ">", x, y, color);
        }
    }
}

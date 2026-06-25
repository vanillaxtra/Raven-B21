package keystrokesmod.module.impl.render;

import keystrokesmod.utility.RenderUtils;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;

public class BreakProgress extends Module {
    private ButtonSetting bar;

    public BreakProgress() {
        super("BreakProgress", category.render, 0);
        this.registerSetting(bar = new ButtonSetting("Bar", true));
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || !bar.isToggled()) {
            return;
        }
        if (!mc.gameMode.isDestroying()) {
            return;
        }
        float progress = Math.max(0, mc.gameMode.getDestroyStage()) / 10.0f;
        String text = (int) (progress * 100) + "%";
        int x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(text) / 2;
        int y = mc.getWindow().getGuiScaledHeight() / 2 + 20;
        RenderUtils.drawTextWithShadow(mc.font, text, x, y, -1);
    }
}

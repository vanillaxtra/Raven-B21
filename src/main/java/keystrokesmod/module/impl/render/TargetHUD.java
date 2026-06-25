package keystrokesmod.module.impl.render;

import keystrokesmod.utility.RenderUtils;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.combat.KillAura;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Theme;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.LivingEntity;

public class TargetHUD extends Module {
    private SliderSetting theme;
    private ButtonSetting healthBar;
    private ButtonSetting renderEsp;
    public float posX = 500;
    public float posY = 500;

    public TargetHUD() {
        super("TargetHUD", category.render, 0);
        this.registerSetting(theme = new SliderSetting("Theme", 0, Theme.themes));
        this.registerSetting(healthBar = new ButtonSetting("Health bar", true));
        this.registerSetting(renderEsp = new ButtonSetting("Render ESP preview", false));
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        LivingEntity target = KillAura.target;
        if (target == null && mc.hitResult != null && mc.hitResult.getType() == net.minecraft.world.phys.HitResult.Type.ENTITY) {
            if (mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity living) {
                target = living;
            }
        }
        if (target == null) {
            return;
        }
        String info = target.getName().getString() + " " + Utils.getHealthStr(target, true);
        int color = Theme.getGradient((int) theme.getInput(), 0);
        RenderUtils.drawTextWithShadow(mc.font, info, posX, posY, color);
        if (healthBar.isToggled()) {
            float pct = target.getHealth() / target.getMaxHealth();
            int barWidth = mc.font.width(info);
            int barColor = Utils.getColorForHealth(pct);
            // health bar drawn as text underline placeholder
            RenderUtils.drawTextWithShadow(mc.font, "HP", posX, posY + 12, barColor);
        }
    }
}

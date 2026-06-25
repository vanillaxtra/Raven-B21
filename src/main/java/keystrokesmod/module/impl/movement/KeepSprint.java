package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.combat.KillAura;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class KeepSprint extends Module {
    public static SliderSetting slow;
    public static ButtonSetting disableWhileJump;
    public static ButtonSetting reduceReachHits;

    public KeepSprint() {
        super("KeepSprint", category.movement, 0);
        this.registerSetting(new DescriptionSetting("Default is 40% motion reduction."));
        this.registerSetting(slow = new SliderSetting("Slow %", 40.0D, 0.0D, 40.0D, 1.0D));
        this.registerSetting(disableWhileJump = new ButtonSetting("Disable while jumping", false));
        this.registerSetting(reduceReachHits = new ButtonSetting("Only reduce reach hits", false));
    }

    public static void keepSprint(Entity en) {
        if (!Mc.nullCheck()) {
            return;
        }
        boolean vanilla = false;
        if (disableWhileJump.isToggled() && !mc.player.onGround()) {
            vanilla = true;
        } else if (reduceReachHits.isToggled() && !mc.player.isCreative()) {
            double distance = -1.0;
            Vec3 eyes = mc.player.getEyePosition();
            if (ModuleManager.killAura != null && ModuleManager.killAura.isEnabled() && KillAura.target != null) {
                distance = eyes.distanceTo(KillAura.target.getEyePosition());
            } else if (ModuleManager.reach != null && ModuleManager.reach.isEnabled() && mc.hitResult != null) {
                distance = eyes.distanceTo(mc.hitResult.getLocation());
            }
            if (distance != -1.0 && distance <= 3.0) {
                vanilla = true;
            }
        }
        Vec3 v = mc.player.getDeltaMovement();
        if (vanilla) {
            mc.player.setDeltaMovement(v.x * 0.6, v.y, v.z * 0.6);
        } else {
            float mult = (100.0f - (float) slow.getInput()) / 100.0f;
            mc.player.setDeltaMovement(v.x * mult, v.y, v.z * mult);
        }
    }
}

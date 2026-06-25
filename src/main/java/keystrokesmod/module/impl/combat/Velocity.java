package keystrokesmod.module.impl.combat;

import keystrokesmod.event.LivingUpdateEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.movement.LongJump;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.BindUtil;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.world.phys.Vec3;

public class Velocity extends Module {
    public static SliderSetting horizontal;
    public static SliderSetting vertical;
    private SliderSetting chance;
    private ButtonSetting onlyWhileTargeting;
    private ButtonSetting disableS;
    public boolean disable;

    public Velocity() {
        super("Velocity", category.combat, 0);
        this.registerSetting(horizontal = new SliderSetting("Horizontal", "%", 90.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(vertical = new SliderSetting("Vertical", "%", 100.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(chance = new SliderSetting("Chance", "%", 100.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(onlyWhileTargeting = new ButtonSetting("Only while targeting", false));
        this.registerSetting(disableS = new ButtonSetting("Disable while holding S", false));
        this.closetModule = true;
    }

    @Override
    public String getInfo() {
        return (int) horizontal.getInput() + "% " + (int) vertical.getInput() + "%";
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent ev) {
        if (!Mc.nullCheck() || LongJump.stopVelocity || disable || ev.entity != mc.player) {
            return;
        }
        if (ModuleManager.antiKnockback.isEnabled()) {
            return;
        }
        if (mc.player.hurtDuration <= 0 || mc.player.hurtTime != mc.player.hurtDuration) {
            return;
        }
        if (onlyWhileTargeting.isToggled() && (mc.hitResult == null || mc.hitResult.getType() != net.minecraft.world.phys.HitResult.Type.ENTITY)) {
            return;
        }
        if (disableS.isToggled() && BindUtil.isBindDown(mc.options.keyDown.getDefaultKey().getValue())) {
            return;
        }
        if (chance.getInput() != 100 && Math.random() >= chance.getInput() / 100.0D) {
            return;
        }
        Vec3 v = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(
                v.x * horizontal.getInput() / 100.0D,
                v.y * vertical.getInput() / 100.0D,
                v.z * horizontal.getInput() / 100.0D
        );
    }
}

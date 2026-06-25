package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;

public class Speed extends Module {
    public static SliderSetting speed;
    private ButtonSetting strafeOnly;
    private ButtonSetting forwardOnly;

    public Speed() {
        super("Speed", category.movement, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 1.2D, 1.0D, 1.5D, 0.01D));
        this.registerSetting(strafeOnly = new ButtonSetting("Strafe only", false));
        this.registerSetting(forwardOnly = new ButtonSetting("Forward only", false));
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        double csp = Utils.getHorizontalSpeed();
        if (csp == 0.0D) {
            return;
        }
        if (!mc.player.onGround() || mc.player.getAbilities().flying) {
            return;
        }
        if (strafeOnly.isToggled() && Utils.getMovementSideways() == 0) {
            return;
        }
        if (forwardOnly.isToggled() && Utils.getMovementForward() <= 0) {
            return;
        }
        if (mc.player.hurtTime == mc.player.hurtDuration && mc.player.hurtDuration > 0) {
            return;
        }
        if (Utils.jumpDown()) {
            return;
        }
        double val = speed.getInput() - (speed.getInput() - 1.0D) * 0.5D;
        Utils.ss(csp * val, true);
    }
}

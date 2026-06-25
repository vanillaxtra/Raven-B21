package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;

public class BHop extends Module {
    public static SliderSetting speed;
    private ButtonSetting lowHop;

    public BHop() {
        super("BHop", category.movement, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 1.0, 0.5, 2.0, 0.05));
        this.registerSetting(lowHop = new ButtonSetting("Low hop", false));
    }

    public void onUpdate() {
        if (!Mc.nullCheck() || Utils.getMovementForward() == 0 && Utils.getMovementSideways() == 0) {
            return;
        }
        if (mc.player.onGround()) {
            mc.player.jumpFromGround();
            if (lowHop.isToggled()) {
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().multiply(1, 0.4, 1));
            }
        }
        double horiz = Utils.getHorizontalSpeed();
        if (horiz > 0) {
            Utils.ss(horiz * speed.getInput(), false);
        }
    }
}

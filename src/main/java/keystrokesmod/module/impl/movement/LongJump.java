package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;

public class LongJump extends Module {
    public static boolean stopVelocity;
    private SliderSetting mode;
    private ButtonSetting disableTimer;
    private String[] modes = new String[]{"Boost", "Flat"};
    private boolean jumped;

    public LongJump() {
        super("LongJump", category.movement, 0);
        this.registerSetting(mode = new SliderSetting("Mode", 0, modes));
        this.registerSetting(disableTimer = new ButtonSetting("Disable timer", true));
    }

    public void onEnable() {
        jumped = false;
        stopVelocity = true;
    }

    public void onDisable() {
        stopVelocity = false;
        jumped = false;
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        if (mc.player.onGround() && !jumped) {
            mc.player.jumpFromGround();
            jumped = true;
            if ((int) mode.getInput() == 0) {
                Utils.ss(1.8, true);
            } else {
                Utils.ss(2.2, false);
            }
        }
        if (mc.player.onGround() && jumped) {
            disable();
        }
    }
}

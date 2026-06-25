package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class Fly extends Module {
    public static SliderSetting speed;
    private ButtonSetting antiKick;

    public Fly() {
        super("Fly", category.movement, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 1.0, 0.1, 5.0, 0.1));
        this.registerSetting(antiKick = new ButtonSetting("Anti kick", true));
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlyingSpeed((float) (speed.getInput() * 0.05f));
        if (antiKick.isToggled() && mc.player.tickCount % 40 == 0) {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0, -0.04, 0));
        }
    }

    public void onDisable() {
        if (Mc.nullCheck() && !mc.player.isCreative()) {
            mc.player.getAbilities().flying = false;
        }
    }
}

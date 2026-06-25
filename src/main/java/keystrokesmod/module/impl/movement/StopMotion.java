package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.phys.Vec3;

public class StopMotion extends Module {
    private ButtonSetting stopX;
    private ButtonSetting stopY;
    private ButtonSetting stopZ;

    public StopMotion() {
        super("Stop Motion", category.movement, 0);
        this.registerSetting(stopX = new ButtonSetting("Stop X", true));
        this.registerSetting(stopY = new ButtonSetting("Stop Y", true));
        this.registerSetting(stopZ = new ButtonSetting("Stop Z", true));
    }

    public void onEnable() {
        if (!Mc.nullCheck()) {
            return;
        }
        Vec3 v = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(
                stopX.isToggled() ? 0 : v.x,
                stopY.isToggled() ? 0 : v.y,
                stopZ.isToggled() ? 0 : v.z
        );
        this.disable();
    }
}

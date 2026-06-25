package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.phys.Vec3;

public class Tower extends Module {
    private SliderSetting speed;
    private ButtonSetting jump;

    public Tower() {
        super("Tower", category.player, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 0.42, 0.1, 1.0, 0.01));
        this.registerSetting(jump = new ButtonSetting("Jump", true));
    }

    public static boolean towering;

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck()) {
            towering = false;
            return;
        }
        towering = true;
        if (jump.isToggled() && mc.player.onGround()) {
            mc.player.jumpFromGround();
        }
        mc.player.setDeltaMovement(new Vec3(0, speed.getInput(), 0));
    }

    public void onDisable() {
        towering = false;
    }
}

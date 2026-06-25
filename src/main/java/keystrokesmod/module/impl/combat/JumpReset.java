package keystrokesmod.module.impl.combat;

import keystrokesmod.event.JumpEvent;
import keystrokesmod.event.PreInputEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class JumpReset extends Module {
    private SliderSetting chance;
    private SliderSetting motion;
    private boolean jump;

    public JumpReset() {
        super("Jump Reset", category.combat);
        this.registerSetting(chance = new SliderSetting("Chance", "%", 80, 0, 100, 1));
        this.registerSetting(motion = new SliderSetting("Jump motion", 0.42, 0, 1, 0.01));
        this.closetModule = true;
    }

    public void onDisable() {
        jump = false;
    }

    @SubscribeEvent
    public void onPreInput(PreInputEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        if (chance.getInput() == 0) {
            return;
        }
        if (mc.player.hurtDuration <= 0) {
            jump = false;
            return;
        }
        if (mc.player.hurtTime == mc.player.hurtDuration) {
            jump = true;
        }
        if (!jump || mc.player.hurtTime == 0) {
            jump = false;
            return;
        }
        if (chance.getInput() != 100.0D && Math.random() >= chance.getInput() / 100.0D) {
            return;
        }
        if (jump && mc.player.onGround()) {
            mc.player.jumpFromGround();
            jump = false;
        }
    }

    @SubscribeEvent
    public void onJump(JumpEvent e) {
        if (!Mc.nullCheck() || !jump) {
            return;
        }
        if (motion.getInput() != 0.42) {
            e.setMotionY((float) motion.getInput());
        }
        jump = false;
    }
}

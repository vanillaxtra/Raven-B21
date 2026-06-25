package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.mixin.impl.accessor.IAccessorLivingEntity;
import keystrokesmod.utility.Mc;

public class DelayRemover extends Module {
    public ButtonSetting oldReg;
    public ButtonSetting removeJumpTicks;

    public DelayRemover() {
        super("Delay Remover", category.player, 0);
        this.registerSetting(oldReg = new ButtonSetting("1.7 hitreg", true));
        this.registerSetting(removeJumpTicks = new ButtonSetting("Remove jump ticks", false));
        this.closetModule = true;
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck()) {
            return;
        }
        if (oldReg.isToggled()) {
            mc.options.keyAttack.setDown(true);
        }
        if (removeJumpTicks.isToggled()) {
            ((IAccessorLivingEntity) mc.player).setNoJumpDelay(0);
        }
    }
}

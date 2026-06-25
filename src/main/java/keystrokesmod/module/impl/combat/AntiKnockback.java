package keystrokesmod.module.impl.combat;

import keystrokesmod.event.PreUpdateEvent;
import keystrokesmod.event.ReceivePacketEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.movement.LongJump;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.BindUtil;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import org.lwjgl.glfw.GLFW;

public class AntiKnockback extends Module {
    private SliderSetting mode;
    private SliderSetting horizontal;
    private SliderSetting vertical;
    private ButtonSetting disableInLobby;
    private ButtonSetting cancelExplosion;
    public String[] modes = new String[]{"Cancel", "Hypixel"};
    public boolean disable;

    public AntiKnockback() {
        super("AntiKnockback", category.combat);
        this.registerSetting(new DescriptionSetting("Overrides Velocity."));
        this.registerSetting(mode = new SliderSetting("Mode", 0, modes));
        this.registerSetting(horizontal = new SliderSetting("Horizontal", 0.0, 0.0, 100.0, 1.0));
        this.registerSetting(vertical = new SliderSetting("Vertical", 0.0, 0.0, 100.0, 1.0));
        this.registerSetting(disableInLobby = new ButtonSetting("Disable in lobby", false));
        this.registerSetting(cancelExplosion = new ButtonSetting("Cancel explosion", true));
    }

    public void guiUpdate() {
        horizontal.setVisible(mode.getInput() == 0, this);
        vertical.setVisible(mode.getInput() == 0, this);
        disableInLobby.setVisible(mode.getInput() == 0, this);
        cancelExplosion.setVisible(mode.getInput() == 0, this);
    }

    @SubscribeEvent
    public void onPreUpdate(PreUpdateEvent e) {
        if (mode.getInput() == 1) {
            horizontal.setValue(0);
            vertical.setValue(100);
            disableInLobby.setToggled(true);
            cancelExplosion.setToggled(true);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent e) {
        if (!Mc.nullCheck() || LongJump.stopVelocity || e.isCanceled() || disable) {
            return;
        }
        if (disableInLobby.isToggled() && Utils.isLobby()) {
            return;
        }
        if (BindUtil.isBindDown(GLFW.GLFW_MOUSE_BUTTON_LEFT + 1000) && horizontal.getInput() == 0 && vertical.getInput() == 0) {
            // allow lmb boost path from original
        }
        if (e.getPacket() instanceof ClientboundSetEntityMotionPacket vel && vel.getId() == mc.player.getId()) {
            if (horizontal.getInput() == 0 && vertical.getInput() == 0) {
                e.setCanceled(true);
            }
        }
        if (cancelExplosion.isToggled() && e.getPacket() instanceof ClientboundExplodePacket) {
            e.setCanceled(true);
        }
    }
}

package keystrokesmod.module.impl.player;

import keystrokesmod.event.SendPacketEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFall extends Module {
    private SliderSetting mode;
    private String[] modes = new String[]{"Packet", "Ground spoof"};

    public NoFall() {
        super("NoFall", category.player, 0);
        this.registerSetting(mode = new SliderSetting("Mode", 0, modes));
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent e) {
        if (!Mc.nullCheck() || mc.player.fallDistance <= 2.5f) {
            return;
        }
        if ((int) mode.getInput() == 0 && e.getPacket() instanceof ServerboundMovePlayerPacket) {
            // mixin handles on-ground spoof for move packets
        }
    }

    public void onUpdate() {
        if (!Mc.nullCheck() || (int) mode.getInput() != 1) {
            return;
        }
        if (mc.player.fallDistance > 2.5f) {
            mc.player.setOnGround(true);
        }
    }
}

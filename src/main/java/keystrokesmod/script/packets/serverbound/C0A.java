package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;

public class C0A extends CPacket {
    public C0A(ServerboundSwingPacket packet) {
        super(packet);
    }

    public C0A() {
        super(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
    }
}

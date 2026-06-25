package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;

public class C09 extends CPacket {
    public int slot;

    public C09(int slot) {
        super(null);
        this.slot = slot;
    }

    public C09(ServerboundSetCarriedItemPacket packet, boolean identifier) {
        super(packet);
        this.slot = packet.getSlot();
    }

    @Override
    public ServerboundSetCarriedItemPacket convert() {
        return new ServerboundSetCarriedItemPacket(this.slot);
    }
}

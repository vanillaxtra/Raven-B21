package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.common.ServerboundPongPacket;

public class C0F extends CPacket {
    public int windowId;
    public short uid;

    public C0F(int windowId, short uid) {
        super(null);
        this.windowId = windowId;
        this.uid = uid;
    }

    public C0F(ServerboundPongPacket packet) {
        super(packet);
        this.windowId = 0;
        this.uid = 0;
    }

    @Override
    public ServerboundPongPacket convert() {
        return new ServerboundPongPacket(this.uid & 0xFFFF);
    }
}

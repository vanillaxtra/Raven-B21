package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;

public class C0D extends CPacket {
    public int windowId;

    public C0D(int windowId) {
        super(null);
        this.windowId = windowId;
    }

    public C0D(ServerboundContainerClosePacket packet) {
        super(packet);
        this.windowId = packet.containerId();
    }

    @Override
    public ServerboundContainerClosePacket convert() {
        return new ServerboundContainerClosePacket(this.windowId);
    }
}

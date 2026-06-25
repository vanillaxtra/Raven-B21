package keystrokesmod.event;

import net.minecraft.network.protocol.Packet;

public class ReceivePacketEvent extends CancelableEvent {
    private final Packet<?> packet;

    public ReceivePacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

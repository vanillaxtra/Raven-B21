package keystrokesmod.event;

import net.minecraft.network.protocol.Packet;

public class SendPacketEvent extends CancelableEvent {
    private final Packet<?> packet;

    public SendPacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

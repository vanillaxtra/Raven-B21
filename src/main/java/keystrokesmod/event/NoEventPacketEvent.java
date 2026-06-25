package keystrokesmod.event;

import net.minecraft.network.protocol.Packet;

public class NoEventPacketEvent extends CancelableEvent {
    private final Packet<?> packet;

    public NoEventPacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

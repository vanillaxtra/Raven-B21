package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.Packet;

public class CPacket {
    public String name;
    public Packet<?> packet;

    public CPacket(Packet<?> packet) {
        if (packet == null) {
            return;
        }
        this.packet = packet;
        this.name = packet.getClass().getSimpleName();
    }

    public Packet<?> convert() {
        return packet;
    }
}

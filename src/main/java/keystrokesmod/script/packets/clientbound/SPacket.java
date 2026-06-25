package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.Packet;

public class SPacket {
    public String name;
    public Packet<?> packet;

    public SPacket(Packet<?> packet) {
        this.packet = packet;
        this.name = packet.getClass().getSimpleName();
    }
}

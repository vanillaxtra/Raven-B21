package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;

public class S0B extends SPacket {
    public int entityId;
    public int type;

    public S0B(ClientboundAnimatePacket packet) {
        super(packet);
        this.entityId = 0;
        this.type = 0;
    }

    public S0B(int entityId, int type) {
        super(new ClientboundAnimatePacket(entityId, (byte) type));
        this.entityId = entityId;
        this.type = type;
    }
}

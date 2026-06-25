package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public class S0B extends SPacket {
    public int entityId;
    public int type;

    public S0B(ClientboundAnimatePacket packet) {
        super(packet);
        this.entityId = 0;
        this.type = 0;
    }

    public S0B(int entityId, int type) {
        super(new ClientboundSetEntityMotionPacket(entityId, new net.minecraft.world.phys.Vec3(0, 0, 0)));
        this.entityId = entityId;
        this.type = type;
    }
}

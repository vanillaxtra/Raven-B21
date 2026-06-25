package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public class S12 extends SPacket {
    public int entityId;
    public ScriptVec3 motion;

    public S12(ClientboundSetEntityMotionPacket packet) {
        super(packet);
        this.entityId = packet.getId();
        this.motion = new ScriptVec3(0, 0, 0);
    }

    public S12(int entityId, ScriptVec3 motion) {
        super(new ClientboundSetEntityMotionPacket(entityId, new net.minecraft.world.phys.Vec3(0, 0, 0)));
        this.entityId = entityId;
        this.motion = motion;
    }
}

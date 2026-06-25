package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.core.particles.ParticleTypes;

public class S2A extends SPacket {
    public String type;
    public ScriptVec3 position;
    public ScriptVec3 offset;
    public float speed;
    public int count;
    public boolean longDistance;
    public int[] args;

    public S2A(ClientboundLevelParticlesPacket packet) {
        super(packet);
        this.type = "";
        this.longDistance = false;
        this.position = new ScriptVec3(0, 0, 0);
        this.offset = new ScriptVec3(0, 0, 0);
        this.speed = 0;
        this.count = 0;
        this.args = new int[0];
    }

    public S2A(String type, boolean longDistance, ScriptVec3 position, ScriptVec3 offset, float speed, int count, int[] args) {
        super(new ClientboundLevelParticlesPacket(ParticleTypes.FLAME, longDistance, false, position.x, position.y, position.z, (float) offset.x, (float) offset.y, (float) offset.z, speed, count));
        this.type = type;
        this.longDistance = longDistance;
        this.position = position;
        this.offset = offset;
        this.speed = speed;
        this.count = count;
        this.args = args;
    }
}

package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;

import java.util.ArrayList;
import java.util.List;

public class S27 extends SPacket {
    public float strength;
    public ScriptVec3 position;
    public ScriptVec3 motion;
    public List<ScriptVec3> affectedBlockPositions;

    public S27(ClientboundExplodePacket packet) {
        super(packet);
        this.strength = 0;
        this.position = new ScriptVec3(0, 0, 0);
        this.motion = new ScriptVec3(0, 0, 0);
        this.affectedBlockPositions = new ArrayList<>();
    }

    public S27(float strength, ScriptVec3 position, ScriptVec3 motion, List<ScriptVec3> affectedBlockPositions) {
        super(new ClientboundExplodePacket(new net.minecraft.world.phys.Vec3(0, 0, 0), 0, 0, java.util.Optional.empty(), net.minecraft.core.particles.ParticleTypes.EXPLOSION, net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE, net.minecraft.util.random.WeightedList.of()));
        this.strength = strength;
        this.position = position;
        this.motion = motion;
        this.affectedBlockPositions = affectedBlockPositions;
    }
}

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
        super(new ClientboundExplodePacket(position.x, position.y, position.z, strength, List.of(), motion.x, motion.y, motion.z));
        this.strength = strength;
        this.position = position;
        this.motion = motion;
        this.affectedBlockPositions = affectedBlockPositions;
    }
}

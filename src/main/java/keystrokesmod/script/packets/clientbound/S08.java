package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;

import java.util.HashSet;
import java.util.Set;

public class S08 extends SPacket {
    public ScriptVec3 position;
    public float yaw;
    public float pitch;
    public Set<String> enumFlags;

    public S08(ClientboundPlayerPositionPacket packet) {
        super(packet);
        this.position = new ScriptVec3(0, 0, 0);
        this.yaw = 0;
        this.pitch = 0;
        this.enumFlags = new HashSet<>();
    }

    public S08(ScriptVec3 position, float yaw, float pitch, Set<String> enumFlags) {
        super(new ClientboundPlayerPositionPacket(0, new net.minecraft.world.entity.PositionMoveRotation(net.minecraft.world.phys.Vec3.ZERO, net.minecraft.world.phys.Vec3.ZERO, 0, 0), java.util.Set.of()));
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.enumFlags = enumFlags;
    }
}

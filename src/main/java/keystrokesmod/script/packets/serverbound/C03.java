package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class C03 extends CPacket {
    public ScriptVec3 position;
    public float yaw;
    public float pitch;
    public boolean ground;

    public C03(ServerboundMovePlayerPacket packet, byte f1, byte f2, byte f3, byte f4, byte f5, byte f6) {
        super(packet);
        this.ground = false;
    }

    public C03(boolean ground) {
        super(new ServerboundMovePlayerPacket.StatusOnly(ground, false));
        this.ground = ground;
    }

    public C03(ScriptVec3 position, boolean ground) {
        super(new ServerboundMovePlayerPacket.Pos(position.x, position.y, position.z, ground, false));
        this.position = position;
        this.ground = ground;
    }

    public C03(float yaw, float pitch, boolean ground) {
        super(new ServerboundMovePlayerPacket.Rot(yaw, pitch, ground, false));
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
    }

    public C03(ScriptVec3 position, float yaw, float pitch, boolean ground) {
        super(new ServerboundMovePlayerPacket.PosRot(position.x, position.y, position.z, yaw, pitch, ground, false));
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
    }
}

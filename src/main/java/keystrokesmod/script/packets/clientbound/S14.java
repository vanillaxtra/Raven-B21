package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;

public class S14 extends SPacket {
    public int entityId;
    public byte posX;
    public byte posY;
    public byte posZ;
    public byte yaw;
    public byte pitch;
    public boolean onGround;
    public boolean rotating;

    public S14(ClientboundMoveEntityPacket packet) {
        super(packet);
        this.entityId = 0;
        this.posX = 0;
        this.posY = 0;
        this.posZ = 0;
        this.yaw = 0;
        this.pitch = 0;
        this.onGround = false;
        this.rotating = false;
    }

    public S14(int entityId, byte posX, byte posY, byte posZ, byte yaw, byte pitch, boolean onGround) {
        super(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entityId, new net.minecraft.world.phys.Vec3(0, 0, 0)));
        this.entityId = entityId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public S14(int entityId, byte posX, byte posY, byte posZ, boolean onGround) {
        super(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entityId, new net.minecraft.world.phys.Vec3(0, 0, 0)));
        this.entityId = entityId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.onGround = onGround;
    }

    public S14(int entityId, byte yaw, byte pitch, boolean onGround) {
        super(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entityId, new net.minecraft.world.phys.Vec3(0, 0, 0)));
        this.entityId = entityId;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
}

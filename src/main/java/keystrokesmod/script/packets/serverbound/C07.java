package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.ScriptVec3;
import keystrokesmod.utility.Utils;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class C07 extends CPacket {
    public ScriptVec3 position;
    public String status;
    public String facing;

    public C07(ScriptVec3 position, String status, String facing) {
        super(null);
        this.position = position;
        this.status = status;
        this.facing = facing;
    }

    public C07(ServerboundPlayerActionPacket packet) {
        super(packet);
        this.position = new ScriptVec3(packet.getPos());
        this.status = packet.getAction().name();
        this.facing = packet.getDirection().name();
    }

    @Override
    public ServerboundPlayerActionPacket convert() {
        if (this.packet instanceof ServerboundPlayerActionPacket actionPacket) {
            return actionPacket;
        }
        ServerboundPlayerActionPacket.Action action = Utils.getEnum(ServerboundPlayerActionPacket.Action.class, this.status);
        Direction direction = Utils.getEnum(Direction.class, this.facing);
        if (action == null) {
            action = ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK;
        }
        if (direction == null) {
            direction = Direction.UP;
        }
        if (this.position == null) {
            return new ServerboundPlayerActionPacket(action, BlockPos.ZERO, direction, 0);
        }
        return new ServerboundPlayerActionPacket(action, ScriptVec3.getBlockPos(this.position), direction, 0);
    }
}

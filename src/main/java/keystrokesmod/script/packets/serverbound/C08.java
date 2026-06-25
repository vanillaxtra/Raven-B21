package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.ItemStack;
import keystrokesmod.script.classes.ScriptVec3;
import keystrokesmod.utility.Mc;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;

public class C08 extends CPacket {
    public ItemStack itemStack;
    public ScriptVec3 position;
    public int direction;
    public ScriptVec3 offset;

    public C08(ItemStack itemStack, ScriptVec3 position, int direction, ScriptVec3 offset) {
        super(null);
        this.itemStack = itemStack;
        this.position = position;
        this.direction = direction;
        this.offset = offset;
    }

    public C08(ServerboundUseItemOnPacket packet) {
        super(packet);
        this.itemStack = new ItemStack(Mc.player().getItemInHand(packet.getHand()), (byte) 0);
        this.position = new ScriptVec3(0, 0, 0);
        this.direction = 0;
        this.offset = new ScriptVec3(0, 0, 0);
    }

    public C08(ServerboundUseItemPacket packet) {
        super(packet);
        this.itemStack = new ItemStack(Mc.player().getItemInHand(packet.getHand()), (byte) 0);
    }

    @Override
    public ServerboundUseItemOnPacket convert() {
        if (this.packet instanceof ServerboundUseItemOnPacket usePacket) {
            return usePacket;
        }
        Direction dir = Direction.from3DDataValue(this.direction);
        var pos = this.position == null ? net.minecraft.core.BlockPos.ZERO : ScriptVec3.getBlockPos(this.position);
        var hitPos = new net.minecraft.world.phys.Vec3(
                pos.getX() + (this.offset != null ? this.offset.x : 0.5),
                pos.getY() + (this.offset != null ? this.offset.y : 0.5),
                pos.getZ() + (this.offset != null ? this.offset.z : 0.5));
        return new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(hitPos, dir, pos, false), 0);
    }
}

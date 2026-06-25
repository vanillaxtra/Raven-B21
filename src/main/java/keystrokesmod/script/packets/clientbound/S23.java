package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.Block;
import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.core.BlockPos;

public class S23 extends SPacket {
    public ScriptVec3 position;
    public Block block;

    public S23(ClientboundBlockUpdatePacket packet, byte f) {
        super(packet);
        this.position = new ScriptVec3(0, 0, 0);
        this.block = new Block(this.position);
    }

    public S23(ScriptVec3 position) {
        super(new ClientboundBlockUpdatePacket(BlockPos.ZERO, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()));
        this.position = position;
        this.block = new Block(position);
    }
}

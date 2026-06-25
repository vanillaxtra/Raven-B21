package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.Block;
import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.core.BlockPos;

public class S25 extends SPacket {
    public int entityId;
    public Block block;
    public int progress;

    public S25(ClientboundBlockDestructionPacket packet) {
        super(packet);
        this.entityId = packet.getId();
        this.block = new Block(new ScriptVec3(0, 0, 0));
        this.progress = packet.getProgress();
    }

    public S25(int entityId, Block block, int progress) {
        super(new ClientboundBlockDestructionPacket(entityId, BlockPos.ZERO, progress));
        this.entityId = entityId;
        this.block = block;
        this.progress = progress;
    }

    public S25(int entityId, ScriptVec3 position, int progress) {
        super(new ClientboundBlockDestructionPacket(entityId, ScriptVec3.getBlockPos(position), progress));
        this.entityId = entityId;
        this.block = new Block(position);
        this.progress = progress;
    }
}

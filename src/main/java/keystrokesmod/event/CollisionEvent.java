package keystrokesmod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

public class CollisionEvent {
    public final BlockPos blockPos;
    public final Block block;
    public AABB boundingBox;

    public CollisionEvent(BlockPos position, Block block, AABB boundingBox) {
        this.blockPos = position;
        this.block = block;
        this.boundingBox = boundingBox;
    }
}

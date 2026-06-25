package keystrokesmod.utility;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public final class BlockUtils {
    private BlockUtils() {}

    public static Block getBlock(BlockPos pos) {
        if (!Mc.nullCheck()) {
            return net.minecraft.world.level.block.Blocks.AIR;
        }
        return Mc.level().getBlockState(pos).getBlock();
    }
}

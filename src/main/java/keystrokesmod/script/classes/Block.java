package keystrokesmod.script.classes;

import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Block {
    public String type;
    public String name;
    public boolean interactable;
    public int variant;
    public double height;
    public double width;
    public double length;
    public double x;
    public double y;
    public double z;

    public Block(net.minecraft.world.level.block.Block block, BlockPos blockPos) {
        BlockState state = getState(blockPos);
        fillFromBlock(block, state, blockPos);
    }

    public Block(BlockState state, BlockPos blockPos) {
        fillFromBlock(state.getBlock(), state, blockPos);
    }

    public Block(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = getState(pos);
        fillFromBlock(state.getBlock(), state, pos);
    }

    protected Block(String name, ScriptVec3 position) {
        this(Utils.getBlockFromName(name), ScriptVec3.getBlockPos(position));
    }

    public Block(String name) {
        this(name, new ScriptVec3(-1, -1, -1));
    }

    public Block(ScriptVec3 position) {
        BlockPos pos = ScriptVec3.getBlockPos(position);
        BlockState state = getState(pos);
        fillFromBlock(state.getBlock(), state, pos);
    }

    private void fillFromBlock(net.minecraft.world.level.block.Block block, BlockState state, BlockPos blockPos) {
        this.type = block.getClass().getSimpleName();
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        this.name = id == null ? "" : id.getPath();
        this.interactable = isInteractable(block);
        this.variant = 0;
        VoxelShape shape = state.getShape(getWorld(), blockPos);
        AABB box = shape.isEmpty() ? new AABB(0, 0, 0, 1, 1, 1) : shape.bounds();
        this.height = box.maxY - box.minY;
        this.width = box.maxX - box.minX;
        this.length = box.maxZ - box.minZ;
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    private static BlockState getState(BlockPos blockPos) {
        if (!Mc.nullCheck()) {
            return Blocks.AIR.defaultBlockState();
        }
        return Mc.level().getBlockState(blockPos);
    }

    private static BlockGetter getWorld() {
        return Mc.nullCheck() ? Mc.level() : EmptyBlockGetter.INSTANCE;
    }

    private static boolean isInteractable(net.minecraft.world.level.block.Block block) {
        return block instanceof AbstractFurnaceBlock
                || block instanceof TrapDoorBlock
                || block instanceof DoorBlock
                || block instanceof EntityBlock
                || block instanceof JukeboxBlock
                || block instanceof FenceGateBlock
                || block instanceof ChestBlock
                || block instanceof EnderChestBlock
                || block instanceof EnchantingTableBlock
                || block instanceof BrewingStandBlock
                || block instanceof BedBlock
                || block instanceof DropperBlock
                || block instanceof DispenserBlock
                || block instanceof HopperBlock
                || block instanceof AnvilBlock
                || block instanceof NoteBlock
                || block instanceof CraftingTableBlock;
    }
}
package keystrokesmod.script.classes;

public class TileEntity {
    public String type = "";
    public int x;
    public int y;
    public int z;

    public TileEntity(net.minecraft.world.level.block.entity.BlockEntity blockEntity) {
        if (blockEntity == null) {
            return;
        }
        this.type = blockEntity.getClass().getSimpleName();
        this.x = blockEntity.getBlockPos().getX();
        this.y = blockEntity.getBlockPos().getY();
        this.z = blockEntity.getBlockPos().getZ();
    }
}

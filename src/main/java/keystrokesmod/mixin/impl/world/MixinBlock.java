package keystrokesmod.mixin.impl.world;

import keystrokesmod.event.CollisionEvent;
import keystrokesmod.event.RavenEventBus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape shape = cir.getReturnValue();
        if (shape.isEmpty()) {
            return;
        }

        AABB box = shape.bounds().move(pos);
        CollisionEvent event = new CollisionEvent(pos, (Block) (Object) this, box);
        RavenEventBus.post(event);

        if (event.boundingBox == null) {
            cir.setReturnValue(Shapes.empty());
            return;
        }

        AABB local = event.boundingBox.move(-pos.getX(), -pos.getY(), -pos.getZ());
        cir.setReturnValue(Shapes.create(local));
    }
}

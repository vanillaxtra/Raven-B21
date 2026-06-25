package keystrokesmod.mixin.impl.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface IAccessorLivingEntity {
    @Accessor("noJumpDelay")
    void setNoJumpDelay(int delay);

    @Accessor("noJumpDelay")
    int getNoJumpDelay();
}

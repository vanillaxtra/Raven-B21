package keystrokesmod.mixin.impl.accessor;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface IAccessorCamera {
    @Invoker("getMaxZoom")
    float invokeGetMaxZoom(float distance);
}

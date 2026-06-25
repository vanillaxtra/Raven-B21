package keystrokesmod.mixin.impl.accessor;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface IAccessorGameRenderer {
    @Invoker("updateCamera")
    void callUpdateCamera(DeltaTracker deltaTracker);

    @Accessor("mainCamera")
    Camera getCamera();
}

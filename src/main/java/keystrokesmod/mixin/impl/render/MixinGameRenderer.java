package keystrokesmod.mixin.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.mixin.impl.accessor.IAccessorCamera;
import keystrokesmod.module.ModuleManager;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Redirect(method = "pick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;clipToSpace(F)F"))
    private float onClipToSpace(Camera camera, float desiredCameraDistance) {
        if (ModuleManager.noCameraClip != null && ModuleManager.noCameraClip.isEnabled()) {
            if (ModuleManager.extendCamera != null && ModuleManager.extendCamera.isEnabled()) {
                return (float) ModuleManager.extendCamera.distance.getInput();
            }
            return 4.0F;
        }
        return ((IAccessorCamera) camera).invokeGetMaxZoom(desiredCameraDistance);
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void onRenderLevelPost(DeltaTracker deltaTracker, CallbackInfo ci) {
        RavenEventBus.post(new RenderWorldLastEvent(deltaTracker.getGameTimeDeltaPartialTick(false)));
    }
}

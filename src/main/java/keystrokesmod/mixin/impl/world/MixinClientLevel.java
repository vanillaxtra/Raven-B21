package keystrokesmod.mixin.impl.world;

import keystrokesmod.event.EntityJoinWorldEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.module.ModuleManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    @Inject(method = "addEntity", at = @At("TAIL"))
    private void onAddEntity(Entity entity, CallbackInfo ci) {
        RavenEventBus.post(new EntityJoinWorldEvent(entity));
    }

    @Inject(method = "getRainGradient", at = @At("RETURN"), cancellable = true)
    private void onGetRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (ModuleManager.weather != null && ModuleManager.weather.isEnabled() && ModuleManager.weather.rain.isToggled()) {
            cir.setReturnValue(1.0F);
        }
    }

    @Inject(method = "getThunderGradient", at = @At("RETURN"), cancellable = true)
    private void onGetThunderGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (ModuleManager.weather != null && ModuleManager.weather.isEnabled() && ModuleManager.weather.lightning.getInput() > 0) {
            cir.setReturnValue((float) ModuleManager.weather.lightning.getInput());
        }
    }

    @Inject(method = "getTimeOfDay", at = @At("RETURN"), cancellable = true)
    private void onGetTimeOfDay(CallbackInfoReturnable<Long> cir) {
        if (ModuleManager.weather != null && ModuleManager.weather.isEnabled()) {
            cir.setReturnValue((long) Mth.clamp(ModuleManager.weather.time.getInput() * 1000.0D, 0.0D, 23999.0D));
        }
    }
}

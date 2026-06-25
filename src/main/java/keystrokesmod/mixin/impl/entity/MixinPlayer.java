package keystrokesmod.mixin.impl.entity;

import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.movement.KeepSprint;
import keystrokesmod.utility.Mc;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V", shift = At.Shift.AFTER))
    private void onAttackKnockback(Entity target, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }

        if (ModuleManager.keepSprint != null && ModuleManager.keepSprint.isEnabled()) {
            KeepSprint.keepSprint(livingTarget);
        } else {
            self.setDeltaMovement(self.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            self.setSprinting(false);
        }
    }

    @Inject(method = "isBlocking", at = @At("RETURN"), cancellable = true)
    private void onIsBlocking(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Mc.player();
        if (ModuleManager.killAura != null
                && ModuleManager.killAura.isEnabled()
                && player != null
                && (Object) this == player) {
            cir.setReturnValue(true);
        }
    }
}

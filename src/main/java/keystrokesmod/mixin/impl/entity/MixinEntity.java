package keystrokesmod.mixin.impl.entity;

import keystrokesmod.event.RavenEventBus;
import keystrokesmod.event.StrafeEvent;
import keystrokesmod.utility.Mc;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public abstract float getYRot();

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void onUpdateVelocity(float speed, Vec3 movementInput, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        float strafe = (float) movementInput.x;
        float forward = (float) movementInput.z;
        float friction = speed;
        float yaw = this.getYRot();

        if (self == Mc.player()) {
            StrafeEvent strafeEvent = new StrafeEvent(strafe, forward, friction, yaw);
            RavenEventBus.post(strafeEvent);

            strafe = strafeEvent.getStrafe();
            forward = strafeEvent.getForward();
            friction = strafeEvent.getFriction();
            yaw = strafeEvent.getYaw();
        }

        float f = strafe * strafe + forward * forward;
        if (f < 1.0E-4F) {
            self.setDeltaMovement(0.0D, self.getDeltaMovement().y, 0.0D);
            ci.cancel();
            return;
        }

        f = Mth.sqrt(f);
        if (f < 1.0F) {
            f = 1.0F;
        }

        f = friction / f;
        strafe *= f;
        forward *= f;
        float sin = Mth.sin(yaw * ((float) Math.PI / 180.0F));
        float cos = Mth.cos(yaw * ((float) Math.PI / 180.0F));
        Vec3 velocity = self.getDeltaMovement();
        self.setDeltaMovement(
                velocity.x + strafe * cos - forward * sin,
                velocity.y,
                velocity.z + forward * cos + strafe * sin
        );
        ci.cancel();
    }
}

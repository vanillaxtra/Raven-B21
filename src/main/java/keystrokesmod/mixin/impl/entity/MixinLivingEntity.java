package keystrokesmod.mixin.impl.entity;

import keystrokesmod.event.JumpEvent;
import keystrokesmod.event.PreMotionEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.module.impl.client.Settings;
import keystrokesmod.utility.RotationUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow
    public float yHeadRot;

    @Shadow
    public float yBodyRot;

    @Shadow
    public float attackAnim;

    @Shadow
    protected abstract float getJumpPower();

    @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
    private void onTurnHead(float bodyRotation, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        float rotationYaw = self.getYRot();

        if (Settings.fullBody != null && Settings.rotateBody != null
                && !Settings.fullBody.isToggled() && Settings.rotateBody.isToggled()
                && self instanceof LocalPlayer && PreMotionEvent.setRenderYaw()) {
            if (this.attackAnim > 0.0F) {
                bodyRotation = RotationUtils.renderYaw;
            }
            rotationYaw = RotationUtils.renderYaw;
            this.yHeadRot = RotationUtils.renderYaw;
        }

        float delta = Mth.wrapDegrees(bodyRotation - this.yBodyRot);
        this.yBodyRot += delta * 0.3F;
        float headDelta = Mth.wrapDegrees(rotationYaw - this.yBodyRot);
        boolean flipped = headDelta < -90.0F || headDelta >= 90.0F;

        if (headDelta < -75.0F) {
            headDelta = -75.0F;
        }
        if (headDelta >= 75.0F) {
            headDelta = 75.0F;
        }

        this.yBodyRot = rotationYaw - headDelta;
        if (headDelta * headDelta > 2500.0F) {
            this.yBodyRot += headDelta * 0.2F;
        }

        if (flipped) {
            bodyRotation *= -1.0F;
        }

        ci.cancel();
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void onJump(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        JumpEvent jumpEvent = new JumpEvent(this.getJumpPower(), self.getYRot(), self.isSprinting());
        RavenEventBus.post(jumpEvent);

        if (jumpEvent.isCanceled()) {
            ci.cancel();
            return;
        }

        if (PreMotionEvent.setRenderYaw()) {
            jumpEvent.setYaw(RotationUtils.renderYaw);
        }

        var velocity = self.getDeltaMovement();
        self.setDeltaMovement(velocity.x, jumpEvent.getMotionY(), velocity.z);

        if (self.hasEffect(MobEffects.JUMP_BOOST)) {
            double boost = (self.getEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
            velocity = self.getDeltaMovement();
            self.setDeltaMovement(velocity.x, velocity.y + boost, velocity.z);
        }

        if (jumpEvent.applySprint()) {
            float yaw = jumpEvent.getYaw() * ((float) Math.PI / 180.0F);
            velocity = self.getDeltaMovement();
            self.setDeltaMovement(
                    velocity.x + -Mth.sin(yaw) * 0.2F,
                    velocity.y,
                    velocity.z + Mth.cos(yaw) * 0.2F
            );
        }

        ci.cancel();
    }
}

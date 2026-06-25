package keystrokesmod.mixin.impl.entity;

import keystrokesmod.event.PostMotionEvent;
import keystrokesmod.event.PostUpdateEvent;
import keystrokesmod.event.PreMotionEvent;
import keystrokesmod.event.PreUpdateEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.utility.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickPre(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) (Object) this;
        if (self.level().hasChunkAt(self.blockPosition())) {
            RotationUtils.prevRenderPitch = RotationUtils.renderPitch;
            RotationUtils.prevRenderYaw = RotationUtils.renderYaw;
            RavenEventBus.post(new PreUpdateEvent());
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTickPost(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) (Object) this;
        if (self.level().hasChunkAt(self.blockPosition())) {
            RavenEventBus.post(new PostUpdateEvent());
        }
    }

    @Inject(method = "sendPosition", at = @At("HEAD"))
    private void onSendMovementPacketsPre(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) (Object) this;

        PreMotionEvent.setRenderYaw(false);
        PreMotionEvent preMotionEvent = new PreMotionEvent(
                self.getX(),
                self.getY(),
                self.getZ(),
                self.getYRot(),
                self.getXRot(),
                self.onGround(),
                self.isSprinting(),
                self.isShiftKeyDown()
        );

        RavenEventBus.post(preMotionEvent);

        self.setPos(preMotionEvent.getPosX(), preMotionEvent.getPosY(), preMotionEvent.getPosZ());
        self.setYRot(preMotionEvent.getYaw());
        self.setXRot(preMotionEvent.getPitch());
        self.setSprinting(preMotionEvent.isSprinting());
        self.setShiftKeyDown(preMotionEvent.isSneaking());

        RotationUtils.serverRotations = new float[]{preMotionEvent.getYaw(), preMotionEvent.getPitch()};

        if (PreMotionEvent.setRenderYaw()) {
            RotationUtils.setRenderYaw(preMotionEvent.getYaw());
        }

        RotationUtils.renderPitch = preMotionEvent.getPitch();
        RotationUtils.renderYaw = preMotionEvent.getYaw();
    }

    @Inject(method = "sendPosition", at = @At("RETURN"))
    private void onSendMovementPacketsPost(CallbackInfo ci) {
        RavenEventBus.post(new PostMotionEvent());
    }
}

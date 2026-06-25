package keystrokesmod.mixin.impl.client;

import keystrokesmod.event.PostPlayerInputEvent;
import keystrokesmod.event.PrePlayerInputEvent;
import keystrokesmod.event.RavenEventBus;
import net.minecraft.client.Options;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput {
    @Shadow
    @Final
    private Options options;

    @Shadow
    public Vec2 moveVector;

    @Shadow
    public Input keyPresses;

    @Overwrite
    public void tick() {
        boolean keyUp = this.options.keyUp.isDown();
        boolean keyDown = this.options.keyDown.isDown();
        boolean keyLeft = this.options.keyLeft.isDown();
        boolean keyRight = this.options.keyRight.isDown();
        boolean keyJump = this.options.keyJump.isDown();
        boolean sneakKey = this.options.keyShift.isDown();
        boolean keySprint = this.options.keySprint.isDown();

        float forward = getMovementMultiplier(keyUp, keyDown);
        float strafe = getMovementMultiplier(keyLeft, keyRight);

        PrePlayerInputEvent moveInputEvent = new PrePlayerInputEvent(forward, strafe, keyJump, sneakKey, 0.3D);
        RavenEventBus.post(moveInputEvent);

        forward = moveInputEvent.getForward();
        strafe = moveInputEvent.getStrafe();
        keyJump = moveInputEvent.isJump();
        sneakKey = moveInputEvent.isSneak();

        if (sneakKey) {
            forward = (float) (forward * moveInputEvent.getSneakSlowDownMultiplier());
            strafe = (float) (strafe * moveInputEvent.getSneakSlowDownMultiplier());
        }

        this.moveVector = new Vec2(strafe, forward).normalized();
        this.keyPresses = new Input(
                forward > 0.0F,
                forward < 0.0F,
                strafe > 0.0F,
                strafe < 0.0F,
                keyJump,
                sneakKey,
                keySprint
        );
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTickPost(CallbackInfo ci) {
        RavenEventBus.post(new PostPlayerInputEvent());
    }

    private static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0F;
        }
        return positive ? 1.0F : -1.0F;
    }
}

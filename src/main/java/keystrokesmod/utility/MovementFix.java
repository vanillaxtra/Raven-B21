package keystrokesmod.utility;

import keystrokesmod.event.JumpEvent;
import keystrokesmod.event.PreMotionEvent;
import keystrokesmod.event.PrePlayerInputEvent;
import keystrokesmod.event.StrafeEvent;
import keystrokesmod.event.SubscribeEvent;
import net.minecraft.util.Mth;

public class MovementFix {
    private float yaw;

    @SubscribeEvent(priority = Integer.MIN_VALUE)
    public void onMoveInput(PrePlayerInputEvent event) {
        if (!fixMovement()) {
            return;
        }
        float forward = event.getForward();
        float strafe = event.getStrafe();
        double angle = Mth.wrapDegrees(Math.toDegrees(direction(Mc.player().getYRot(), forward, strafe)));
        if (forward == 0 && strafe == 0) {
            return;
        }
        float closestForward = 0;
        float closestStrafe = 0;
        float closestDifference = Float.MAX_VALUE;
        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) {
                    continue;
                }
                double predictedAngle = Mth.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                double difference = wrappedDifference(angle, predictedAngle);
                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }
        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }

    @SubscribeEvent(priority = Integer.MIN_VALUE)
    public void onJump(JumpEvent event) {
        if (fixMovement()) {
            event.setYaw(yaw);
        }
    }

    @SubscribeEvent(priority = Integer.MIN_VALUE)
    public void onStrafe(StrafeEvent event) {
        if (fixMovement()) {
            event.setYaw(yaw);
        }
    }

    @SubscribeEvent(priority = Integer.MIN_VALUE)
    public void onPreMotion(PreMotionEvent event) {
        this.yaw = event.getYaw();
    }

    private boolean fixMovement() {
        return false;
    }

    public double wrappedDifference(double number1, double number2) {
        return Math.min(Math.abs(number1 - number2),
                Math.min(Math.abs(number1 - 360) - Math.abs(number2 - 0),
                        Math.abs(number2 - 360) - Math.abs(number1 - 0)));
    }

    public double direction(float rotationYaw, double moveForward, double moveStrafing) {
        if (moveForward < 0F) {
            rotationYaw += 180F;
        }
        float forward = moveForward < 0F ? -0.5F : moveForward > 0F ? 0.5F : 1F;
        if (moveStrafing > 0F) {
            rotationYaw -= 90F * forward;
        }
        if (moveStrafing < 0F) {
            rotationYaw += 90F * forward;
        }
        return Math.toRadians(rotationYaw);
    }
}

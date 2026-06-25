package keystrokesmod.utility;

import keystrokesmod.event.PreMotionEvent;
import keystrokesmod.module.impl.client.Settings;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class RotationUtils {
    public static float renderPitch;
    public static float prevRenderPitch;
    public static float renderYaw;
    public static float prevRenderYaw;
    public static float[] serverRotations = new float[]{0, 0};

    public static void setRenderYaw(float yaw) {
        var player = Mc.player();
        if (player == null) {
            return;
        }
        player.yHeadRot = yaw;
        if (Settings.rotateBody != null && Settings.rotateBody.isToggled() && Settings.fullBody != null && Settings.fullBody.isToggled()) {
            player.yBodyRotO = prevRenderYaw;
            player.yBodyRot = yaw;
        }
    }

    public static void setSilentRotations(float yaw, float pitch) {
        renderYaw = yaw;
        renderPitch = pitch;
    }

    public static float[] getRotations(BlockPos blockPos) {
        var player = Mc.player();
        double x = blockPos.getX() + 0.45 - player.getX();
        double y = blockPos.getY() + 0.45 - (player.getY() + player.getEyeHeight(player.getPose()));
        double z = blockPos.getZ() + 0.45 - player.getZ();
        float angleToBlock = (float) (Math.atan2(z, x) * (180 / Math.PI)) - 90.0f;
        float deltaYaw = Mth.wrapDegrees(angleToBlock - player.getYRot());
        float yaw = player.getYRot() + deltaYaw;
        double horizontal = Math.sqrt(x * x + z * z);
        float pitch = (float) (-(Math.atan2(y, horizontal) * (180 / Math.PI)));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entity) {
        return getRotations(entity.blockPosition());
    }

    public static float angle(double posX, double posZ) {
        var player = Mc.player();
        return (float) (Math.toDegrees(Math.atan2(posZ - player.getZ(), posX - player.getX())) - 90.0f);
    }
}

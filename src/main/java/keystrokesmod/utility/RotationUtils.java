package keystrokesmod.utility;

import com.google.common.base.Predicates;
import keystrokesmod.event.PreMotionEvent;
import keystrokesmod.module.impl.client.Settings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;

import java.util.List;

public class RotationUtils {
    public static float renderPitch;
    public static float prevRenderPitch;
    public static float renderYaw;
    public static float prevRenderYaw;
    public static float[] serverRotations = new float[]{0, 0};
    public static final float PI = (float) Math.PI;
    public static final float TO_DEGREES = 180.0F / PI;

    public static void setRenderYaw(float yaw) {
        var player = Mc.player();
        player.headYaw = yaw;
        if (Settings.rotateBody.isToggled() && Settings.fullBody.isToggled()) {
            player.lastBodyYaw = prevRenderYaw;
            player.bodyYaw = yaw;
        }
    }

    public static float[] getRotations(BlockPos blockPos, float yawLimit, float pitchLimit) {
        float[] rotations = getRotations(blockPos);
        return fixRotation(rotations[0], rotations[1], yawLimit, pitchLimit);
    }

    public static float[] getRotations(BlockPos blockPos) {
        var player = Mc.player();
        double x = blockPos.getX() + 0.45 - player.getX();
        double y = blockPos.getY() + 0.45 - (player.getY() + player.getEyeHeight(player.getPose()));
        double z = blockPos.getZ() + 0.45 - player.getZ();

        float angleToBlock = (float) (Math.atan2(z, x) * (180 / Math.PI)) - 90.0f;
        float deltaYaw = Mth.wrapDegrees(angleToBlock - player.getYRot());
        float yaw = player.getYRot() + deltaYaw;

        double distance = Math.sqrt(x * x + z * z);
        float angleToBlockPitch = (float) (-(Math.atan2(y, distance) * (180 / Math.PI)));
        float deltaPitch = Mth.wrapDegrees(angleToBlockPitch - player.getXRot());
        float pitch = player.getXRot() + deltaPitch;
        pitch = clampTo90(pitch);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        var player = Mc.player();
        double x = posX + 1.0 - player.getX();
        double y = posY + 1.0 - (player.getY() + player.getEyeHeight(player.getPose()));
        double z = posZ + 1.0 - player.getZ();

        float angleToBlock = (float) (Math.atan2(z, x) * (180 / Math.PI)) - 90.0f;
        float deltaYaw = Mth.wrapDegrees(angleToBlock - player.getYRot());
        float yaw = player.getYRot() + deltaYaw;

        double distance = Math.sqrt(x * x + z * z);
        float angleToBlockPitch = (float) (-(Math.atan2(y, distance) * (180 / Math.PI)));
        float deltaPitch = Mth.wrapDegrees(angleToBlockPitch - player.getXRot());
        float pitch = clampTo90(player.getXRot() + deltaPitch);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Vec3 vec3) {
        var player = Mc.player();
        double x = vec3.x + 0.45 - player.getX();
        double y = vec3.y + 0.45 - (player.getY() + player.getEyeHeight(player.getPose()));
        double z = vec3.z + 0.45 - player.getZ();

        float angleToBlock = (float) (Math.atan2(z, x) * (180 / Math.PI)) - 90.0f;
        float deltaYaw = Mth.wrapDegrees(angleToBlock - player.getYRot());
        float yaw = player.getYRot() + deltaYaw;

        double distance = Math.sqrt(x * x + z * z);
        float angleToBlockPitch = (float) (-(Math.atan2(y, distance) * (180 / Math.PI)));
        float deltaPitch = Mth.wrapDegrees(angleToBlockPitch - player.getXRot());
        float pitch = clampTo90(player.getXRot() + deltaPitch);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(BlockPos blockPos, net.minecraft.core.Direction facing) {
        double x = blockPos.getX() + 0.5D;
        double y = blockPos.getY() + 0.5D;
        double z = blockPos.getZ() + 0.5D;
        x += facing.getOffsetX() * 0.5D;
        y += facing.getOffsetY() * 0.5D;
        z += facing.getOffsetZ() * 0.5D;
        Vec3 target = new Vec3(x, y, z);
        var player = Mc.player();
        Vec3 diff = target.subtract(player.getX(), player.getY() + player.getStandingEyeHeight(), player.getZ());
        double distance = Math.hypot(diff.x, diff.z);
        float yaw = (float) (Mth.atan2(diff.z, diff.x) * TO_DEGREES) - 90.0F;
        float pitch = (float) (-(Mth.atan2(diff.y, distance) * TO_DEGREES));
        return new float[]{applyVanilla(yaw), clampTo90(pitch)};
    }

    public static float interpolateValue(float tickDelta, float old, float newFloat) {
        return old + (newFloat - old) * tickDelta;
    }

    public static float[] getRotations(Entity entity, float yawLimit, float pitchLimit) {
        float[] rotations = getRotations(entity);
        if (rotations == null) {
            return null;
        }
        return fixRotation(rotations[0], rotations[1], yawLimit, pitchLimit);
    }

    public static double distanceFromYaw(Entity entity, boolean renderYaw) {
        float baseYaw = renderYaw && PreMotionEvent.setRenderYaw() ? RotationUtils.renderYaw : Mc.player().getYRot();
        return Math.abs(Mth.wrapDegrees(i(entity.getX(), entity.getZ()) - baseYaw));
    }

    public static float i(double x, double z) {
        var player = Mc.player();
        return (float) (Math.atan2(x - player.getX(), z - player.getZ()) * 57.295780181884766 * -1.0);
    }

    public static boolean isPossibleToHit(Entity target, double reach, float[] rotations) {
        var player = Mc.player();
        Vec3 eyePosition = player.getEyePosition();
        float yaw = rotations[0];
        float pitch = rotations[1];

        float radianYaw = -yaw * 0.017453292f - (float) Math.PI;
        float radianPitch = -pitch * 0.017453292f;
        float cosYaw = Mth.cos(radianYaw);
        float sinYaw = Mth.sin(radianYaw);
        float cosPitch = -Mth.cos(radianPitch);
        float sinPitch = Mth.sin(radianPitch);

        Vec3 lookVector = new Vec3(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
        Vec3 endPosition = eyePosition.add(lookVector.multiply(reach));

        Entity viewEntity = Mc.mc().getCameraEntity();
        AABB expandedBox = viewEntity.getBoundingBox().stretch(lookVector.multiply(reach)).inflate(1.0, 1.0, 1.0);
        List<Entity> entitiesInPath = Mc.level().getOtherEntities(viewEntity, expandedBox, Predicates.and(Entity::canHit, Entity::isAttackable));

        for (Entity entity : entitiesInPath) {
            if (entity == target) {
                AABB entityBox = entity.getBoundingBox().inflate(entity.getTargetingMargin());
                var intercept = entityBox.raycast(eyePosition, endPosition);
                return intercept.isPresent();
            }
        }
        return false;
    }

    public static boolean inRange(BlockPos blockPos, double reach) {
        float[] rotations = getRotations(blockPos);
        Vec3 eyes = Mc.player().getEyePosition();
        float yawRad = -rotations[0] * 0.017453292f;
        float pitchRad = -rotations[1] * 0.017453292f;
        float cos = Mth.cos(yawRad - (float) Math.PI);
        float sin = Mth.sin(yawRad - (float) Math.PI);
        float cosPitch = -Mth.cos(pitchRad);
        Vec3 look = new Vec3(sin * cosPitch, Mth.sin(pitchRad), cos * cosPitch);

        Block block = BlockUtils.getBlock(blockPos);
        var blockState = BlockUtils.getBlockState(blockPos);
        if (block != null && blockState != null) {
            AABB boundingBox = blockState.getOutlineShape(Mc.level(), blockPos).bounds().move(blockPos);
            if (!boundingBox.isEmpty()) {
                Vec3 targetVec = eyes.add(look.multiply(reach));
                var intercept = boundingBox.raycast(eyes, targetVec);
                return intercept.isPresent();
            }
        }
        return false;
    }

    public static float[] getRotations(Entity entity) {
        return getRotations(entity, PLAYER_OFFSETS.NONE);
    }

    public static float[] getRotations(Entity entity, PLAYER_OFFSETS offset) {
        if (entity == null) {
            return null;
        }
        var player = Mc.player();
        double dx = entity.getX() - player.getX();
        double dz = entity.getZ() - player.getZ();
        double dy;
        if (entity instanceof LivingEntity living) {
            dy = living.getY() + offset.getHeightOffset(living) * 0.9 - (player.getY() + player.getEyeHeight(player.getPose()));
        } else {
            dy = entity.getBoundingBox().getCenter().y - (player.getY() + player.getEyeHeight(player.getPose()));
        }
        float yaw = player.getYRot() + Mth.wrapDegrees((float) (Math.atan2(dz, dx) * 57.295780181884766) - 90.0f - player.getYRot());
        float pitch = clampTo90(player.getXRot() + Mth.wrapDegrees((float) (-(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 57.295780181884766)) - player.getXRot()) + 3.0f);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsPredicated(Entity entity, int ticks) {
        if (entity == null) {
            return null;
        }
        if (ticks == 0) {
            return getRotations(entity);
        }
        double posX = entity.getX();
        double posY = entity.getY();
        double posZ = entity.getZ();
        double dx = posX - entity.xOld;
        double dz = posZ - entity.zOld;
        for (int i = 0; i < ticks; ++i) {
            posX += dx;
            posZ += dz;
        }
        var player = Mc.player();
        double n4 = posX - player.getX();
        double dy;
        if (entity instanceof LivingEntity living) {
            dy = posY + living.getEyeHeight(living.getPose()) * 0.9 - (player.getY() + player.getEyeHeight(player.getPose()));
        } else {
            dy = entity.getBoundingBox().getCenter().y - (player.getY() + player.getEyeHeight(player.getPose()));
        }
        double n6 = posZ - player.getZ();
        return new float[]{
                applyVanilla(player.getYRot() + Mth.wrapDegrees((float) (Math.atan2(n6, n4) * 57.295780181884766) - 90.0f - player.getYRot())),
                clampTo90(player.getXRot() + Mth.wrapDegrees((float) (-(Math.atan2(dy, Math.sqrt(n4 * n4 + n6 * n6)) * 57.295780181884766)) - player.getXRot()) + 3.0f)
        };
    }

    public static float clampTo90(float pitch) {
        return Mth.clamp(pitch, -90.0f, 90.0f);
    }

    public static void setSilentRotations(float yaw, float pitch) {
        serverRotations[0] = yaw;
        serverRotations[1] = pitch;
    }

    public static float[] fixRotation(float yaw, float pitch, float prevYaw, float prevPitch) {
        float deltaYaw = yaw - prevYaw;
        float abs = Math.abs(deltaYaw);
        float deltaPitch = pitch - prevPitch;
        float sensitivity = Mc.mc().options.getMouseSensitivity().getValue().floatValue() * 0.6f + 0.2f;
        double gcd = sensitivity * sensitivity * sensitivity * 1.2;
        yaw = prevYaw + (float) (Math.round(deltaYaw / gcd) * gcd);
        pitch = prevPitch + (float) (Math.round(deltaPitch / gcd) * gcd);
        if (abs >= 1.0f) {
            int factor = (int) Settings.randomYawFactor.getInput();
            if (factor != 0) {
                int spread = factor * 100 + Utils.randomizeInt(-30, 30);
                yaw += Utils.randomizeInt(-spread, spread) / 100.0;
            }
        } else if (abs <= 0.04) {
            yaw += abs > 0.0f ? 0.01 : -0.01;
        }
        return new float[]{yaw, clampTo90(pitch)};
    }

    public static float angle(double x, double z) {
        var player = Mc.player();
        return (float) (Math.atan2(x - player.getX(), z - player.getZ()) * 57.295780181884766 * -1.0);
    }

    public static BlockHitResult rayCast(double distance, float yaw, float pitch, boolean fluids) {
        Vec3 eyes = Mc.player().getEyePosition();
        Vec3 look = getVectorForRotation(pitch, yaw);
        Vec3 end = eyes.add(look.multiply(distance));
        return Mc.level().raycast(new ClipContext(eyes, end, ClipContext.ShapeType.OUTLINE, fluids ? ClipContext.FluidHandling.ANY : ClipContext.FluidHandling.NONE, Mc.player()));
    }

    public static BlockHitResult rayTraceCustom(double reach, float yaw, float pitch) {
        Vec3 eyes = Mc.player().getEyePosition();
        Vec3 look = getVectorForRotation(pitch, yaw);
        Vec3 end = eyes.add(look.multiply(reach));
        return Mc.level().raycast(new ClipContext(eyes, end, ClipContext.ShapeType.OUTLINE, ClipContext.FluidHandling.NONE, Mc.player()));
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float cosYaw = Mth.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float sinYaw = Mth.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float cosPitch = -Mth.cos(-pitch * ((float) Math.PI / 180F));
        float sinPitch = Mth.sin(-pitch * ((float) Math.PI / 180F));
        return new Vec3(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
    }

    public static float applyVanilla(float yaw, boolean stop) {
        if (stop) {
            return yaw;
        }
        int scaleFactor = (int) Math.floor(serverRotations[0] / 360);
        float unwrappedYaw = yaw + 360 * scaleFactor;
        if (unwrappedYaw < serverRotations[0] - 180) {
            unwrappedYaw += 360;
        } else if (unwrappedYaw > serverRotations[0] + 180) {
            unwrappedYaw -= 360;
        }
        return serverRotations[0] + (unwrappedYaw - serverRotations[0]);
    }

    public static HitResult rayTrace(double range, float partialTicks, float[] rotations, LivingEntity ignoreCollision) {
        if (ignoreCollision != null) {
            HitResult ignored = rayTraceIgnore(range, partialTicks, rotations, ignoreCollision);
            if (ignored != null) {
                return ignored;
            }
        }
        var player = Mc.player();
        if (rotations == null) {
            rotations = new float[]{player.getYRot(), player.getXRot()};
        }
        BlockHitResult blockHit = rayTraceCustom(range, rotations[0], rotations[1]);
        double blockDistance = range;
        Vec3 eyes = player.getEyePosition();
        if (blockHit != null) {
            blockDistance = blockHit.getLocation().distanceTo(eyes);
        }
        Vec3 look = getVectorForRotation(rotations[1], rotations[0]);
        Vec3 end = eyes.add(look.multiply(range));
        Entity targetEntity = null;
        Vec3 hitPos = null;
        double closest = blockDistance;
        AABB searchBox = player.getBoundingBox().stretch(look.multiply(range)).inflate(1.0);
        List<Entity> entities = Mc.level().getOtherEntities(player, searchBox, Predicates.and(Entity::canHit, Entity::isAttackable));

        for (Entity entity : entities) {
            AABB AABB = entity.getBoundingBox().inflate(entity.getTargetingMargin());
            var intercept = AABB.raycast(eyes, end);
            if (AABB.contains(eyes)) {
                if (closest >= 0.0) {
                    targetEntity = entity;
                    hitPos = intercept.orElse(eyes);
                    closest = 0.0;
                }
            } else if (intercept.isPresent()) {
                double dist = eyes.distanceTo(intercept.get());
                if (dist < closest || closest == 0.0) {
                    targetEntity = entity;
                    hitPos = intercept.get();
                    closest = dist;
                }
            }
        }
        if (targetEntity != null && closest < blockDistance) {
            return new net.minecraft.world.phys.EntityHitResult(targetEntity, hitPos);
        }
        return null;
    }

    public static HitResult rayTraceIgnore(double range, float partialTicks, float[] rotations, LivingEntity ignoreCollision) {
        BlockHitResult blockHit = rayTraceCustom(range, rotations[0], rotations[1]);
        Vec3 start = Mc.player().getEyePosition();
        double blockDistance = range;
        if (blockHit != null) {
            blockDistance = blockHit.getLocation().distanceTo(start);
        }
        if (ignoreCollision != null) {
            if (rotations == null) {
                rotations = new float[]{Mc.player().getYRot(), Mc.player().getXRot()};
            }
            Vec3 look = getVectorForRotation(rotations[1], rotations[0]);
            Vec3 end = start.add(look.multiply(range));
            AABB AABB = ignoreCollision.getBoundingBox().inflate(ignoreCollision.getTargetingMargin());
            var intercept = AABB.raycast(start, end);
            if (AABB.contains(start)) {
                return new net.minecraft.world.phys.EntityHitResult(ignoreCollision, start);
            }
            if (intercept.isPresent() && start.distanceTo(intercept.get()) < blockDistance) {
                return new net.minecraft.world.phys.EntityHitResult(ignoreCollision, intercept.get());
            }
        }
        return blockHit;
    }

    public static float applyVanilla(float yaw) {
        return applyVanilla(yaw, false);
    }

    public enum PLAYER_OFFSETS {
        EYE,
        CHEST,
        FOOT,
        NONE;

        public double getHeightOffset(Entity entity) {
            return switch (this) {
                case NONE, EYE -> entity.getEyeHeight(entity.getPose());
                case CHEST -> entity.getBbHeight() / 2;
                case FOOT -> 0;
            };
        }
    }
}

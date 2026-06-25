package keystrokesmod.script.classes;

import keystrokesmod.utility.Mc;

import java.util.HashMap;

public class Entity {
    public net.minecraft.world.entity.Entity entity;
    public String type = "";
    public int entityId;
    public boolean isLiving;
    public boolean isPlayer;
    public boolean isUser;
    private static final HashMap<Integer, Entity> cache = new HashMap<>();

    public Entity(net.minecraft.world.entity.Entity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        this.type = entity.getClass().getSimpleName();
        this.entityId = entity.getId();
        this.isLiving = entity instanceof net.minecraft.world.entity.LivingEntity;
        this.isPlayer = entity instanceof net.minecraft.world.entity.player.Player;
        if (this.isPlayer && Mc.player() != null && entity.getUUID().equals(Mc.player().getUUID())) {
            this.isUser = true;
        }
    }

    public static Entity convert(net.minecraft.world.entity.Entity entity) {
        if (entity == null) {
            return null;
        }
        int id = entity.getId() + System.identityHashCode(entity);
        return cache.computeIfAbsent(id, ignored -> new Entity(entity));
    }

    public static void clearCache() {
        cache.clear();
    }

    public boolean allowEditing() {
        return false;
    }

    public double distanceTo(ScriptVec3 position) {
        return entity == null ? 0.0D : Math.sqrt(entity.distanceToSqr(position.x, position.y, position.z));
    }

    public double distanceToSq(ScriptVec3 position) {
        return entity == null ? 0.0D : entity.distanceToSqr(position.x, position.y, position.z);
    }

    public double distanceToGround() {
        return 0.0D;
    }

    public ScriptVec3 getPosition() {
        return entity == null ? new ScriptVec3(0, 0, 0) : new ScriptVec3(entity.getX(), entity.getY(), entity.getZ());
    }

    public float getYaw() {
        return entity == null ? 0.0F : entity.getYRot();
    }

    public float getPitch() {
        return entity == null ? 0.0F : entity.getXRot();
    }

    public String getName() {
        return entity == null ? "" : entity.getName().getString();
    }

    public boolean isAlive() {
        return entity != null && entity.isAlive();
    }

    public boolean isInWater() {
        return entity != null && entity.isInWater();
    }

    public boolean isOnGround() {
        return entity != null && entity.onGround();
    }

    public boolean isSneaking() {
        return entity != null && entity.isShiftKeyDown();
    }

    public boolean isSprinting() {
        return entity != null && entity.isSprinting();
    }

    public boolean isBurning() {
        return entity != null && entity.isOnFire();
    }

    public boolean isInvisible() {
        return entity != null && entity.isInvisible();
    }

    public boolean isClimbing() {
        return entity instanceof net.minecraft.world.entity.LivingEntity living && living.onClimbable();
    }

    public int getHurtTime() {
        return entity instanceof net.minecraft.world.entity.LivingEntity living ? living.hurtTime : 0;
    }

    public float getHealth() {
        return entity instanceof net.minecraft.world.entity.LivingEntity living ? living.getHealth() : 0.0F;
    }

    public float getMaxHealth() {
        return entity instanceof net.minecraft.world.entity.LivingEntity living ? living.getMaxHealth() : 0.0F;
    }

    public net.minecraft.world.phys.AABB getBoundingBox() {
        return entity == null ? new net.minecraft.world.phys.AABB(0, 0, 0, 0, 0, 0) : entity.getBoundingBox();
    }
}

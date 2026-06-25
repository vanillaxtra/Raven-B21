package keystrokesmod.event;

import net.minecraft.world.entity.LivingEntity;

public class LivingUpdateEvent {
    public final LivingEntity entity;

    public LivingUpdateEvent(LivingEntity entity) {
        this.entity = entity;
    }
}

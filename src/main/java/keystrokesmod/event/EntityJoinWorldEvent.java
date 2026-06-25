package keystrokesmod.event;

import net.minecraft.world.entity.Entity;

public class EntityJoinWorldEvent {
    public final Entity entity;

    public EntityJoinWorldEvent(Entity entity) {
        this.entity = entity;
    }
}

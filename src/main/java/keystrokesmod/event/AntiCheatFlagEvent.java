package keystrokesmod.event;

import net.minecraft.world.entity.Entity;

public class AntiCheatFlagEvent  {
    public String flag;
    public Entity entity;

    public AntiCheatFlagEvent(String flag, Entity entity) {
        this.flag = flag;
        this.entity = entity;
    }
}

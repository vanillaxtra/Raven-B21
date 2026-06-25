package keystrokesmod.mixin.impl.accessor;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface IAccessorMinecraft {
    @Accessor("deltaTracker")
    DeltaTracker.Timer getDeltaTracker();

    @Accessor("rightClickDelay")
    int getRightClickDelay();

    @Accessor("rightClickDelay")
    void setRightClickDelay(int delay);

    @Accessor("missTime")
    int getMissTime();

    @Accessor("missTime")
    void setMissTime(int delay);

    @Invoker("startUseItem")
    void callStartUseItem();

    @Invoker("startAttack")
    boolean callStartAttack();
}

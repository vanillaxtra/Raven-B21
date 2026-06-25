package keystrokesmod.mixin.impl.accessor;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface IAccessorLocalPlayer {
    @Accessor("lastXClient")
    double getLastReportedPosX();

    @Accessor("lastYClient")
    double getLastReportedPosY();

    @Accessor("lastZClient")
    double getLastReportedPosZ();

    @Accessor("lastYawClient")
    float getLastReportedYaw();
}

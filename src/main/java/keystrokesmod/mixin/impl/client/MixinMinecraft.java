package keystrokesmod.mixin.impl.client;

import keystrokesmod.event.GuiUpdateEvent;
import keystrokesmod.event.PreInputEvent;
import keystrokesmod.event.PreSlotScrollEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.event.SlotUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickPre(CallbackInfo ci) {
        RavenEventBus.post(new PreInputEvent());
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        Screen previousGui = mc.screen;
        Screen setGui = screen;
        boolean opened = setGui != null;
        if (!opened) {
            setGui = previousGui;
        }
        RavenEventBus.post(new GuiUpdateEvent(setGui, opened));
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void onChangeSelectedSlot(Inventory inventory, int slot) {
        PreSlotScrollEvent event = new PreSlotScrollEvent(slot, inventory.getSelectedSlot());
        RavenEventBus.post(event);
        if (event.isCanceled()) {
            return;
        }
        inventory.setSelectedSlot(slot);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V", shift = At.Shift.AFTER))
    private void onSelectedSlotChanged(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player != null) {
            RavenEventBus.post(new SlotUpdateEvent(mc.player.getInventory().getSelectedSlot()));
        }
    }
}

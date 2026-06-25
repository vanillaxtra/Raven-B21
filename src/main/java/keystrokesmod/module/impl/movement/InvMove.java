package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.KeyMapping;

public class InvMove extends Module {
    private ButtonSetting allowJump;
    private ButtonSetting allowSprint;

    public InvMove() {
        super("InvMove", category.movement, 0);
        this.registerSetting(allowJump = new ButtonSetting("Allow jump", true));
        this.registerSetting(allowSprint = new ButtonSetting("Allow sprint", true));
    }

    public void onUpdate() {
        if (!Mc.nullCheck() || !(mc.screen instanceof AbstractContainerScreen) && !(mc.screen instanceof ChatScreen)) {
            return;
        }
        updateKey(mc.options.keyUp);
        updateKey(mc.options.keyDown);
        updateKey(mc.options.keyLeft);
        updateKey(mc.options.keyRight);
        if (allowJump.isToggled()) {
            updateKey(mc.options.keyJump);
        }
        if (allowSprint.isToggled()) {
            updateKey(mc.options.keySprint);
        }
    }

    private void updateKey(KeyMapping key) {
        KeyMapping.set(key.getDefaultKey(), key.isDown());
    }
}

package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.client.KeyMapping;

public class AntiAFK extends Module {
    private SliderSetting afk;
    private ButtonSetting jump;
    private ButtonSetting swapItem;
    private SliderSetting spin;
    private String[] afkModes = new String[]{"None", "Wander", "Forward", "Backward"};
    private String[] spinModes = new String[]{"None", "Right", "Left"};
    private int ticks;
    private boolean flip;

    public AntiAFK() {
        super("AntiAFK", category.player);
        this.registerSetting(afk = new SliderSetting("AFK", 0, afkModes));
        this.registerSetting(jump = new ButtonSetting("Jump", false));
        this.registerSetting(swapItem = new ButtonSetting("Swap item", false));
        this.registerSetting(spin = new SliderSetting("Spin", 0, spinModes));
    }

    public void onEnable() {
        ticks = 40;
        flip = Utils.getRandom().nextBoolean();
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        resetMovement();
        switch ((int) afk.getInput()) {
            case 1 -> {
                KeyMapping.set(mc.options.keyUp.getDefaultKey(), flip);
                KeyMapping.set(mc.options.keyRight.getDefaultKey(), !flip);
            }
            case 2 -> KeyMapping.set(mc.options.keyUp.getDefaultKey(), true);
            case 3 -> KeyMapping.set(mc.options.keyDown.getDefaultKey(), true);
        }
        switch ((int) spin.getInput()) {
            case 1 -> mc.player.setYRot(mc.player.getYRot() + 3);
            case 2 -> mc.player.setYRot(mc.player.getYRot() - 3);
        }
        if (jump.isToggled() && mc.player.onGround()) {
            mc.player.jumpFromGround();
        }
        if (--ticks <= 0) {
            if (swapItem.isToggled()) {
                mc.player.getInventory().setSelectedSlot(Utils.randomizeInt(0, 8));
            }
            ticks = 40;
            flip = !flip;
        }
    }

    private void resetMovement() {
        KeyMapping.set(mc.options.keyUp.getDefaultKey(), false);
        KeyMapping.set(mc.options.keyDown.getDefaultKey(), false);
        KeyMapping.set(mc.options.keyLeft.getDefaultKey(), false);
        KeyMapping.set(mc.options.keyRight.getDefaultKey(), false);
    }

    public void onDisable() {
        resetMovement();
    }
}

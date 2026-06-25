package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.entity.player.Player;

public class Blink extends Module {
    private SliderSetting maxTicks;
    private ButtonSetting pulse;
    private int ticks;

    public Blink() {
        super("Blink", category.player, 0);
        this.registerSetting(maxTicks = new SliderSetting("Max ticks", 20, 1, 100, 1));
        this.registerSetting(pulse = new ButtonSetting("Pulse", false));
    }

    public static boolean blinking;

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck()) {
            return;
        }
        blinking = true;
        ticks++;
        if (ticks >= maxTicks.getInput()) {
            if (pulse.isToggled()) {
                blinking = false;
                ticks = 0;
            } else {
                disable();
            }
        }
    }

    public void onDisable() {
        blinking = false;
        ticks = 0;
    }
}

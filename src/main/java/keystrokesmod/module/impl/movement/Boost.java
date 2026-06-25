package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.TimerHelper;
import keystrokesmod.utility.Utils;

public class Boost extends Module {
    public static DescriptionSetting c;
    public static SliderSetting a;
    public static SliderSetting b;
    private int startTick;
    private boolean reenableTimer;

    public Boost() {
        super("Boost", category.movement, 0);
        this.registerSetting(c = new DescriptionSetting("20 ticks are in 1 second"));
        this.registerSetting(a = new SliderSetting("Multiplier", 2.0D, 1.0D, 3.0D, 0.05D));
        this.registerSetting(b = new SliderSetting("Time ticks", 15.0D, 1.0D, 80.0D, 1.0D));
    }

    public void onEnable() {
        if (ModuleManager.timer.isEnabled()) {
            reenableTimer = true;
            ModuleManager.timer.disable();
        }
        startTick = Mc.nullCheck() ? mc.player.tickCount : 0;
    }

    public void onDisable() {
        startTick = 0;
        Utils.resetTimer();
        if (reenableTimer) {
            ModuleManager.timer.enable();
        }
        reenableTimer = false;
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        TimerHelper.speed = (float) a.getInput();
        if (mc.player.tickCount - startTick >= (int) b.getInput()) {
            Utils.resetTimer();
            this.disable();
        }
    }
}

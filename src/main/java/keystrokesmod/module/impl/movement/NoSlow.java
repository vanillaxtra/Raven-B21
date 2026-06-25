package keystrokesmod.module.impl.movement;

import keystrokesmod.event.PreUpdateEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class NoSlow extends Module {
    private SliderSetting speed;
    private ButtonSetting items;
    private ButtonSetting webs;
    public static boolean noSlow;

    public NoSlow() {
        super("NoSlow", category.movement, 0);
        this.registerSetting(speed = new SliderSetting("Speed", "%", 100, 0, 100, 1));
        this.registerSetting(items = new ButtonSetting("Items", true));
        this.registerSetting(webs = new ButtonSetting("Webs", true));
    }

    @SubscribeEvent
    public void onPreUpdate(PreUpdateEvent e) {
        noSlow = Mc.nullCheck() && isEnabled() && items.isToggled();
    }

    public void onDisable() {
        noSlow = false;
    }

    public float getMultiplier() {
        return (float) (speed.getInput() / 100.0);
    }
}

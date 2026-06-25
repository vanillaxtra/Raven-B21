package keystrokesmod.module.impl.client;

import keystrokesmod.Raven;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class Gui extends Module {
    public static SliderSetting guiScale;
    public static SliderSetting backgroundBlur;
    public static SliderSetting scrollSpeed;
    public static ButtonSetting removePlayerModel;
    public static ButtonSetting darkBackground;
    public static ButtonSetting limitToScreen;
    public static ButtonSetting removeWatermark;
    public static ButtonSetting rainBowOutlines;

    public Gui() {
        super("Gui", category.client, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT);
        this.registerSetting(guiScale = new SliderSetting("Gui scale", 1, new String[]{"Small", "Normal", "Large"}));
        this.registerSetting(backgroundBlur = new SliderSetting("Background blur", "%", 0, 0, 100, 1));
        this.registerSetting(scrollSpeed = new SliderSetting("Scroll speed", 50, 2, 90, 1));
        this.registerSetting(darkBackground = new ButtonSetting("Dark background", true));
        this.registerSetting(limitToScreen = new ButtonSetting("Limit to screen", false));
        this.registerSetting(rainBowOutlines = new ButtonSetting("Rainbow outlines", true));
        this.registerSetting(removePlayerModel = new ButtonSetting("Remove player model", false));
        this.registerSetting(removeWatermark = new ButtonSetting("Remove watermark", false));
    }

    public void onEnable() {
        if (Mc.nullCheck() && mc.screen != Raven.clickGui) {
            mc.setScreen(Raven.clickGui);
            Raven.clickGui.initMain();
        }
        this.disable();
    }
}

package keystrokesmod.module.impl.render;

import keystrokesmod.utility.RenderUtils;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Theme;
import keystrokesmod.utility.Utils;

public class HUD extends Module {
    public static ButtonSetting alphabeticalSort;
    public static ButtonSetting showInfo;
    public static SliderSetting theme;
    public static ButtonSetting background;
    public static SliderSetting backgroundAlpha;
    public static ButtonSetting blur;
    public static ButtonSetting outline;
    public static ButtonSetting lowercase;
    public static ButtonSetting removeBrackets;
    public static ButtonSetting showEffects;
    public static ButtonSetting showPing;
    public static ButtonSetting showFps;
    public static ButtonSetting showBps;
    public static SliderSetting colorMode;
    public float posX = 5;
    public float posY = 5;

    public HUD() {
        super("HUD", category.render, 0);
        this.registerSetting(alphabeticalSort = new ButtonSetting("Alphabetical sort", false));
        this.registerSetting(showInfo = new ButtonSetting("Show info", true));
        this.registerSetting(theme = new SliderSetting("Theme", 0, Theme.themes));
        this.registerSetting(background = new ButtonSetting("Background", true));
        this.registerSetting(backgroundAlpha = new SliderSetting("Background alpha", 120, 0, 255, 1));
        this.registerSetting(blur = new ButtonSetting("Blur", false));
        this.registerSetting(outline = new ButtonSetting("Outline", true));
        this.registerSetting(lowercase = new ButtonSetting("Lowercase", false));
        this.registerSetting(removeBrackets = new ButtonSetting("Remove brackets", false));
        this.registerSetting(showEffects = new ButtonSetting("Show effects", false));
        this.registerSetting(showPing = new ButtonSetting("Show ping", false));
        this.registerSetting(showFps = new ButtonSetting("Show FPS", false));
        this.registerSetting(showBps = new ButtonSetting("Show BPS", false));
        this.registerSetting(colorMode = new SliderSetting("Color mode", 0, new String[]{"Static", "Fade", "Rainbow"}));
        this.alwaysOn = true;
        this.enable();
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent ev) {
        if (ev.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        if (Module.sort) {
            ModuleManager.sort();
            Module.sort = false;
        }
        int y = (int) posY;
        int n = 0;
        for (Module module : ModuleManager.organizedModules) {
            if (module.isHidden()) {
                continue;
            }
            String name = module.getNameInHud();
            if (showInfo.isToggled()) {
                String info = module.getInfoUpdate();
                if (!info.isEmpty()) {
                    name += " " + info;
                }
            }
            if (lowercase.isToggled()) {
                name = name.toLowerCase();
            }
            if (!removeBrackets.isToggled()) {
                name = "[" + name + "]";
            }
            int color = Theme.getGradient((int) theme.getInput(), n);
            if (background.isToggled()) {
                int w = mc.font.width(name);
                // simple bg draw via in-game hud layer in mixin
            }
            RenderUtils.drawTextWithShadow(mc.font, name, posX, y, color);
            y += mc.font.lineHeight + 2;
            n++;
        }
        if (showFps.isToggled()) {
            RenderUtils.drawTextWithShadow(mc.font, "FPS: 0", posX, y, -1);
        }
        if (showBps.isToggled()) {
            y += mc.font.lineHeight + 2;
            RenderUtils.drawTextWithShadow(mc.font, "BPS: " + Utils.asWholeNum(Utils.getHorizontalSpeed() * 20), posX, y, -1);
        }
    }
}

package keystrokesmod.module.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ChestESP extends Module {
    private SliderSetting red;
    private SliderSetting green;
    private SliderSetting blue;
    private ButtonSetting rainbow;
    private ButtonSetting outline;
    private ButtonSetting shade;

    public ChestESP() {
        super("ChestESP", category.render, 0);
        this.registerSetting(red = new SliderSetting("Red", 255, 0, 255, 1));
        this.registerSetting(green = new SliderSetting("Green", 165, 0, 255, 1));
        this.registerSetting(blue = new SliderSetting("Blue", 0, 0, 255, 1));
        this.registerSetting(rainbow = new ButtonSetting("Rainbow", false));
        this.registerSetting(outline = new ButtonSetting("Outline", true));
        this.registerSetting(shade = new ButtonSetting("Shade", true));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        int rgb = rainbow.isToggled() ? Utils.getChroma(2L, 0L) : ((int) red.getInput() << 16) | ((int) green.getInput() << 8) | (int) blue.getInput();
        for (BlockEntity be : Utils.getLoadedBlockEntities()) {
            if (be instanceof ChestBlockEntity) {
                RenderUtils.renderChest(be.getBlockPos(), rgb, outline.isToggled(), shade.isToggled());
            }
        }
    }
}

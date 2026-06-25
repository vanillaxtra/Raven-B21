package keystrokesmod.module.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import net.minecraft.world.entity.item.ItemEntity;

public class ItemESP extends Module {
    private SliderSetting red;
    private SliderSetting green;
    private SliderSetting blue;

    public ItemESP() {
        super("ItemESP", category.render, 0);
        this.registerSetting(red = new SliderSetting("Red", 255, 0, 255, 1));
        this.registerSetting(green = new SliderSetting("Green", 255, 0, 255, 1));
        this.registerSetting(blue = new SliderSetting("Blue", 0, 0, 255, 1));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        int rgb = ((int) red.getInput() << 16) | ((int) green.getInput() << 8) | (int) blue.getInput();
        for (ItemEntity item : mc.level.getEntities(
                net.minecraft.world.level.entity.EntityTypeTest.forClass(ItemEntity.class),
                mc.player.getBoundingBox().inflate(48),
                i -> true)) {
            RenderUtils.drawEntityBox(item, rgb, false);
        }
    }
}

package keystrokesmod.module.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.BlockUtils;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Xray extends Module {
    private SliderSetting range;
    private ButtonSetting caves;
    private final Set<BlockPos> cached = new HashSet<>();
    private int tick;

    public Xray() {
        super("Xray", category.render, 0);
        this.registerSetting(range = new SliderSetting("Range", 32, 8, 64, 1));
        this.registerSetting(caves = new ButtonSetting("Caves only", false));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        if (++tick % 20 == 0) {
            scan();
        }
        for (BlockPos pos : cached) {
            Block block = BlockUtils.getBlock(pos);
            int[] rgb = getColor(block);
            if (rgb != null) {
                RenderUtils.renderBlock(pos, new Color(rgb[0], rgb[1], rgb[2]).getRGB(), false, true);
            }
        }
    }

    private void scan() {
        cached.clear();
        int r = (int) range.getInput();
        BlockPos center = mc.player.blockPosition();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    if (getColor(BlockUtils.getBlock(pos)) != null) {
                        cached.add(pos);
                    }
                }
            }
        }
    }

    private int[] getColor(Block block) {
        String id = block.getDescriptionId();
        if (id.contains("diamond")) return new int[]{0, 255, 255};
        if (id.contains("gold")) return new int[]{255, 215, 0};
        if (id.contains("iron")) return new int[]{210, 210, 210};
        if (id.contains("coal")) return new int[]{30, 30, 30};
        if (id.contains("redstone")) return new int[]{255, 0, 0};
        if (id.contains("lapis")) return new int[]{0, 0, 255};
        if (id.contains("emerald")) return new int[]{0, 255, 0};
        if (id.contains("ancient_debris")) return new int[]{120, 60, 40};
        return null;
    }

    public void onDisable() {
        cached.clear();
    }
}

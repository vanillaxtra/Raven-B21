package keystrokesmod.keystroke;

import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;

public class KeyStrokeKeyRenderer {
    private final KeyMapping keyBinding;
    private final int offsetX;
    private final int offsetY;
    private boolean wasDown = true;
    private long lastChange = 0L;
    private int fadeAlpha = 255;
    private double colorScale = 1.0D;

    public KeyStrokeKeyRenderer(KeyMapping keyBinding, int offsetX, int offsetY) {
        this.keyBinding = keyBinding;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void renderKey(GuiGraphics context, int baseX, int baseY, int color) {
        boolean down = this.keyBinding.isDown();
        String label = getKeyLabel(this.keyBinding);
        if (down != this.wasDown) {
            this.wasDown = down;
            this.lastChange = System.currentTimeMillis();
        }
        if (down) {
            this.fadeAlpha = Math.min(255, (int) (2L * (System.currentTimeMillis() - this.lastChange)));
            this.colorScale = Math.max(0.0D, 1.0D - (System.currentTimeMillis() - this.lastChange) / 20.0D);
        } else {
            this.fadeAlpha = Math.max(0, 255 - (int) (2L * (System.currentTimeMillis() - this.lastChange)));
            this.colorScale = Math.min(1.0D, (System.currentTimeMillis() - this.lastChange) / 20.0D);
        }
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        int outlineColor = new Color(r, g, b).getRGB();
        int x1 = baseX + this.offsetX;
        int y1 = baseY + this.offsetY;
        int x2 = x1 + 22;
        int y2 = y1 + 22;
        int bgColor = 2013265920 + (this.fadeAlpha << 16) + (this.fadeAlpha << 8) + this.fadeAlpha;
        RenderUtils.drawRect(x1, y1, x2, y2, bgColor);
        if (KeyStroke.f) {
            RenderUtils.drawRect(x1, y1, x2, y1 + 1, outlineColor);
            RenderUtils.drawRect(x1, y2 - 1, x2, y2, outlineColor);
            RenderUtils.drawRect(x1, y1, x1 + 1, y2, outlineColor);
            RenderUtils.drawRect(x2 - 1, y1, x2, y2, outlineColor);
        }
        int textColor = -16777216 + ((int) (r * this.colorScale) << 16) + ((int) (g * this.colorScale) << 8) + (int) (b * this.colorScale);
        context.drawString(Mc.mc().font, label, x1 + 8, y1 + 8, textColor, false);
    }

    private static String getKeyLabel(KeyMapping bind) {
        return bind.getTranslatedKeyMessage().getString().toUpperCase();
    }
}

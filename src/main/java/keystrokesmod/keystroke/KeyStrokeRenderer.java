package keystrokesmod.keystroke;

import keystrokesmod.utility.Mc;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.KeyMapping;

import java.awt.Color;

public class KeyStrokeRenderer {
    private static final int[] COLORS = new int[]{16777215, 16711680, 65280, 255, 16776960, 11141290};
    private final KeyStrokeKeyRenderer[] movementKeys = new KeyStrokeKeyRenderer[4];
    private final KeyStrokeMouse[] mouseButtons = new KeyStrokeMouse[2];

    public KeyStrokeRenderer() {
        KeyMapping forward = Mc.mc().options.keyUp;
        KeyMapping back = Mc.mc().options.keyDown;
        KeyMapping left = Mc.mc().options.keyLeft;
        KeyMapping right = Mc.mc().options.keyRight;
        this.movementKeys[0] = new KeyStrokeKeyRenderer(forward, 26, 2);
        this.movementKeys[1] = new KeyStrokeKeyRenderer(back, 26, 26);
        this.movementKeys[2] = new KeyStrokeKeyRenderer(left, 2, 26);
        this.movementKeys[3] = new KeyStrokeKeyRenderer(right, 50, 26);
        this.mouseButtons[0] = new KeyStrokeMouse(0, 2, 50);
        this.mouseButtons[1] = new KeyStrokeMouse(1, 38, 50);
        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    private void onHudRender(GuiGraphics context, net.minecraft.client.DeltaTracker tickCounter) {
        var mc = Mc.mc();
        if (mc.screen instanceof KeyStrokeConfigScreen) {
            this.renderKeystrokes(context);
        } else if (mc.screen == null && mc.isWindowActive() && !mc.getDebugOverlay().showDebugScreen()) {
            this.renderKeystrokes(context);
        }
    }

    public void renderKeystrokes(GuiGraphics context) {
        if (!KeyStroke.e) {
            return;
        }
        int x = KeyStroke.x;
        int y = KeyStroke.y;
        int textColor = this.getColor(KeyStroke.currentColorNumber);
        boolean showMouse = KeyStroke.d;
        int width = 74;
        int height = showMouse ? 74 : 50;
        int scaledWidth = mc().getWindow().getGuiScaledWidth();
        int scaledHeight = mc().getWindow().getGuiScaledHeight();
        if (x < 0) {
            KeyStroke.x = 0;
            x = 0;
        } else if (x > scaledWidth - width) {
            KeyStroke.x = scaledWidth - width;
            x = KeyStroke.x;
        }
        if (y < 0) {
            KeyStroke.y = 0;
            y = 0;
        } else if (y > scaledHeight - height) {
            KeyStroke.y = scaledHeight - height;
            y = KeyStroke.y;
        }
        this.drawMovementKeys(context, x, y, textColor);
        if (showMouse) {
            this.drawMouseButtons(context, x, y, textColor);
        }
    }

    private static net.minecraft.client.Minecraft mc() {
        return Mc.mc();
    }

    private int getColor(int index) {
        return index == 6 ? Color.getHSBColor((float) (System.currentTimeMillis() % 3750L) / 3750.0F, 1.0F, 1.0F).getRGB() : COLORS[index];
    }

    private void drawMovementKeys(GuiGraphics context, int x, int y, int textColor) {
        for (KeyStrokeKeyRenderer key : this.movementKeys) {
            key.renderKey(context, x, y, textColor);
        }
    }

    private void drawMouseButtons(GuiGraphics context, int x, int y, int textColor) {
        for (KeyStrokeMouse button : this.mouseButtons) {
            button.render(context, x, y, textColor);
        }
    }
}

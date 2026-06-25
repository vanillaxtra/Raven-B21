package keystrokesmod.keystroke;

import keystrokesmod.Raven;
import keystrokesmod.utility.CPSCalculator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;

public class KeyStrokeConfigScreen extends Screen {
    private static final String[] COLORS = new String[]{"White", "Red", "Green", "Blue", "Yellow", "Purple", "Rainbow"};
    private Button modeBtn;
    private Button textColorBtn;
    private Button showMouseBtn;
    private Button outlineBtn;
    private boolean dragging;
    private int lx;
    private int ly;

    public KeyStrokeConfigScreen() {
        super(net.minecraft.network.chat.Component.literal("Keystrokes"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;
        this.modeBtn = Button.builder(net.minecraft.network.chat.Component.literal("Mod: " + (KeyStroke.e ? "Enabled" : "Disabled")), btn -> {
            KeyStroke.e = !KeyStroke.e;
            btn.setMessage(net.minecraft.network.chat.Component.literal("Mod: " + (KeyStroke.e ? "Enabled" : "Disabled")));
        }).bounds(cx - 70, cy - 28, 140, 20).build();
        this.textColorBtn = Button.builder(net.minecraft.network.chat.Component.literal("Text color: " + COLORS[KeyStroke.currentColorNumber]), btn -> {
            KeyStroke.currentColorNumber = KeyStroke.currentColorNumber == 6 ? 0 : KeyStroke.currentColorNumber + 1;
            btn.setMessage(net.minecraft.network.chat.Component.literal("Text color: " + COLORS[KeyStroke.currentColorNumber]));
        }).bounds(cx - 70, cy - 6, 140, 20).build();
        this.showMouseBtn = Button.builder(net.minecraft.network.chat.Component.literal("Show mouse buttons: " + (KeyStroke.d ? "On" : "Off")), btn -> {
            KeyStroke.d = !KeyStroke.d;
            btn.setMessage(net.minecraft.network.chat.Component.literal("Show mouse buttons: " + (KeyStroke.d ? "On" : "Off")));
        }).bounds(cx - 70, cy + 16, 140, 20).build();
        this.outlineBtn = Button.builder(net.minecraft.network.chat.Component.literal("Outline: " + (KeyStroke.f ? "On" : "Off")), btn -> {
            KeyStroke.f = !KeyStroke.f;
            btn.setMessage(net.minecraft.network.chat.Component.literal("Outline: " + (KeyStroke.f ? "On" : "Off")));
        }).bounds(cx - 70, cy + 38, 140, 20).build();
        this.addRenderableWidget(this.modeBtn);
        this.addRenderableWidget(this.textColorBtn);
        this.addRenderableWidget(this.showMouseBtn);
        this.addRenderableWidget(this.outlineBtn);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Raven.getKeyStrokeRenderer().renderKeystrokes(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        int button = event.button();
        double mouseX = event.x();
        double mouseY = event.y();
        if (button == 0) {
            CPSCalculator.aL();
            int startX = KeyStroke.x;
            int startY = KeyStroke.y;
            int endX = startX + 74;
            int endY = startY + (KeyStroke.d ? 74 : 50);
            if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                this.dragging = true;
                this.lx = (int) mouseX;
                this.ly = (int) mouseY;
            }
        } else if (button == 1) {
            CPSCalculator.aR();
        }
        return super.mouseClicked(event, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        this.dragging = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (this.dragging) {
            KeyStroke.x = KeyStroke.x + (int) event.x() - this.lx;
            KeyStroke.y = KeyStroke.y + (int) event.y() - this.ly;
            this.lx = (int) event.x();
            this.ly = (int) event.y();
        }
        return super.mouseDragged(event, deltaX, deltaY);
    }

    public boolean shouldPause() {
        return false;
    }
}

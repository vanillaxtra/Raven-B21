package keystrokesmod.clickgui;

import keystrokesmod.Raven;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.clickgui.components.impl.BindComponent;
import keystrokesmod.clickgui.components.impl.CategoryComponent;
import keystrokesmod.clickgui.components.impl.ModuleComponent;
import keystrokesmod.client.version.Version;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.client.CommandLine;
import keystrokesmod.module.impl.client.Gui;
import keystrokesmod.utility.Commands;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Timer;
import keystrokesmod.utility.Utils;
import keystrokesmod.utility.shader.BlurUtils;
import keystrokesmod.utility.shader.RoundedUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClickGui extends Screen {
    private ScheduledFuture<?> sf;
    private Timer logoSmoothWidth;
    private Timer logoSmoothLength;
    private Timer smoothEntity;
    private Timer backgroundFade;
    private Timer blurSmooth;
    private EditBox commandLineInput;
    private Button commandLineSend;
    public static ArrayList<CategoryComponent> categories;
    public int originalScale;
    public int previousScale;
    private static boolean isNotFirstOpen;

    private final String clientName;
    private final String clientVersion;
    private final String developer = "olzi, hus, key, lquifi, tinywifi";
    private final int color = (new Color(57, 146, 229)).getRGB();

    private boolean clickGuiOpen = false;
    private long openedTime;

    public float updates;
    public long last;
    private float cached;

    public ClickGui() {
        super(net.minecraft.network.chat.Component.literal("Raven"));
        this.clientName = Version.getClientName();
        this.clientVersion = Version.getVersion();
        categories = new ArrayList<>();
        int y = 5;
        for (Module.category c : Module.category.values()) {
            CategoryComponent categoryComponent = new CategoryComponent(c);
            categoryComponent.setY(y, false);
            categories.add(categoryComponent);
            y += 20;
        }
    }

    public void initMain() {
        (this.logoSmoothWidth = this.smoothEntity = this.blurSmooth = this.backgroundFade = new Timer(500.0F)).start();
        this.sf = Raven.getScheduledExecutor().schedule(() -> {
            (this.logoSmoothLength = new Timer(650.0F)).start();
        }, 650L, TimeUnit.MILLISECONDS);
    }

    private void applyGuiScale() {
        originalScale = Mc.mc().options.guiScale().get();
        Mc.mc().options.guiScale().set((int) Gui.guiScale.getInput() + 1);
        Mc.mc().resizeDisplay();
    }

    @Override
    protected void init() {
        applyGuiScale();
        if (!isNotFirstOpen) {
            isNotFirstOpen = true;
            this.previousScale = (int) Gui.guiScale.getInput();
        }
        if (this.previousScale != Gui.guiScale.getInput()) {
            for (CategoryComponent categoryComponent : categories) {
                categoryComponent.limitPositions();
            }
        }
        int scaledHeight = Mc.mc().getWindow().getGuiScaledHeight();
        for (CategoryComponent categoryComponent : categories) {
            categoryComponent.setScreenHeight(scaledHeight);
        }

        int inputX = 22;
        if (CommandLine.opened) {
            int r = CommandLine.animate.isToggled() ? CommandLine.animation.getValueInt(0, 200, 2) : 200;
            if (CommandLine.closed) {
                r = 200 - r;
            }
            inputX = r - 178;
        }

        this.commandLineInput = new EditBox(this.font, inputX, this.height - 100, 150, 20, net.minecraft.network.chat.Component.empty());
        this.commandLineInput.setMaxLength(256);
        this.addWidget(this.commandLineInput);

        this.commandLineSend = Button.builder(net.minecraft.network.chat.Component.literal("Send"), btn -> {
            Commands.rCMD(this.commandLineInput.getValue());
            this.commandLineInput.setValue("");
        }).bounds(inputX, this.height - 70, 150, 20).build();
        this.commandLineSend.visible = CommandLine.opened;
        this.addRenderableWidget(this.commandLineSend);

        this.previousScale = (int) Gui.guiScale.getInput();
        super.init();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (Gui.backgroundBlur.getInput() != 0) {
            BlurUtils.prepareBlur();
            RoundedUtils.drawRound(0, 0, this.width, this.height, 0.0f, Color.black);
            float inputToRange = (float) (3 * ((Gui.backgroundBlur.getInput() + 35) / 100));
            BlurUtils.blurEnd(2, this.blurSmooth.getValueFloat(0, inputToRange, 1));
        }
        if (Gui.darkBackground.isToggled()) {
            int alpha = (int) (this.backgroundFade.getValueFloat(0.0F, 0.7F, 2) * 255.0F);
            context.fill(0, 0, this.width, this.height, alpha << 24);
        }

        int r;
        if (!Gui.removeWatermark.isToggled()) {
            int h = this.height / 4;
            int wd = this.width / 2;
            int w_c = 30 - this.logoSmoothWidth.getValueInt(0, 30, 3);
            context.drawCenteredString(this.font, net.minecraft.network.chat.Component.literal("r"), wd + 1 - w_c, h - 25, Utils.getChroma(2L, 1500L));
            context.drawCenteredString(this.font, net.minecraft.network.chat.Component.literal("a"), wd - w_c, h - 15, Utils.getChroma(2L, 1200L));
            context.drawCenteredString(this.font, net.minecraft.network.chat.Component.literal("v"), wd - w_c, h - 5, Utils.getChroma(2L, 900L));
            context.drawCenteredString(this.font, net.minecraft.network.chat.Component.literal("e"), wd - w_c, h + 5, Utils.getChroma(2L, 600L));
            context.drawCenteredString(this.font, net.minecraft.network.chat.Component.literal("n"), wd - w_c, h + 15, Utils.getChroma(2L, 300L));
            context.drawCenteredString(this.font, net.minecraft.network.chat.Component.literal("bS++"), wd + 1 + w_c, h + 30, Utils.getChroma(2L, 0L));
            context.fill(wd - 10 - w_c, h - 30, wd - 10 - w_c + 1, h + 43, Color.WHITE.getRGB());
            context.fill(wd + 10 + w_c, h - 30, wd + 10 + w_c + 1, h + 43, Color.WHITE.getRGB());
            if (this.logoSmoothLength != null) {
                r = this.logoSmoothLength.getValueInt(0, 20, 2);
                context.fill(wd - 10, h - 29, wd - 10 + r, h - 28, -1);
                context.fill(wd + 10 - r, h + 42, wd + 10, h + 43, -1);
            }
        }

        for (CategoryComponent c : categories) {
            c.render(context);
            c.mousePosition(mouseX, mouseY);
            for (Component m : c.getModules()) {
                m.drawScreen(mouseX, mouseY);
            }
        }

        if (!Gui.removePlayerModel.isToggled() && Mc.player() != null) {
            int entityX = this.width + 15 - this.smoothEntity.getValueInt(0, 40, 2);
            int entityY = this.height - 10;
            InventoryScreen.renderEntityInInventoryFollowsMouse(context,
                    entityX - 20, entityY - 60, entityX + 20, entityY + 10, 40, 0.0625F,
                    (float) (this.width - 25 - mouseX), (float) (this.height - 50 - mouseY),
                    Mc.player());
        }

        onRenderTick(context);

        if (CommandLine.opened) {
            if (!this.commandLineSend.visible) {
                this.commandLineSend.visible = true;
            }

            r = CommandLine.animate.isToggled() ? CommandLine.animation.getValueInt(0, 200, 2) : 200;
            if (CommandLine.closed) {
                r = 200 - r;
                if (r == 0) {
                    CommandLine.closed = false;
                    CommandLine.opened = false;
                    this.commandLineSend.visible = false;
                }
            }
            context.fill(0, 0, r, this.height, 0xBF404040);
            context.fill(0, this.height - 345, r, this.height - 344, -1);
            context.fill(0, this.height - 115, r, this.height - 114, -1);
            context.fill(r - 1, 0, r, this.height, -1);
            Commands.rc(this.font, this.height, r, Mc.mc().getWindow().getGuiScale());
            int x2 = r - 178;
            this.commandLineInput.setX(x2);
            this.commandLineSend.setX(x2);
            this.commandLineInput.render(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
        } else if (CommandLine.closed) {
            CommandLine.closed = false;
        }
    }

    private void onRenderTick(GuiGraphics context) {
        if (!clickGuiOpen && Mc.mc().screen instanceof ClickGui) {
            clickGuiOpen = true;
            initTimer(500.0F);
            startTimer();
            openedTime = System.currentTimeMillis();
        } else if (!(Mc.mc().screen instanceof ClickGui)) {
            clickGuiOpen = false;
        } else {
            int y = this.height + (8 - getValueInt(0, 30, 2));
            context.drawString(this.font, clientName + "-" + clientVersion, 4, y, color);

            long elapsedTime = System.currentTimeMillis() - openedTime + 50L;
            int characterIndex = (int) (elapsedTime / 200L);
            y += this.font.lineHeight + 1;

            if (characterIndex < developer.length()) {
                StringBuilder obfuscated = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < developer.length(); ++i) {
                    char currentChar = i < characterIndex ? developer.charAt(i) : (char) (random.nextInt(26) + 'a');
                    obfuscated.append(currentChar);
                }
                context.drawString(this.font, "devs. " + obfuscated, 4, y, color);
            } else {
                context.drawString(this.font, "devs. " + developer, 4, y, color);
            }
        }
    }

    public void initTimer(float updates) {
        this.updates = updates;
    }

    public void startTimer() {
        this.cached = 0.0F;
        this.last = System.currentTimeMillis();
    }

    public float getValueFloat(float begin, float end, int type) {
        if (this.cached == end) {
            return this.cached;
        }
        float t = (float) (System.currentTimeMillis() - this.last) / this.updates;
        switch (type) {
            case 1:
                t = t < 0.5F ? 4.0F * t * t * t : (t - 1.0F) * (2.0F * t - 2.0F) * (2.0F * t - 2.0F) + 1.0F;
                break;
            case 2:
                t = (float) (1.0D - Math.pow((double) (1.0F - t), 5.0D));
                break;
        }
        float value = begin + t * (end - begin);
        if ((end > begin && value > end) || (end < begin && value < end)) {
            value = end;
        }
        if (value == end) {
            this.cached = value;
        }
        return value;
    }

    public int getValueInt(int begin, int end, int type) {
        return Math.round(this.getValueFloat((float) begin, (float) end, type));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        double mouseX = event.x();
        double mouseY = event.y();
        int mouseButton = event.button();
        int x = (int) mouseX;
        int y = (int) mouseY;
        if (mouseButton == 0) {
            boolean draggingAssigned = false;
            for (int i = categories.size() - 1; i >= 0; i--) {
                CategoryComponent category = categories.get(i);
                if (!draggingAssigned && category.draggable(x, y)) {
                    category.overTitle(true);
                    category.xx = x - category.getX();
                    category.yy = y - category.getY();
                    category.dragging = true;
                    draggingAssigned = true;
                } else {
                    category.overTitle(false);
                }
            }
        }

        if (mouseButton == 1) {
            boolean toggled = false;
            for (int i = categories.size() - 1; i >= 0; i--) {
                CategoryComponent category = categories.get(i);
                if (!toggled && category.overTitle(x, y)) {
                    category.mouseClicked(!category.isOpened());
                    toggled = true;
                }
            }
        }

        for (CategoryComponent category : categories) {
            if (category.isOpened() && !category.getModules().isEmpty() && category.overRect(x, y)) {
                for (ModuleComponent component : category.getModules()) {
                    if (component.onClick(x, y, mouseButton)) {
                        category.openModule(component);
                    }
                }
            }
        }

        if (CommandLine.opened) {
            this.commandLineInput.mouseClicked(event, doubled);
        }
        return super.mouseClicked(event, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();
        int x = (int) mouseX;
        int y = (int) mouseY;
        if (button == 0) {
            for (CategoryComponent category : categories) {
                category.overTitle(false);
                if (category.isOpened() && !category.getModules().isEmpty()) {
                    for (Component module : category.getModules()) {
                        module.mouseReleased(x, y, button);
                    }
                }
            }
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0) {
            int wheel = verticalAmount > 0 ? 1 : -1;
            for (CategoryComponent category : categories) {
                category.onScroll(wheel);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        int scanCode = event.scancode();
        int modifiers = event.modifiers();
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && !binding()) {
            this.minecraft.setScreen(null);
            return true;
        }

        for (CategoryComponent category : categories) {
            if (category.isOpened() && !category.getModules().isEmpty()) {
                for (Component module : category.getModules()) {
                    module.keyPressed(keyCode, scanCode, modifiers);
                }
            }
        }

        if (CommandLine.opened) {
            String cm = this.commandLineInput.getValue();
            if (keyCode == GLFW.GLFW_KEY_ENTER && !cm.isEmpty()) {
                Commands.rCMD(this.commandLineInput.getValue());
                this.commandLineInput.setValue("");
                return true;
            }
        }
        return super.keyPressed(event);
    }

    public void onClose() {
        this.logoSmoothLength = null;
        if (this.sf != null) {
            this.sf.cancel(true);
            this.sf = null;
        }
        for (CategoryComponent c : categories) {
            c.dragging = false;
            for (Component m : c.getModules()) {
                m.onGuiClosed();
            }
        }
        Mc.mc().options.guiScale().set(originalScale);
        Mc.mc().resizeDisplay();
    }

    public boolean shouldPause() {
        return false;
    }

    private boolean binding() {
        for (CategoryComponent c : categories) {
            for (ModuleComponent m : c.getModules()) {
                for (Component component : m.settings) {
                    if (component instanceof BindComponent bind && bind.isBinding) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void onSliderChange() {
        for (CategoryComponent c : categories) {
            for (ModuleComponent m : c.getModules()) {
                m.onSliderChange();
            }
        }
    }
}

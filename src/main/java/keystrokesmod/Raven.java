package keystrokesmod;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import keystrokesmod.clickgui.ClickGui;
import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.EntityJoinWorldEvent;
import keystrokesmod.event.PostProfileLoadEvent;
import keystrokesmod.event.PostSetSliderEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.keystroke.KeyStrokeConfigScreen;
import keystrokesmod.keystroke.KeyStrokeRenderer;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.script.ScriptDefaults;
import keystrokesmod.script.ScriptManager;
import keystrokesmod.script.classes.Entity;
import keystrokesmod.script.classes.NetworkPlayer;
import keystrokesmod.updates.UpdateChecker;
import keystrokesmod.utility.CPSCalculator;
import keystrokesmod.utility.Debugger;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.ModHelper;
import keystrokesmod.utility.MovementFix;
import keystrokesmod.utility.PacketsHandler;
import keystrokesmod.utility.Ping;
import keystrokesmod.utility.Reflection;
import keystrokesmod.utility.Utils;
import keystrokesmod.utility.command.CommandManager;
import keystrokesmod.utility.profile.Profile;
import keystrokesmod.utility.profile.ProfileManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class Raven implements ClientModInitializer {
    public static final String MOD_ID = "raven-bS";
    public static final String VERSION = "2.0.0";

    public static boolean debug = false;
    public static Minecraft mc;
    private static KeyStrokeRenderer keyStrokeRenderer;
    private static boolean isKeyStrokeConfigGuiToggled;
    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);
    private static final ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;
    public static ProfileManager profileManager;
    public static ScriptManager scriptManager;
    public static CommandManager commandManager;
    public static Profile currentProfile;
    public static PacketsHandler packetsHandler;
    private static boolean firstLoad;

    @Override
    public void onInitializeClient() {
        mc = Mc.mc();
        Runtime.getRuntime().addShutdownHook(new Thread(scheduledExecutor::shutdown));
        Runtime.getRuntime().addShutdownHook(new Thread(cachedExecutor::shutdown));

        RavenMetrics.register();

        moduleManager = new ModuleManager();
        RavenEventBus.register(new RavenTickHandler());
        ClientTickEvents.START_CLIENT_TICK.register(client -> RavenEventBus.post(new ClientTickEvent(ClientTickEvent.Phase.START)));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            RavenEventBus.post(new ClientTickEvent(ClientTickEvent.Phase.END));
            if (isKeyStrokeConfigGuiToggled) {
                isKeyStrokeConfigGuiToggled = false;
                client.setScreen(new KeyStrokeConfigScreen());
            }
        });

        net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register((context, tickCounter) -> {
            RavenEventBus.post(new keystrokesmod.event.RenderTickEvent(keystrokesmod.event.RenderTickEvent.Phase.END));
        });
        RavenEventBus.register(new Debugger());
        RavenEventBus.register(new CPSCalculator());
        RavenEventBus.register(new MovementFix());
        RavenEventBus.register(new Ping());
        RavenEventBus.register(packetsHandler = new PacketsHandler());
        RavenEventBus.register(new ModHelper());
        Reflection.getFields();

        clickGui = new ClickGui();
        moduleManager.register();

        scriptManager = new ScriptManager();
        keyStrokeRenderer = new KeyStrokeRenderer();
        RavenEventBus.register(keyStrokeRenderer);
        profileManager = new ProfileManager();
        ScriptDefaults.reloadModules();
        scriptManager.loadScripts();
        profileManager.loadProfiles();
        profileManager.loadProfile("default");
        Reflection.setKeyMappings();
        commandManager = new CommandManager();

        new UpdateChecker();

    }

    public static class RavenTickHandler {
        @SubscribeEvent
        public void onTick(ClientTickEvent e) {
            if (e.phase == ClientTickEvent.Phase.END) {
                if (Utils.nullCheck()) {
                    if (Mc.player().tickCount % 6000 == 0) {
                        Entity.clearCache();
                        NetworkPlayer.clearCache();
                        if (Debugger.BACKGROUND) {
                            Utils.sendMessage("&aticks % 6000 == 0 &7reached, clearing script caches.");
                        }
                    }
                    if (Reflection.sendMessage) {
                        Utils.sendMessage("&cThere was an error, relaunch the game.");
                        Reflection.sendMessage = false;
                    }
                    for (Module module : getModuleManager().getModules()) {
                        if (Mc.mc().screen == null && module.canBeEnabled()) {
                            module.onKeyBind();
                        } else if (Mc.mc().screen instanceof ClickGui) {
                            module.guiUpdate();
                        }
                        if (module.isEnabled()) {
                            module.onUpdate();
                        }
                    }
                    if (Mc.mc().screen == null && scriptManager != null) {
                        for (Module module : scriptManager.scripts.values()) {
                            module.onKeyBind();
                        }
                    }
                }
            } else {
                if (Mc.mc().screen == null && Utils.nullCheck() && profileManager != null) {
                    for (Profile profile : profileManager.profiles) {
                        profile.getModule().onKeyBind();
                    }
                }
            }
        }

        @SubscribeEvent
        public void onPostProfileLoad(PostProfileLoadEvent e) {
            clickGui.onSliderChange();
        }

        @SubscribeEvent
        public void onPostSetSlider(PostSetSliderEvent e) {
            clickGui.onSliderChange();
        }

        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent e) {
            if (e.entity == Mc.player()) {
                if (!firstLoad) {
                    firstLoad = true;
                    scriptManager.loadScripts();
                }
                Entity.clearCache();
                NetworkPlayer.clearCache();
                if (Debugger.BACKGROUND) {
                    Utils.sendMessage("&enew world&7, clearing script caches.");
                }
            }
        }
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    public static ExecutorService getCachedExecutor() {
        return cachedExecutor;
    }

    public static KeyStrokeRenderer getKeyStrokeRenderer() {
        return keyStrokeRenderer;
    }

    public static void toggleKeyStrokeConfigGui() {
        isKeyStrokeConfigGuiToggled = true;
    }
}

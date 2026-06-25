package keystrokesmod.utility;

import keystrokesmod.Raven;
import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;

public class Debugger {
    public static boolean MIXIN;
    public static boolean BACKGROUND;

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        if (!Raven.debug || event.phase != RenderTickEvent.Phase.END || !Mc.nullCheck()) {
            return;
        }
        if (Mc.mc().screen == null) {
            RenderUtils.renderBPS(true, true);
        }
    }

    public static void debugMixin(Object obj, String message) {
        if (!MIXIN) {
            return;
        }
        Utils.sendMessage("&d" + obj.getClass().getSimpleName() + "&7: " + message);
    }
}

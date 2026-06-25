package keystrokesmod.script;

import keystrokesmod.Raven;
import keystrokesmod.event.*;
import keystrokesmod.module.Module;
import keystrokesmod.script.classes.Entity;
import keystrokesmod.script.classes.PlayerState;
import keystrokesmod.script.packets.clientbound.SPacket;
import keystrokesmod.script.packets.serverbound.CPacket;
import keystrokesmod.script.packets.serverbound.PacketHandler;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;

public class ScriptEvents {
    public Module module;

    public ScriptEvents(Module module) {
        this.module = module;
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onChat(ClientChatReceivedEvent e) {
        if (!Utils.nullCheck()) {
            return;
        }
        final String r = Utils.stripColor(e.message.getString());
        if (r.isEmpty()) {
            return;
        }
        if (Raven.scriptManager.invokeBoolean("onChat", module, e.message.getString()) == 0) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onSendPacket(SendPacketEvent e) {
        if (e.isCanceled() || e.getPacket() == null) {
            return;
        }
        if (e.getPacket().getClass().getSimpleName().startsWith("S")) {
            return;
        }
        CPacket a = PacketHandler.convertServerBound(e.getPacket());
        if (a != null && Raven.scriptManager.invokeBoolean("onPacketSent", module, a) == 0) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onReceivePacket(ReceivePacketEvent e) {
        if (e.isCanceled() || e.getPacket() == null) {
            return;
        }
        SPacket a = PacketHandler.convertClientBound(e.getPacket());
        if (a != null && Raven.scriptManager.invokeBoolean("onPacketReceived", module, a) == 0) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = -Integer.MAX_VALUE)
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        if (!Utils.nullCheck()) {
            return;
        }
        Raven.scriptManager.invoke("onRenderWorld", module, e.tickDelta);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onPreUpdate(PreUpdateEvent e) {
        Raven.scriptManager.invoke("onPreUpdate", module);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onPostUpdate(PostUpdateEvent e) {
        Raven.scriptManager.invoke("onPostUpdate", module);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onRenderTick(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Utils.nullCheck()) {
            return;
        }
        Raven.scriptManager.invoke("onRenderTick", module, Mc.mc().getDeltaTracker().getGameTimeDeltaPartialTick(false));
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onAntiCheatFlag(AntiCheatFlagEvent e) {
        Raven.scriptManager.invoke("onAntiCheatFlag", module, e.flag, Entity.convert(e.entity));
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onGuiUpdate(GuiUpdateEvent e) {
        if (e.Screen == null) {
            return;
        }
        Raven.scriptManager.invoke("onGuiUpdate", module, e.Screen.getClass().getSimpleName(), e.opened);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onPreMotion(PreMotionEvent e) {
        PlayerState playerState = new PlayerState(e, (byte) 0);
        Raven.scriptManager.invoke("onPreMotion", module, playerState);
        if (e.isEquals(playerState)) {
            return;
        }
        if (e.getYaw() != playerState.yaw) {
            e.setYaw(playerState.yaw);
        }
        e.setPitch(playerState.pitch);
        e.setPosX(playerState.x);
        e.setPosY(playerState.y);
        e.setPosZ(playerState.z);
        e.setOnGround(playerState.onGround);
        e.setSprinting(playerState.isSprinting);
        e.setSneaking(playerState.isSneaking);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onWorldJoin(EntityJoinWorldEvent e) {
        if (e.entity == null) {
            return;
        }
        Raven.scriptManager.invoke("onWorldJoin", module, Entity.convert(e.entity));
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onPostInput(PostPlayerInputEvent e) {
        Raven.scriptManager.invoke("onPostPlayerInput", module);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onPostMotion(PostMotionEvent e) {
        Raven.scriptManager.invoke("onPostMotion", module);
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onMouse(MouseEvent e) {
        if (Raven.scriptManager.invokeBoolean("onMouse", module, e.button, e.buttonState) == 0) {
            e.setCanceled(true);
        }
    }
}

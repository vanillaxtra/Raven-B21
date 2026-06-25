package keystrokesmod.utility;

import keystrokesmod.event.PostUpdateEvent;
import keystrokesmod.event.PreMotionEvent;
import keystrokesmod.event.PreUpdateEvent;
import keystrokesmod.event.ReceivePacketEvent;
import keystrokesmod.event.SendPacketEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.ModuleManager;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import org.lwjgl.glfw.GLFW;

public class ModHelper {
    public static int inAirTicks;
    public static int groundTicks;
    public static boolean threwFireball;
    public static boolean threwFireballLow;
    public static long MAX_EXPLOSION_DIST_SQ = 10;
    private static final long FIREBALL_TIMEOUT = 500L;
    private long fireballTime = 0;

    @SubscribeEvent
    public void onPreMotion(PreMotionEvent event) {
        var player = Mc.player();
        if (inAirTicks <= 20) {
            inAirTicks = player.onGround() ? 0 : ++inAirTicks;
        } else {
            inAirTicks = 19;
        }
        groundTicks = !player.onGround() ? 0 : ++groundTicks;
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {
        if (!Mc.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof ServerboundUseItemPacket && Mc.player().getMainHandItem().is(Items.FIRE_CHARGE)) {
            if (GLFW.glfwGetMouseButton(Mc.mc().getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
                fireballTime = System.currentTimeMillis();
                threwFireball = true;
                if (Mc.player().getXRot() > 50F) {
                    threwFireballLow = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPreUpdate(PreUpdateEvent event) {
        if (fireballTime > 0 && System.currentTimeMillis() - fireballTime > FIREBALL_TIMEOUT / 3) {
            threwFireballLow = false;
            ModuleManager.velocity.disable = false;
            ModuleManager.antiKnockback.disable = false;
        }
        if (fireballTime > 0 && System.currentTimeMillis() - fireballTime > FIREBALL_TIMEOUT) {
            threwFireball = threwFireballLow = false;
            fireballTime = 0;
            ModuleManager.velocity.disable = false;
            ModuleManager.antiKnockback.disable = false;
        }
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {
        if (!Mc.nullCheck() || event.isCanceled() || threwFireball) {
            return;
        }
        if (event.getPacket() instanceof ClientboundExplodePacket explosion && threwFireball) {
            var center = explosion.center();
            if (Mc.player().blockPosition().distSqr(net.minecraft.core.BlockPos.containing(center)) <= MAX_EXPLOSION_DIST_SQ) {
                ModuleManager.velocity.disable = false;
                ModuleManager.antiKnockback.disable = false;
                threwFireball = false;
            }
        }
    }
}

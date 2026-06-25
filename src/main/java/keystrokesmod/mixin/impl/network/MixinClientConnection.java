package keystrokesmod.mixin.impl.network;

import io.netty.channel.ChannelHandlerContext;
import keystrokesmod.event.NoEventPacketEvent;
import keystrokesmod.event.RavenEventBus;
import keystrokesmod.event.ReceivePacketEvent;
import keystrokesmod.event.SendPacketEvent;
import keystrokesmod.utility.PacketUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class MixinClientConnection {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (PacketUtils.skipSendEvent.contains(packet)) {
            RavenEventBus.post(new NoEventPacketEvent(packet));
            PacketUtils.skipSendEvent.remove(packet);
            return;
        }

        SendPacketEvent sendPacketEvent = new SendPacketEvent(packet);
        RavenEventBus.post(sendPacketEvent);

        if (sendPacketEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (PacketUtils.skipReceiveEvent.contains(packet)) {
            PacketUtils.skipReceiveEvent.remove(packet);
            return;
        }

        ReceivePacketEvent receivePacketEvent = new ReceivePacketEvent(packet);
        RavenEventBus.post(receivePacketEvent);

        if (receivePacketEvent.isCanceled()) {
            ci.cancel();
        }
    }
}

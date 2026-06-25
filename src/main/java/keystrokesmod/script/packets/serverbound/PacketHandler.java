package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.packets.PacketMappings;
import keystrokesmod.script.packets.clientbound.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;

public class PacketHandler {
    public static CPacket convertServerBound(Packet<?> packet) {
        if (packet == null || packet.getClass().getSimpleName().startsWith("S")) {
            return null;
        }
        Class<? extends CPacket> asClass = PacketMappings.minecraftToScriptC.get(packet.getClass());
        CPacket newPacket;
        if (asClass != null) {
            if (packet instanceof ServerboundMovePlayerPacket) {
                newPacket = new C03((ServerboundMovePlayerPacket) packet, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
            } else if (packet instanceof ServerboundChatPacket) {
                newPacket = new C01((ServerboundChatPacket) packet, (byte) 0);
            } else if (packet instanceof ServerboundSetCarriedItemPacket) {
                newPacket = new C09((ServerboundSetCarriedItemPacket) packet, true);
            } else if (packet instanceof ServerboundUseItemOnPacket blockPacket) {
                newPacket = new C08(blockPacket);
            } else if (packet instanceof ServerboundUseItemPacket itemPacket) {
                newPacket = new C08(itemPacket);
            } else {
                try {
                    newPacket = asClass.getConstructor(packet.getClass()).newInstance(packet);
                } catch (Exception e) {
                    newPacket = new CPacket(packet);
                }
            }
        } else {
            newPacket = new CPacket(packet);
        }
        return newPacket;
    }

    public static SPacket convertClientBound(Packet<?> packet) {
        Class<? extends SPacket> asClass = PacketMappings.minecraftToScriptS.get(packet.getClass());
        SPacket newPacket;
        if (asClass != null) {
            if (packet instanceof ClientboundCommandSuggestionsPacket) {
                newPacket = new S3A((ClientboundCommandSuggestionsPacket) packet, (byte) 0);
            } else if (packet instanceof ClientboundBlockUpdatePacket) {
                newPacket = new S23((ClientboundBlockUpdatePacket) packet, (byte) 0);
            } else if (packet instanceof ClientboundSystemChatPacket gameMessage) {
                newPacket = new S02(gameMessage);
            } else if (packet instanceof net.minecraft.network.protocol.game.ClientboundPlayerChatPacket chatMessage) {
                newPacket = new S02(chatMessage);
            } else if (packet instanceof ClientboundSetTitleTextPacket title) {
                newPacket = new S45(title);
            } else if (packet instanceof ClientboundSetSubtitleTextPacket subtitle) {
                newPacket = new S45(subtitle);
            } else if (packet instanceof ClientboundSetTitlesAnimationPacket fade) {
                newPacket = new S45(fade);
            } else {
                try {
                    newPacket = asClass.getConstructor(packet.getClass()).newInstance(packet);
                } catch (Exception e) {
                    newPacket = new SPacket(packet);
                }
            }
        } else {
            newPacket = new SPacket(packet);
        }
        return newPacket;
    }

    public static Packet<?> convertCPacket(CPacket cPacket) {
        try {
            if (cPacket instanceof C0A) {
                return new ServerboundSwingPacket(net.minecraft.world.InteractionHand.MAIN_HAND);
            } else if (cPacket instanceof C0B) {
                return ((C0B) cPacket).convert();
            } else if (cPacket instanceof C0D) {
                return ((C0D) cPacket).convert();
            } else if (cPacket instanceof C09) {
                return ((C09) cPacket).convert();
            } else if (cPacket instanceof C0E) {
                return ((C0E) cPacket).convert();
            } else if (cPacket instanceof C0F) {
                return ((C0F) cPacket).convert();
            } else if (cPacket instanceof C08) {
                return ((C08) cPacket).convert();
            } else if (cPacket instanceof C07) {
                return ((C07) cPacket).convert();
            } else if (cPacket instanceof C01) {
                return ((C01) cPacket).convert();
            } else if (cPacket instanceof C02) {
                return ((C02) cPacket).convert();
            } else if (cPacket instanceof C03) {
                return cPacket.packet;
            } else if (cPacket instanceof C10) {
                return ((C10) cPacket).convert();
            } else if (cPacket instanceof C13) {
                return ((C13) cPacket).convert();
            } else if (cPacket instanceof C16) {
                return ((C16) cPacket).convert();
            }
        } catch (Exception e) {
            if (cPacket != null && cPacket.packet != null && !cPacket.name.startsWith("S")) {
                return cPacket.packet;
            }
            return null;
        }
        if (cPacket == null || cPacket.packet == null) {
            return null;
        }
        return cPacket.packet;
    }
}

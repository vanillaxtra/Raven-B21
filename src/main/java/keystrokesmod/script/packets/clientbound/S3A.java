package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;

public class S3A extends SPacket {
    public String[] matches;

    public S3A(ClientboundCommandSuggestionsPacket packet, byte f) {
        super(packet);
        this.matches = new String[0];
    }

    public S3A(String[] matches) {
        super(new ClientboundCommandSuggestionsPacket(0, new com.mojang.brigadier.suggestion.Suggestions(com.mojang.brigadier.context.StringRange.at(0), java.util.List.of())));
        this.matches = matches;
    }
}

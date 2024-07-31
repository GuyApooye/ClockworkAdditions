package com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkadditions.util.PedalsInputKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.platform.api.network.C2SCWPacket;
import org.valkyrienskies.clockwork.platform.api.network.ServerNetworkContext;

import java.util.Objects;
import java.util.Set;

public class PedalsDrivingPacket implements C2SCWPacket {
    private final int seatId;
    @NotNull
    private final Set<PedalsInputKey> pressedKeys;

    public PedalsDrivingPacket(int seatId, @NotNull Set<PedalsInputKey> pressedKeys) {
        super();
        this.seatId = seatId;
        this.pressedKeys = pressedKeys;
    }

    public PedalsDrivingPacket(@NotNull FriendlyByteBuf buffer) {
        super();
        this.seatId = buffer.readInt();
        this.pressedKeys = PedalsInputKey.fromInt(buffer.readInt());
    }

    public void handle(@NotNull ServerNetworkContext context) {
        context.enqueueWork(() -> handle$lambda$0(context, PedalsDrivingPacket.this));
        context.setPacketHandled(true);
    }

    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(this.seatId);
        buffer.writeInt(PedalsInputKey.asInt(this.pressedKeys));
    }

    private static void handle$lambda$0(ServerNetworkContext $context, PedalsDrivingPacket this$0) {
        Level level = $context.getSender().level;
        Entity entity = level.getEntity(this$0.seatId);
        if (entity instanceof PedalsEntity && Objects.equals($context.getSender(), entity.getFirstPassenger())) {
            BlockEntity be = level.getBlockEntity(entity.blockPosition());
            PedalsBlockEntity var10000 = be instanceof PedalsBlockEntity ? (PedalsBlockEntity)be : null;
            if ((be instanceof PedalsBlockEntity ? (PedalsBlockEntity)be : null) != null) {
                var10000.updateInput(this$0.pressedKeys);
            }
        }

    }
}

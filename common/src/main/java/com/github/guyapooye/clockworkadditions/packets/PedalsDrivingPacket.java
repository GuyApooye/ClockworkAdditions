package com.github.guyapooye.clockworkadditions.packets;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.platform.api.network.C2SCWPacket;
import org.valkyrienskies.clockwork.platform.api.network.ServerNetworkContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class PedalsDrivingPacket implements C2SCWPacket {
    private final int seatId;
    @NotNull
    private final Collection<Integer> pressedKeys;

    public PedalsDrivingPacket(int seatId, @NotNull Collection<Integer> pressedKeys) {
        this.seatId = seatId;
        this.pressedKeys = pressedKeys;
    }

    public PedalsDrivingPacket(@NotNull FriendlyByteBuf buffer) {
        this.seatId = buffer.readInt();
        pressedKeys = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            pressedKeys.add(buffer.readVarInt());
    }

    public void handle(@NotNull ServerNetworkContext context) {
        context.enqueueWork(() -> handle$lambda$0(context, this));
        context.setPacketHandled(true);
    }

    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(this.seatId);
        buffer.writeVarInt(pressedKeys.size());
        pressedKeys.forEach(buffer::writeVarInt);
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

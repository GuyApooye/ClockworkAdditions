package com.github.guyapooye.clockworkadditions.packets.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class HandlebarDrivingPacket extends HandlebarPacketBase {
    private final Collection<Integer> pressedKeys;

    public HandlebarDrivingPacket(Collection<Integer> pressedKeys, BlockPos pos) {
        super(pos);
        this.pressedKeys = pressedKeys;
    }
    public HandlebarDrivingPacket(@NotNull FriendlyByteBuf buffer) {
        super(buffer);
        pressedKeys = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            pressedKeys.add(buffer.readVarInt());
    }
    @Override
    public void handle(Player player, HandlebarBlockEntity be) {
        be.updateInput(pressedKeys);
    }

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
        super.write(friendlyByteBuf);
        friendlyByteBuf.writeVarInt(pressedKeys.size());
        pressedKeys.forEach(friendlyByteBuf::writeVarInt);
    }
}

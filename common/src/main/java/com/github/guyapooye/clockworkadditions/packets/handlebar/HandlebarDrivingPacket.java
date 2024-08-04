package com.github.guyapooye.clockworkadditions.packets.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import com.github.guyapooye.clockworkadditions.util.InputKey;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.redstone.link.controller.LecternControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.platform.api.network.C2SCWPacket;
import org.valkyrienskies.clockwork.platform.api.network.ServerNetworkContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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

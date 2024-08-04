package com.github.guyapooye.clockworkadditions.packets.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class HandlebarStopDrivingPacket extends HandlebarPacketBase{
    public HandlebarStopDrivingPacket(BlockPos pos) {
        super(pos);
    }
    public HandlebarStopDrivingPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void handle(Player player, HandlebarBlockEntity be) {
        be.tryStopUsing();
    }
}

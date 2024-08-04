package com.github.guyapooye.clockworkadditions.packets.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.platform.api.network.C2SCWPacket;
import org.valkyrienskies.clockwork.platform.api.network.ServerNetworkContext;

public abstract class HandlebarPacketBase implements C2SCWPacket {
    private final BlockPos pos;

    protected HandlebarPacketBase(BlockPos pos) {
        this.pos = pos;
    }
    public HandlebarPacketBase(@NotNull FriendlyByteBuf buffer) {
        this.pos = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());
    }

    protected boolean inBlock() {
        return pos != null;
    }
    @Override
    public void handle(@NotNull ServerNetworkContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            BlockEntity be = player.level.getBlockEntity(pos);
            if (!(be instanceof HandlebarBlockEntity))
                return;
            handle(player, (HandlebarBlockEntity) be);
        });
    }
    protected abstract void handle(Player player, HandlebarBlockEntity be);
    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(pos.getX());
        friendlyByteBuf.writeInt(pos.getY());
        friendlyByteBuf.writeInt(pos.getZ());
    }
}

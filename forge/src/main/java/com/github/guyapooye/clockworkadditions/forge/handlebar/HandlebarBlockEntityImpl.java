package com.github.guyapooye.clockworkadditions.forge.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HandlebarBlockEntityImpl extends HandlebarBlockEntity {
    public HandlebarBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    protected void stopUsing(Player player) {
        user = null;
        if (player != null)
            player.getPersistentData().remove("IsUsingHandlebar");
        deactivatedThisTick = true;
        sendData();
    }
    protected void startUsing(Player player) {
        user = player.getUUID();
        player.getPersistentData().putBoolean("IsUsingHandlebar", true);
        sendData();
    }
    public boolean playerIsUsingHandle(Player player) {
        return player.getPersistentData().contains("IsUsingHandlebar");
    }
}

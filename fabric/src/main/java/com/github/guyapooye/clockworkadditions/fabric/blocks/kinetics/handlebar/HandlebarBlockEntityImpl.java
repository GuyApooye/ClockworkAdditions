package com.github.guyapooye.clockworkadditions.fabric.blocks.kinetics.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import com.tterrag.registrate.fabric.EnvExecutor;
import net.fabricmc.api.EnvType;
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
            player.getCustomData().remove("IsUsingHandlebar");
        deactivatedThisTick = true;
        sendData();
    }
    protected void startUsing(Player player) {
        user = player.getUUID();
        player.getCustomData().putBoolean("IsUsingHandlebar", true);
        sendData();
    }
    public boolean playerIsUsingHandle(Player player) {
        return player.getCustomData().contains("IsUsingHandlebar");
    }

    @Override
    protected void runWhenOn() {
        if (level.isClientSide) {
            EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::tryToggleActive);
            prevUser = user;
        }
    }
}

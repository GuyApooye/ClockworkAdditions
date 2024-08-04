package com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.redstone.link.controller.LecternControllerBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class HandlebarBlock extends HorizontalKineticBlock implements IBE<HandlebarBlockEntity> {
    public HandlebarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (!player.isShiftKeyDown() && HandlebarBlockEntity.playerInRange(player, world, pos)) {
            if (!world.isClientSide) {
                withBlockEntityDo(world, pos, be -> {
                    if (be.playerIsUsingHandle(player)) {
                        be.pleaseStopUsing(player);
                    } else {
                        be.tryStartUsing(player);
                    }
                });
            }
            return InteractionResult.SUCCESS;
        }
//        withBlockEntityDo(world,pos,be -> be.pleaseStopUsing(player));
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!world.isClientSide)
                withBlockEntityDo(world,pos,be -> be.tryStopUsing());

            super.onRemove(state, world, pos, newState, isMoving);
        }
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<HandlebarBlockEntity> getBlockEntityClass() {
        return HandlebarBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HandlebarBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.HANDLEBAR.get();
    }
}

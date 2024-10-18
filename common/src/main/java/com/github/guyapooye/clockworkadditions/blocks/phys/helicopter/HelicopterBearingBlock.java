package com.github.guyapooye.clockworkadditions.blocks.phys.helicopter;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.github.guyapooye.clockworkadditions.registries.ShapesRegistry;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class HelicopterBearingBlock extends BearingBlock implements IBE<HelicopterBearingBlockEntity> {
    public HelicopterBearingBlock(Properties properties) {
        super(properties);
    }

    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.FAIL;
        } else if (player.isShiftKeyDown()) {
            return InteractionResult.FAIL;
        } else if (player.getItemInHand(handIn).isEmpty()) {
            if (worldIn.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(worldIn, pos, HelicopterBearingBlock::use);
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public Class<HelicopterBearingBlockEntity> getBlockEntityClass() {
        return HelicopterBearingBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HelicopterBearingBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.HELICOPTER_BEARING.get();
    }
    @NotNull
    public Direction.Axis getRotationAxis(@NotNull BlockState state) {
        return (state.getValue(BearingBlock.FACING)).getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return ShapesRegistry.HELI_BEARING.get(blockState.getValue(FACING));
    }

    public boolean hasShaftTowards(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Direction face) {
        return face == (state.getValue(BearingBlock.FACING).getOpposite());
    }

    private static void use(@NotNull HelicopterBearingBlockEntity te) {
        if (!te.isRunning()) {
            te.setAssembleNextTick(true);
        }
    }
}

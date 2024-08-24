package com.github.guyapooye.clockworkadditions.blocks.phys.alternator.archived;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


import java.util.Objects;

public class BasePhysicsBearingBlock extends BearingBlock implements IBE<BasePhysicsBearingBlockEntity> {

    public BasePhysicsBearingBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @NotNull
    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.FAIL;
        } else if (player.isShiftKeyDown()) {
            return InteractionResult.FAIL;
        } else if (player.getItemInHand(handIn).isEmpty()) {
            if (worldIn.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(worldIn, pos, BasePhysicsBearingBlock::use);
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @NotNull
    public Class getBlockEntityClass() {
        return BasePhysicsBearingBlockEntity.class;
    }

    @NotNull
    public  BlockEntityType<BasePhysicsBearingBlockEntity> getBlockEntityType() {
        BlockEntityType<BasePhysicsBearingBlockEntity> var10000 = BlockEntityRegistry.BASE_PHYS_BEARING.get();
        return var10000;
    }

    @NotNull
    public Direction.Axis getRotationAxis(@NotNull BlockState state) {
        Direction.Axis var10000 = (state.getValue(BearingBlock.FACING)).getAxis();
        Objects.requireNonNull(var10000, "getAxis(...)");
        return var10000;
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        VoxelShape var10000 = AllShapes.MECHANICAL_PISTON.get(state.getValue(BearingBlock.FACING));
        Objects.requireNonNull(var10000, "get(...)");
        return var10000;
    }
    public boolean hasShaftTowards(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Direction face) {
        return face == (state.getValue(BearingBlock.FACING).getOpposite());
    }

    private static void use(@NotNull BasePhysicsBearingBlockEntity te) {
        if (!te.isRunning()) {
            te.setAssembleNextTick(true);
        }
    }

}

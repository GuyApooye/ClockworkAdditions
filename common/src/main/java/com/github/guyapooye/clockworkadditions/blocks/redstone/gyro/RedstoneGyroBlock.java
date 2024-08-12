package com.github.guyapooye.clockworkadditions.blocks.redstone.gyro;

import com.github.guyapooye.clockworkadditions.blocks.redstone.AbstractFourSidedPoweredBlock;
import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class RedstoneGyroBlock extends AbstractFourSidedPoweredBlock implements IBE<RedstoneGyroBlockEntity> {


    public RedstoneGyroBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(NORTH,false)
                .setValue(SOUTH,false)
                .setValue(EAST,false)
                .setValue(WEST,false)
        );
    }
    public boolean isSignalSource(BlockState arg) {
        return true;
    }
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide) return;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level,pos);
        if (ship == null) return;
        RedstoneGyroAttachment.getOrCreate(ship).increment();
    }
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide) return;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level,pos);
        if (ship == null) return;
        RedstoneGyroAttachment.getOrCreate(ship).decrement();
    }


    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        CompoundTag tag = new CompoundTag();
        ((RedstoneGyroBlockEntity)level.getBlockEntity(pos)).write(tag,false);
        return switch (direction) {
            case DOWN, UP -> 0;
            case NORTH -> tag.getInt("SignalNorth");
            case SOUTH -> tag.getInt("SignalSouth");
            case EAST -> tag.getInt("SignalEast");
            case WEST -> tag.getInt("SignalWest");
        };
//        return 15;
    }
    public void neighborChanged(BlockState arg, Level arg2, BlockPos arg3, Block arg4, BlockPos arg5, boolean bl) {
        if (!arg.canSurvive(arg2, arg3)) {
            BlockEntity blockentity = arg.hasBlockEntity() ? arg2.getBlockEntity(arg3) : null;
            dropResources(arg, arg2, arg3, blockentity);
            arg2.removeBlock(arg3, false);
            Direction[] var8 = Direction.values();

            for (Direction direction : var8) {
                arg2.updateNeighborsAt(arg3.relative(direction), this);
            }
        }
    }
    public boolean canSurvive(BlockState arg, LevelReader arg2, BlockPos arg3) {
        return canSupportRigidBlock(arg2, arg3.below());
    }
    public void updateBlockState(BlockState newState, ServerLevel level, BlockPos pos) {
        level.setBlock(pos,newState, 3);
        level.blockUpdated(pos, this);
//        updateOrDestroy();
    }

    @Override
    public Class<RedstoneGyroBlockEntity> getBlockEntityClass() {
        return RedstoneGyroBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RedstoneGyroBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.REDSTONE_GYRO.get();
    }
}

package com.github.guyapooye.clockworkadditions.fabric.blocks.fluid.extensible_hose;

import com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose.ExtensibleHoseBlockEntity;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class ExtensibleHoseBlockEntityImpl extends ExtensibleHoseBlockEntity<FluidTank> implements SidedStorageBlockEntity {

    public ExtensibleHoseBlockEntityImpl(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public FluidTank createTank() {
        return new FluidTank(81000);
    }

    @Override
    public @Nullable Storage<FluidVariant> getFluidStorage(Direction side) {
        if (side == getBlockState().getValue(FACING).getOpposite()) {
            return tank;
        }
        return null;
    }
}

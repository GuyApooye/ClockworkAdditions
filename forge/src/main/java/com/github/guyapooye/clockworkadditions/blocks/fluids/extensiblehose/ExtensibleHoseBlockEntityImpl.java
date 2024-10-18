package com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class ExtensibleHoseBlockEntityImpl extends ExtensibleHoseBlockEntity<FluidTank> {
    public ExtensibleHoseBlockEntityImpl(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public FluidTank createTank() {
        return new FluidTank(1000);
    }

    public FluidTank getFluidStorage() {
        return tank;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (side == getBlockState().getValue(FACING).getOpposite() && cap == ForgeCapabilities.FLUID_HANDLER) {
            return LazyOptional.of(this::getFluidStorage).cast();
        }
        return super.getCapability(cap, side);
    }
}

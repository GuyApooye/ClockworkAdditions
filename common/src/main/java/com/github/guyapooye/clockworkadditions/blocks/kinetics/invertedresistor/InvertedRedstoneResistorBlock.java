package com.github.guyapooye.clockworkadditions.blocks.kinetics.invertedresistor;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class InvertedRedstoneResistorBlock extends AbstractEncasedShaftBlock implements IBE<InvertedRedstoneResistorBlockEntity> {
    public InvertedRedstoneResistorBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    public final void detachKinetics(@NotNull Level worldIn, BlockPos pos, boolean reAttachNextTick) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof KineticBlockEntity) {
            RotationPropagator.handleRemoved(worldIn, pos, (KineticBlockEntity)te);
            if (reAttachNextTick) {
                worldIn.scheduleTick(pos, this, 0, TickPriority.EXTREMELY_HIGH);
            }

        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        if (level.getBlockEntity(pos) instanceof KineticBlockEntity te) {
            RotationPropagator.handleAdded(level, pos, te);
        }
    }

    @NotNull
    public Class<InvertedRedstoneResistorBlockEntity> getBlockEntityClass() {
        return InvertedRedstoneResistorBlockEntity.class;
    }

    @NotNull
    public BlockEntityType<? extends InvertedRedstoneResistorBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.INVERTED_RESISTOR.get();
    }
}
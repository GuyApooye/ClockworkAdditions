package com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class HandleBarBlock extends HorizontalKineticBlock {
    public HandleBarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
}

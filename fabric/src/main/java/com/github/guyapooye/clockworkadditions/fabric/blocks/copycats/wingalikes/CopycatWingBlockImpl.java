package com.github.guyapooye.clockworkadditions.fabric.blocks.copycats.wingalikes;

import com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes.CopycatWingBlock;
import com.github.guyapooye.clockworkadditions.fabric.blocks.copycats.ICopycatBlockImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CopycatWingBlockImpl extends CopycatWingBlock implements ICopycatBlockImpl {
    public CopycatWingBlockImpl(@Nullable Properties properties) {
        super(properties);
    }

    @Override
	public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		return false;
	}
}

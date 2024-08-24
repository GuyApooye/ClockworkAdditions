package com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.content.fluids.PipeConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class HoseConnection extends PipeConnection {
    public HoseConnection(Direction side) {
        super(side);
    }

    @Override
    public boolean determineSource(Level world, BlockPos pos) {
        ExtensibleHoseBlockEntity be = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(world, pos);
        if (be == null) return false;
        BlockPos target = be.target;
        if (target == null) return false;
        return super.determineSource(world, target);
    }

    @Override
    public boolean hasFlow() {
        return super.hasFlow();
    }
}

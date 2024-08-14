package com.github.guyapooye.clockworkadditions.fabric.blocks.copycats;

import com.github.guyapooye.clockworkadditions.blocks.copycats.CWACopycatBlockEntity;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CWACopycatBlockEntityImpl extends CWACopycatBlockEntity implements RenderAttachmentBlockEntity {
    public CWACopycatBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}

package com.github.guyapooye.clockworkadditions.blocks.copycats;

import com.github.guyapooye.clockworkadditions.blocks.copycats.CWACopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.jetbrains.annotations.NotNull;

public class CWACopycatBlockEntityImpl extends CWACopycatBlockEntity {
    public CWACopycatBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public @NotNull IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(CopycatModel.MATERIAL_PROPERTY, material)
                .build();
    }
    @Override
    protected void redraw() {
        if (!isVirtual())
            requestModelDataUpdate();
        super.redraw();
    }
}

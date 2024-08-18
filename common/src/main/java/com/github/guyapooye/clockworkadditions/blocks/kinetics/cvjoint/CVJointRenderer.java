package com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CVJointRenderer extends KineticBlockEntityRenderer<CVJointBlockEntity> {
    public CVJointRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CVJointBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if (Backend.canUseInstancing(be.getLevel()))
            return;
    }
}

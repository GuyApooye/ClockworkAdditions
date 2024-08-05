package com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class HandlebarRenderer extends ShaftRenderer<PedalsBlockEntity> {
    public HandlebarRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(PedalsBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());
        float independentAngle = be.getIndependentAngle(partialTicks);

        if (!Backend.canUseInstancing(be.getLevel())) {
            SuperByteBuffer handle = CachedBufferer.partial(PartialModelRegistry.HANDLEBAR, be.getBlockState());
            handle.centre()
                    .rotateY(180 + AngleHelper.horizontalAngle(be.getBlockState().getValue(HandlebarBlock.HORIZONTAL_FACING))+independentAngle*70)
                    .unCentre();
            handle.light(light)
                    .renderInto(ms, vertexBuilder);
        }
    }
}

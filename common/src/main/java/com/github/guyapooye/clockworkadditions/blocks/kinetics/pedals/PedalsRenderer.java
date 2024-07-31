package com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.Rotate;
import com.jozufozu.flywheel.util.transform.Translate;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class PedalsRenderer extends ShaftRenderer<PedalsBlockEntity> {
    static final Vec3 pivot = VecHelper.voxelSpace(0, 8, 0);
    static final Vec3 pedalOffset = VecHelper.voxelSpace(0,3,0);

    public PedalsRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(PedalsBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());
        float independentAngle = be.getIndependentAngle(partialTicks);

        if (!Backend.canUseInstancing(be.getLevel())) {
            SuperByteBuffer crank = CachedBufferer.partial(PartialModelRegistry.PEDALS_CRANK, be.getBlockState());
            SuperByteBuffer pedalLeft = CachedBufferer.partial(PartialModelRegistry.PEDAL_LEFT, be.getBlockState());
            SuperByteBuffer pedalRight = CachedBufferer.partial(PartialModelRegistry.PEDAL_RIGHT, be.getBlockState());
            applyCrankAngle(be, independentAngle, crank);
            crank.light(light)
                    .renderInto(ms, vertexBuilder);
            applyPedalAngle(be, independentAngle, pedalLeft,1);
            pedalLeft.light(light)
                    .renderInto(ms, vertexBuilder);
            applyPedalAngle(be, independentAngle, pedalRight,-1);
            pedalRight.light(light)
                    .renderInto(ms, vertexBuilder);
        }
    }

    static <T extends Translate<T> & Rotate<T>> void applyCrankAngle(PedalsBlockEntity be, float angle, T tr) {
        tr.centre()
                .rotateY(180 + AngleHelper.horizontalAngle(be.getBlockState()
                        .getValue(PedalsBlock.HORIZONTAL_FACING)))
                .unCentre()
                .translate(pivot)
                .rotateX(-angle)
                .translateBack(pivot);
    }
    static <T extends Translate<T> & Rotate<T>> void applyPedalAngle(PedalsBlockEntity be, float angle, T tr, int reversed) {
        tr.centre()
                .rotateY(180 + AngleHelper.horizontalAngle(be.getBlockState()
                        .getValue(PedalsBlock.HORIZONTAL_FACING)))
                .unCentre()
                .translate(pivot)
                .rotateX(180-angle*70)
                .translate(pedalOffset.scale(reversed))
                .rotateX(180+angle*70)
                .translateBack(pivot)
                .translate(pedalOffset.scale(reversed));
    }
}

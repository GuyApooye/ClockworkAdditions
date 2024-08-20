package com.github.guyapooye.clockworkadditions.blocks.bearings.flap;

import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public class KineticFlapBearingRenderer extends KineticBlockEntityRenderer<KineticFlapBearingBlockEntity> {
    public KineticFlapBearingRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(@NotNull KineticFlapBearingBlockEntity be, float partialTicks, @NotNull PoseStack ms, @NotNull MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());

        if (!Backend.canUseInstancing(be.getLevel())) {
            SuperByteBuffer top = CachedBufferer.partial(PartialModelRegistry.BEARING_TOP, be.getBlockState());
            double angle = be.getInterpolatedAngle(partialTicks);
            top.rotateZ(angle);
            top.light(light)
                    .renderInto(ms, vertexBuilder);
        }
    }
}

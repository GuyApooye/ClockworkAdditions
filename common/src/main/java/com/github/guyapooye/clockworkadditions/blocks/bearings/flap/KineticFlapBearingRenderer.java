package com.github.guyapooye.clockworkadditions.blocks.bearings.flap;

import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
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
    protected void renderSafe(@NotNull KineticFlapBearingBlockEntity te, float partialTicks, @NotNull PoseStack ms, @NotNull MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
        final Direction facing = (Direction) te.getBlockState().getValue((Property) BlockStateProperties.FACING);
        PartialModel top = PartialModelRegistry.BEARING_TOP;
        SuperByteBuffer superBuffer = CachedBufferer.partial(top, te.getBlockState());
        float interpolatedAngle = ((IBearingBlockEntity) te).getInterpolatedAngle(partialTicks);
        KineticBlockEntityRenderer.kineticRotationTransform(superBuffer, te, facing.getAxis(), (float) ((double) (interpolatedAngle / (float) 180) * Math.PI), light);
        if (facing.getAxis().isHorizontal()) {
            superBuffer.rotateCentered(Direction.UP, AngleHelper.rad(AngleHelper.horizontalAngle(facing.getOpposite())));
        }

        superBuffer.rotateCentered(Direction.EAST, AngleHelper.rad((float) -90 - AngleHelper.verticalAngle(facing)));
        superBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
//        KineticFlapBearingBlockEntity var10000 = te;
//        BlockState var10003 = te.getBlockState();
        KineticBlockEntityRenderer.renderRotatingBuffer(te, this.getRotatedModel(te, te.getBlockState()), ms, buffer.getBuffer(RenderType.solid()), light);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(KineticFlapBearingBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, state
                .getValue(BearingBlock.FACING)
                .getOpposite());
    }
}

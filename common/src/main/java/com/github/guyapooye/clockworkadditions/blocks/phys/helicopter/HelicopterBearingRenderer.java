package com.github.guyapooye.clockworkadditions.blocks.phys.helicopter;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.BearingRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
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
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class HelicopterBearingRenderer extends BearingRenderer<HelicopterBearingBlockEntity> {
    public HelicopterBearingRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(HelicopterBearingBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        if (Backend.canUseInstancing(be.getLevel())) return;

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        final Direction facing = be.getBlockState()
                .getValue(BlockStateProperties.FACING);
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.BEARING_TOP, be.getBlockState());

        Ship shiptraption = be.getShiptraption();
        if (shiptraption != null) superBuffer.rotateCentered(VectorConversionsMCKt.toMinecraft(shiptraption.getTransform().getShipToWorldRotation()));

        if (facing.getAxis()
                .isHorizontal())
            superBuffer.rotateCentered(Direction.UP,
                    AngleHelper.rad(AngleHelper.horizontalAngle(facing.getOpposite())));
        superBuffer.rotateCentered(Direction.EAST, AngleHelper.rad(-90 - AngleHelper.verticalAngle(facing)));
        superBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }
}

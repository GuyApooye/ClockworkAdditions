package com.github.guyapooye.clockworkadditions.blocks.kinetics.invertedresistor;

import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class InvertedRedstoneResistorRenderer extends KineticBlockEntityRenderer<InvertedRedstoneResistorBlockEntity> {

    public InvertedRedstoneResistorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(InvertedRedstoneResistorBlockEntity te, float partialTicks, @NotNull PoseStack ms, @NotNull MultiBufferSource buffer, int light, int overlay) {
        Block block = te.getBlockState().getBlock();
        Direction.Axis boxAxis = ((IRotate)block).getRotationAxis(te.getBlockState());
        BlockPos pos = te.getBlockPos();
        float time = AnimationTickHolder.getRenderTime(te.getLevel());
        Direction[] var10000 = Iterate.directions;
        int var13 = 0;

        for(int var14 = var10000.length; var13 < var14; ++var13) {
            Direction direction = var10000[var13];
            Direction.Axis axis = direction.getAxis();
            if (boxAxis == axis) {
                float offset = KineticBlockEntityRenderer.getRotationOffsetForPosition(te, pos, axis);
                float angle = time * te.getSpeed() * 3.0F / (float)10 % (float)360;
                float modifier = 1.0F;
                modifier = te.getRotationSpeedModifier(direction);
                angle *= modifier;
                angle += offset;
                angle = angle / 180.0F * 3.1415927F;
                SuperByteBuffer superByteBuffer = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, te.getBlockState(), direction);
                KineticBlockEntityRenderer.kineticRotationTransform(superByteBuffer, te, axis, angle, light);
                superByteBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
            }
        }

        float state = 0.0F;
        BlockState resistorState = te.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        int color = Color.mixColors(2884352, 13434880, (float) te.getState() / 15.0F);
        SuperByteBuffer var10001 = CachedBufferer.partial(PartialModelRegistry.INVERTED_RESISTOR_INDICATOR, resistorState);
        SuperByteBuffer indicator = this.transform(var10001, resistorState);
        indicator.light(light).color(color).renderInto(ms, vb);
    }

    private SuperByteBuffer transform(SuperByteBuffer buffer, BlockState resistorState) {
        Direction.Axis axis = resistorState.getValue(BlockStateProperties.AXIS);
        return switch (axis) {
            case X -> buffer.rotateCentered(Direction.NORTH, (float) Math.toRadians(90.0));
            case Y -> buffer;
            case Z -> buffer.rotateCentered(Direction.EAST, (float) Math.toRadians(90.0));
        };
    }
}

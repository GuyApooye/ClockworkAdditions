package com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix4d;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import static com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointInstance.getLength;
import static com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointInstance.getMatrix;

public class CVJointRenderer extends KineticBlockEntityRenderer<CVJointBlockEntity> {
    public CVJointRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CVJointBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());

        if (!Backend.canUseInstancing(blockEntity.getLevel())) {
            SuperByteBuffer rod = CachedBufferer.partial(PartialModelRegistry.CV_JOINT_ROD, blockEntity.getBlockState());
            SuperByteBuffer connector = CachedBufferer.partial(PartialModelRegistry.CV_JOINT_CONNECTOR, blockEntity.getBlockState());
            SuperByteBuffer connector2 = CachedBufferer.partial(PartialModelRegistry.CV_JOINT_CONNECTOR, blockEntity.getBlockState());
            CVJointBlockEntity other = null;
            boolean disconnect = false;
            if (blockEntity.target == null) {
                disconnect = true;
            }
            else {
                other = BlockRegistry.CV_JOINT.get().getBlockEntity(blockEntity.getLevel(), blockEntity.target);
                if (other == null) disconnect = true;
            }
            Direction facing = blockEntity.getBlockState().getValue(DirectionalKineticBlock.FACING);
            float angle = KineticBlockEntityRenderer.getAngleForTe(blockEntity, blockEntity.getBlockPos(), facing.getAxis());
            if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) angle *= -1;

            Matrix4f bendMat = getMatrix(blockEntity,other,blockEntity.getLevel(),facing);
            double len = getLength(blockEntity,other,blockEntity.getLevel());
            if (disconnect) {
                rod
                        .centre()
                        .rotateY(AngleHelper.horizontalAngle(facing))
                        .rotateX(AngleHelper.verticalAngle(facing) + 180)
                        .rotateZRadians(angle)
                        .unCentre()
                ;
                connector.delete();
                connector2.delete();
                return;
            }
            rod
                    .centre()
                    .transform(
                            bendMat,
                            new com.mojang.math.Matrix3f())
                    .rotateY(AngleHelper.horizontalAngle(facing))
                    .rotateX(AngleHelper.verticalAngle(facing) + 180)
                    .rotateZRadians(angle)
                    .unCentre()
            ;
            rod.light(light)
                    .renderInto(ms, vertexBuilder);
            connector2
                    .centre()
                    .transform(
                            bendMat,
                            new com.mojang.math.Matrix3f())
                    .rotateY(AngleHelper.horizontalAngle(facing))
                    .rotateX(AngleHelper.verticalAngle(facing) + 180)
                    .rotateZRadians(angle)
                    .scale(0.875f)
                    .unCentre()
                    .translateZ(-len*0.375)
            ;
            connector2.light(light)
                    .renderInto(ms, vertexBuilder);
            if (blockEntity.renderConnector) {
                connector.delete();
                return;
            }
            connector
                    .centre()
                    .transform(
                            bendMat,
                            new com.mojang.math.Matrix3f())
                    .rotateY(AngleHelper.horizontalAngle(facing))
                    .rotateX(AngleHelper.verticalAngle(facing) + 180)
                    .rotateZRadians(angle)
                    .unCentre()
                    .translateZ(-len/2)
            ;
            connector.light(light)
                    .renderInto(ms, vertexBuilder);
        }
    }
}

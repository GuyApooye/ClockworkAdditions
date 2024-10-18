package com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.github.guyapooye.clockworkadditions.util.NumberUtil;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.math.Matrix4f;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector4d;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;


public class CVJointInstance extends SingleRotatingInstance<CVJointBlockEntity> implements DynamicInstance {

    final Direction facing;

    final ModelData rod;
    final ModelData connector;
    final ModelData connector2;

    public CVJointInstance(MaterialManager materialManager, CVJointBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        Material<ModelData> mat = getTransformMaterial();
        rod = mat.getModel(PartialModelRegistry.CV_JOINT_ROD).createInstance();
        connector = mat.getModel(PartialModelRegistry.CV_JOINT_CONNECTOR).createInstance();
        connector2 = mat.getModel(PartialModelRegistry.CV_JOINT_CONNECTOR).createInstance();
    }

    @Override
    public void beginFrame() {

        boolean disconnect = false;

        CVJointBlockEntity other = null;
        if (blockEntity.target == null) {
            disconnect = true;
        }
        else {
            other = BlockRegistry.CV_JOINT.get().getBlockEntity(world, blockEntity.target);
            if (other == null) disconnect = true;
        }
        float angle = KineticBlockEntityRenderer.getAngleForTe(blockEntity, blockEntity.getBlockPos(), axis);
        if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) angle *= -1;

        if (disconnect) {
            rod
                    .loadIdentity()
                    .translate(getInstancePosition())
                    .centre()
                    .rotateY(AngleHelper.horizontalAngle(facing))
                    .rotateX(AngleHelper.verticalAngle(facing) + 180)
                    .rotateZRadians(angle)
                    .unCentre()
            ;
            connector.setEmptyTransform();
            connector2.setEmptyTransform();
            return;
        }
        Matrix4f bendMat = getMatrix(blockEntity,other,world,facing);
        double len = getLength(blockEntity,other,world);
        rod
                .loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .transform(
                        bendMat,
                        new com.mojang.math.Matrix3f())
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(AngleHelper.verticalAngle(facing) + 180)
                .rotateZRadians(angle)
                .unCentre()
        ;
        connector2
                .loadIdentity()
                .translate(getInstancePosition())
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
        if (blockEntity.isOrigin) {
            connector.setEmptyTransform();
            return;
        }
        connector
                .loadIdentity()
                .translate(getInstancePosition())
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
    }
    protected static Matrix4f getMatrix(CVJointBlockEntity blockEntity, CVJointBlockEntity other, Level world, Direction facing) {
        org.joml.Vector3d dif = blockEntity.getShipToWorldClient(world).invert(new Matrix4d()).transformPosition(other.getWorldSpaceClient(world))
                .sub(VectorConversionsMCKt.toJOMLD(blockEntity.getBlockPos()).add(NumberUtil.blockPosOffset));
        double len = dif.length();
        org.joml.Vector3d dir = dif.div(len, new org.joml.Vector3d());
        org.joml.Vector3d startDir = VectorConversionsMCKt.toJOMLD(facing.getNormal());
        org.joml.Vector3d bendAxis = startDir.cross(dir, new Vector3d()).normalize();
        if (dir.equals(startDir)) bendAxis.set(1);
        double bendAmount = Math.acos(startDir.dot(dir));

        Vector4d
                bmx = new Vector4d(1, 0, 0, 0).rotateAxis(bendAmount, bendAxis.x, bendAxis.y, bendAxis.z),
                bmy = new Vector4d(0, 1, 0, 0).rotateAxis(bendAmount, bendAxis.x, bendAxis.y, bendAxis.z),
                bmz = new Vector4d(0, 0, 1, 0).rotateAxis(bendAmount, bendAxis.x, bendAxis.y, bendAxis.z);
        Matrix4d bendMat = new Matrix4d(new Matrix4d(bmx, bmy, bmz, new Vector4d(0, 0, 0, 1)));
        return VectorConversionsMCKt.toMinecraft(bendMat);
    }
    protected static double getLength(CVJointBlockEntity blockEntity, CVJointBlockEntity other, Level world) {
        return blockEntity.getShipToWorldClient(world).invert(new Matrix4d()).transformPosition(other.getWorldSpaceClient(world))
                .sub(VectorConversionsMCKt.toJOMLD(blockEntity.getBlockPos()).add(NumberUtil.blockPosOffset)).length();
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        Direction dir = facing.getOpposite();
        return getRotatingMaterial().getModel(PartialModelRegistry.CV_JOINT_BASE, blockState, dir);
    }

    @Override
    public void remove() {
        super.remove();
        rod.delete();
        connector.delete();
        connector2.delete();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, rod, connector,connector2);
    }
}

package com.github.guyapooye.clockworkadditions.blocks.kinetics.cv_joint;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.ibm.icu.text.MessagePattern;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.BackHalfShaftInstance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.*;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class CVJointInstance extends SingleRotatingInstance<CVJointBlockEntity> implements DynamicInstance {

    final Direction facing;

    final ModelData rod;
    final ModelData connector;

    public CVJointInstance(MaterialManager materialManager, CVJointBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        Material<ModelData> mat = getTransformMaterial();
        rod = mat.getModel(PartialModelRegistry.CV_JOINT_ROD).createInstance();
        connector = mat.getModel(PartialModelRegistry.CV_JOINT_CONNECTOR).createInstance();
    }

    @Override
    public void beginFrame() {

        boolean disconnect = false;

        CVJointBlockEntity other = null;
        if (blockEntity.target == null) disconnect = true;
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
            return;
        }

        Vector3d dif = blockEntity.getShipToWorldClient(world).invert(new Matrix4d()).transformPosition(other.getWorldSpaceClient(world))
                .sub(VectorConversionsMCKt.toJOML(blockEntity.getBlockPos().getCenter()));
        double len = dif.length();
        Vector3d dir = dif.div(len, new Vector3d());
        Vector3d startDir = VectorConversionsMCKt.toJOMLD(facing.getNormal());
        Vector3d bendAxis = startDir.cross(dir, new Vector3d()).normalize();
        if (dir.equals(startDir)) bendAxis.set(1);
        double bendAmount = Math.acos(startDir.dot(dir));

        Vector4d
                bmx = new Vector4d(1, 0, 0, 0).rotateAxis(bendAmount, bendAxis.x, bendAxis.y, bendAxis.z),
                bmy = new Vector4d(0, 1, 0, 0).rotateAxis(bendAmount, bendAxis.x, bendAxis.y, bendAxis.z),
                bmz = new Vector4d(0, 0, 1, 0).rotateAxis(bendAmount, bendAxis.x, bendAxis.y, bendAxis.z);

        Matrix4f bendMat = new Matrix4f(new Matrix4d(bmx, bmy, bmz, new Vector4d(0, 0, 0, 1)));

        rod
                .loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .transform(
                        bendMat,
                        new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0))
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(AngleHelper.verticalAngle(facing) + 180)
                .rotateZRadians(angle)
                .unCentre()
        ;
        if (blockEntity.renderConnector) {
            connector.setEmptyTransform();
            return;
        }
        connector
                .loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .transform(
                        bendMat,
                        new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0))
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(AngleHelper.verticalAngle(facing) + 180)
                .rotateZRadians(angle)
                .unCentre()
                .translateZ(-len/2)
        ;

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
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, rod, connector);
    }
}

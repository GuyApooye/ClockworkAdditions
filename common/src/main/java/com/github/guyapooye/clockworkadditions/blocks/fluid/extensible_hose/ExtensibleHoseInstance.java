package com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.*;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;

import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.getShipToWorldClient;
import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.getWorldSpaceClient;

public class ExtensibleHoseInstance extends BlockEntityInstance<ExtensibleHoseBlockEntity> implements DynamicInstance {

    final Direction facing;

    final ModelData[] hoses;
    final ModelData connector;

    public ExtensibleHoseInstance(MaterialManager materialManager, ExtensibleHoseBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        Material<ModelData> mat = getTransformMaterial();
        hoses = new ModelData[10];
            for (int i = 0; i < hoses.length; i++) {
                hoses[i] = mat.getModel(PartialModelRegistry.EXTENSIBLE_HOSE_HOSE).createInstance();
            }
        connector = mat.getModel(PartialModelRegistry.EXTENSIBLE_HOSE_CONNECTOR).createInstance();
    }

    @Override
    public void beginFrame() {

        boolean disconnect = false;

        ExtensibleHoseBlockEntity other = null;
        if (blockEntity.target == null) disconnect = true;
        else {
            other = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(world, blockEntity.target);
            if (other == null) disconnect = true;
        }

        if (disconnect) {
            for (int i = 0; i < hoses.length; i++) {
                ModelData hose = hoses[i];
                if (i == 0) {
                    hose
                            .loadIdentity()
                            .translate(getInstancePosition())
                            .centre()
                            .rotateY(AngleHelper.horizontalAngle(facing))
                            .rotateX(AngleHelper.verticalAngle(facing) + 180)
                            .unCentre()
                            .translateZ(0.25)
                    ;
                    continue;
                }
                hose.setEmptyTransform();
            }
            connector.setEmptyTransform();
            return;
        }

        Vector3d dif = getShipToWorldClient(blockEntity, world).invert(new Matrix4d()).transformPosition(getWorldSpaceClient(other, world))
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

        for (int i = 0; i < hoses.length; i++) {
            ModelData hose = hoses[i];
            if (i > len) {
                hose.setEmptyTransform();
                continue;
            }
            hose
                    .loadIdentity()
                    .translate(getInstancePosition())
                    .centre()
                    .transform(
                            bendMat,
                            new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0))
                    .rotateY(AngleHelper.horizontalAngle(facing))
                    .rotateX(AngleHelper.verticalAngle(facing) + 180)
                    .unCentre()
                    .translateZ((1+i-len)/2)
            ;
            if (i > len - 1) {
                hose.scale(1, 1, (float) (len - i));
            }
        }
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
                .unCentre()
                .translateZ(-len/2)
        ;

    }


    @Override
    public void remove() {
        for (ModelData hose : hoses) {
            hose.delete();
        }
        connector.delete();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, connector);
        for (ModelData hose : hoses) {
            relight(pos, hose);
        }
    }
}

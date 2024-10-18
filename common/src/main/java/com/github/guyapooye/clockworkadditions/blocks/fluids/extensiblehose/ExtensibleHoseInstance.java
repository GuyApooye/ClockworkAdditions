package com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.instancing.ConditionalInstance;
import com.jozufozu.flywheel.core.instancing.GroupInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.*;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;

import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.getShipToWorldClient;
import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.getWorldSpaceClient;

public class ExtensibleHoseInstance extends BlockEntityInstance<ExtensibleHoseBlockEntity<?>> implements DynamicInstance {

    final Direction facing;

    final GroupInstance<ModelData> hoses;
//    final ConditionalInstance<ModelData> connector;

    public ExtensibleHoseInstance(MaterialManager materialManager, ExtensibleHoseBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        Material<ModelData> mat = getTransformMaterial();
        hoses = new GroupInstance<>(mat.getModel(PartialModelRegistry.EXTENSIBLE_HOSE_HOSE));
//        connector = new ConditionalInstance<>(mat.getModel(PartialModelRegistry.EXTENSIBLE_HOSE_CONNECTOR)).withCondition(() -> !disconnect && blockEntity.isOrigin);
    }

    @Override
    public void beginFrame() {
        boolean disconnect = false;
        ExtensibleHoseBlockEntity<?> other = null;
        if (blockEntity.target == null) disconnect = true;
        else {
            other = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(world, blockEntity.target);
            if (other == null) disconnect = true;
        }
        if (disconnect) {
            if (hoses.size() != 1) {
                hoses.resize(1);
                hoses.get(0)
                        .loadIdentity()
                        .translate(getInstancePosition())
                        .centre()
                        .rotateY(AngleHelper.horizontalAngle(facing))
                        .rotateX(AngleHelper.verticalAngle(facing) + 180)
                        .unCentre()
                        .translateZ(0.25)
                ;
                relight(pos,hoses.get(0));
//                connector.update();
            }
            return;
        }
        if (!blockEntity.isOrigin) {
            hoses.resize(0);
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
        len *= 2;
        hoses.resize(Mth.ceil(len));
        for (int i = 0; i < hoses.size(); i++) {
            ModelData hose = hoses.get(i);
            relight(pos,hose);
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
                    .translateZ(((i+1-len)/2))
            ;
            if (i > len - 1) {
                hose.scale(1, 1, (float) (len - i));
            }
        }
//        connector.update().get().ifPresent(connector -> {
//            connector.loadIdentity()
//                    .translate(getInstancePosition())
//                    .centre()
//                    .transform(
//                            bendMat,
//                            new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0))
//                    .rotateY(AngleHelper.horizontalAngle(facing))
//                    .rotateX(AngleHelper.verticalAngle(facing) + 180)
//                    .unCentre()
//                    .translateZ(-len/2);
//            relight(pos,connector);
//        });

    }


    @Override
    public void remove() {
        hoses.clear();
//        connector.delete();
    }

    @Override
    public void updateLight() {
        super.updateLight();
    }
}

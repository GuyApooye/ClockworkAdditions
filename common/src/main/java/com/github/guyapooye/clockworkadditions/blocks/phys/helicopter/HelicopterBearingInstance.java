package com.github.guyapooye.clockworkadditions.blocks.phys.helicopter;

import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.BackHalfShaftInstance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import static java.lang.Math.PI;

public class HelicopterBearingInstance extends BackHalfShaftInstance<HelicopterBearingBlockEntity> implements DynamicInstance {
    final ModelData topInstance;

    final Vector3f rotationAxis;
    final Quaternion blockOrientation;
    final Direction facing;

    public HelicopterBearingInstance(MaterialManager materialManager, HelicopterBearingBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        facing = blockState.getValue(BlockStateProperties.FACING);
        rotationAxis = Direction.get(Direction.AxisDirection.POSITIVE, axis).step();

        blockOrientation = getBlockStateOrientation(facing);

        topInstance = getTransformMaterial().getModel(AllPartialModels.BEARING_TOP, blockState).createInstance();

        rotateTop();
    }

    private void rotateTop() {
        ClientShip shiptraption = VSGameUtilsKt.getShipObjectWorld((ClientLevel)world).getAllShips().getById(blockEntity.getShiptraptionId());
        ClientShip shipOn = VSGameUtilsKt.getShipObjectManagingPos((ClientLevel)world,pos);
        Matrix4f orientation = VectorConversionsMCKt.toMinecraft(new org.joml.Matrix4f());
        orientation.multiply(blockOrientation);
        if (shiptraption != null) {
            orientation.multiply(VectorConversionsMCKt.toMinecraft(shiptraption.getShipToWorld()));
            if (shipOn != null) orientation.multiply(VectorConversionsMCKt.toMinecraft(shipOn.getShipToWorld().invert(new Matrix4d())));
        }

        topInstance.loadIdentity()
                .translate(getInstancePosition())
                .transform(orientation,new Matrix3f(VectorConversionsMCKt.toMinecraft(new org.joml.Matrix4f(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))))
        ;
    }


    @Override
    public void beginFrame() {
        rotateTop();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, topInstance);
    }

    @Override
    public void remove() {
        super.remove();
        topInstance.delete();
    }

    @Override
    public void update() {
        super.update();
        rotateTop();
    }

    static Quaternion getBlockStateOrientation(Direction facing) {
        Quaternion orientation;

        if (facing.getAxis().isHorizontal()) {
            orientation = Vector3f.YP.rotationDegrees(AngleHelper.horizontalAngle(facing.getOpposite()));
        } else {
            orientation = Quaternion.ONE.copy();
        }

        orientation.mul(Vector3f.XP.rotationDegrees(-90 - AngleHelper.verticalAngle(facing)));
        return orientation;
    }
}

package com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkaddons.registries.PartialModelRegistry;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.BackHalfShaftInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.valkyrienskies.core.util.VectorConversionsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import static com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals.PedalsRenderer.pedalOffset;
import static com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals.PedalsRenderer.pivot;

public class PedalsInstance extends BackHalfShaftInstance<PedalsBlockEntity> implements DynamicInstance {
    private final ModelData crank;
    private final ModelData pedalLeft;
    private final ModelData pedalRight;
    private final Direction facing;

    public PedalsInstance(MaterialManager modelManager, PedalsBlockEntity blockEntity) {
        super(modelManager, blockEntity);
        facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        crank = getTransformMaterial().getModel(PartialModelRegistry.PEDALS_CRANK, blockState).createInstance();
        pedalLeft = getTransformMaterial().getModel(PartialModelRegistry.PEDAL_LEFT, blockState).createInstance();
        pedalRight = getTransformMaterial().getModel(PartialModelRegistry.PEDAL_RIGHT, blockState).createInstance();
        rotatePedals();
    }
    @Override
    protected Direction getShaftDirection() {
        return Direction.DOWN;
    }
    private void rotatePedals() {
//        Direction.Axis axis = facing.getAxis();
        float angle = blockEntity.getIndependentAngle(AnimationTickHolder.getPartialTicks());
        crank.loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .rotateY(180 + AngleHelper.horizontalAngle(facing))
                .unCentre()
                .translate(pivot)
                .rotateX(-angle*70)
                .translateBack(pivot);

        pedalRight.loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .rotateY(180 + AngleHelper.horizontalAngle(facing))
                .unCentre()
                .translate(pivot)
                .rotateX(180-angle*70)
                .translate(pedalOffset)
                .rotateX(180+angle*70)
                .translateBack(pivot)
                .translate(pedalOffset);
        pedalLeft.loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .rotateY(180 + AngleHelper.horizontalAngle(facing))
                .unCentre()
                .translate(pivot)
                .rotateX(180-angle*70)
                .translateBack(pedalOffset)
                .rotateX(180+angle*70)
                .translateBack(pivot)
                .translateBack(pedalOffset);
    }

    @Override
    public void remove() {
        super.remove();
        crank.delete();
        pedalLeft.delete();
        pedalRight.delete();
    }
    @Override
    public void update() {
        super.update();
        rotatePedals();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, crank);
        relight(pos, pedalLeft);
        relight(pos, pedalRight);
    }
    @Override
    public void beginFrame() {
        rotatePedals();
    }

}

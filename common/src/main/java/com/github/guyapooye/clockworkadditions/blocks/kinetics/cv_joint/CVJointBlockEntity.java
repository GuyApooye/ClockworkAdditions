package com.github.guyapooye.clockworkadditions.blocks.kinetics.cv_joint;

import com.github.guyapooye.clockworkadditions.mixin.create.KineticBlockEntityMixin;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4d;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.apigame.VSCore;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class CVJointBlockEntity extends KineticBlockEntity {

    public BlockPos target;
    public boolean renderConnector;

    public CVJointBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        int[] target = compound.getIntArray("target");
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (target == null) return;
        compound.putIntArray("target", new int[]{target.getX(), target.getY(), target.getZ()});
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (target == null) return neighbours;
        neighbours.add(target);
        return neighbours;
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (!(target instanceof CVJointBlockEntity other)) return 0;
        if (this.target == null || other.target == null) return 0;
        float result = (other.target.equals(getBlockPos()) && this.target.equals(other.getBlockPos())) ? 1 : 0;
        if (stateFrom.getValue(FACING).getAxisDirection() == stateTo.getValue(FACING).getAxisDirection()) result *= -1;
        return result;
    }

    public Vector3d getWorldspace() {
        Ship ship = VSGameUtilsKt.getShipManagingPos(level, getBlockPos());
        BlockPos pos = getBlockPos();
        if (ship == null) return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        return ship.getShipToWorld().transformPosition(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) return;
        CVJointBlockEntity other = BlockRegistry.CV_JOINT.get().getBlockEntity(level, target);
        if (other == null) return;
        if (renderConnector == other.renderConnector) renderConnector = !other.renderConnector;
        if (getWorldspace().sub(other.getWorldspace()).lengthSquared() > 9) {
            target = null;
            other.target = null;
            detachKinetics();
            other.detachKinetics();
        }
    }
}

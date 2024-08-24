package com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import kotlin.Triple;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.*;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.lang.Math;
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
        target = NbtUtils.readBlockPos(compound.getCompound("target"));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (target == null) return;
        compound.put("target", NbtUtils.writeBlockPos(target));
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

    public Matrix4dc getShipToWorld() {
        VSGameUtilsKt.getShipManagingPos(level, getBlockPos());
        Ship ship = VSGameUtilsKt.getShipManagingPos(level, getBlockPos());
        if (ship == null) return new Matrix4d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        return ship.getShipToWorld();
    }

    public Matrix4dc getShipToWorldClient(Level level) {
        VSGameUtilsKt.getShipManagingPos(level, getBlockPos());
        ClientShip ship = (ClientShip) VSGameUtilsKt.getShipManagingPos(level, getBlockPos());

        if (ship == null) return new Matrix4d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        return ship.getRenderTransform().getShipToWorld();
    }

    public Vector3d getWorldSpace() {
        BlockPos pos = getBlockPos();
        return getShipToWorld().transformPosition(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    public Vector3d getWorldSpaceClient(Level level) {
        BlockPos pos = getBlockPos();
        return getShipToWorldClient(level).transformPosition(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    public void detach() {
        target = null;
        if (source != null && !source.equals(getBlockPos().subtract(getBlockState().getValue(FACING).getNormal()))) {
            detachKinetics();
            removeSource();
            requestModelDataUpdate();
        }
    }

    public void attach(BlockPos pos) {
        if (pos.equals(target)) return;
        if (target != null) {
            CVJointBlockEntity old = BlockRegistry.CV_JOINT.get().getBlockEntity(level, target);
            if (old != null) {
                old.detach();
            }
            detach();
        }
        target = pos;
        CVJointBlockEntity targ = BlockRegistry.CV_JOINT.get().getBlockEntity(level, target);
        if (targ == null) return;
        detach();
        target = pos;
        attachKinetics();
        targ.attach(getBlockPos());

    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) {
            detach();
            return;
        }
        CVJointBlockEntity other = BlockRegistry.CV_JOINT.get().getBlockEntity(level, target);
        if (other == null) {
            if (level.isLoaded(target))
                detach();
            return;
        };
        if (renderConnector == other.renderConnector) renderConnector = !other.renderConnector;
        if (getWorldSpace().sub(other.getWorldSpace()).lengthSquared() > Mth.square(ConfigRegistry.server().extendables.cvJointMaxLength.get())) {
            target = null;
            other.target = null;
            detachKinetics();
            other.detachKinetics();
        }
    }

    @Override
    protected boolean isNoisy() {
        return true;
    }
}

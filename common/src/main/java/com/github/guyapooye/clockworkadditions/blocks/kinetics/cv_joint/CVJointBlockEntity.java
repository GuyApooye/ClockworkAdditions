package com.github.guyapooye.clockworkadditions.blocks.kinetics.cv_joint;

import com.github.guyapooye.clockworkadditions.mixin.create.KineticBlockEntityMixin;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.*;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.apigame.VSCore;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

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

    @Override
    public void tick() {
        super.tick();
        if (target == null) return;
        CVJointBlockEntity other = BlockRegistry.CV_JOINT.get().getBlockEntity(level, target);
        if (other == null) return;
        if (renderConnector == other.renderConnector) renderConnector = !other.renderConnector;
        if (getWorldSpace().sub(other.getWorldSpace()).lengthSquared() > 9) {
            target = null;
            other.target = null;
            detachKinetics();
            other.detachKinetics();
        }
    }
}

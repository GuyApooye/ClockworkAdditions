package com.github.guyapooye.clockworkadditions.blocks.kinetics.cv_joint;

import com.github.guyapooye.clockworkadditions.mixin.create.KineticBlockEntityMixin;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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

import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.*;
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
        if (getWorldSpace(this).sub(getWorldSpace(other)).lengthSquared() > 9) {
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

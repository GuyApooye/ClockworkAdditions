package com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointBlockEntity;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.github.guyapooye.clockworkadditions.util.PlatformUtil;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.getWorldSpace;
import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public abstract class ExtensibleHoseBlockEntity<Tank> extends SmartBlockEntity {

    public BlockPos target;
    @Environment(EnvType.CLIENT)
    public boolean isOrigin;
    public Tank tank;

    public ExtensibleHoseBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        isOrigin = compound.getBoolean("isOrigin");
        target = NbtUtils.readBlockPos(compound.getCompound("target"));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (target == null) return;
        compound.putBoolean("isOrigin", isOrigin);
        compound.put("target", NbtUtils.writeBlockPos(target));
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
        tank = null;
    }

    public abstract Tank createTank();

    public void attach(BlockPos pos) {
        if (pos.equals(target)) return;
        if (target != null) {
            ExtensibleHoseBlockEntity old = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(level, target);
            if (old != null) {
                old.detach();
            }
            detach();
        }
        target = pos;
        ExtensibleHoseBlockEntity targ = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(level, target);
        if (targ == null) return;
        detach();
        target = pos;
        tank = createTank();
        targ.tank = tank;
        targ.attach(getBlockPos());

    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) {
            this.detach();
        } else {
            ExtensibleHoseBlockEntity other = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(level, target);
            if (other == null) {
                if (level.isLoaded(this.target)) this.detach();
            } else {
                if (this.isOrigin == other.isOrigin) this.isOrigin = !other.isOrigin;

                if (getWorldSpace().sub(other.getWorldSpace()).lengthSquared() > Math.pow(ConfigRegistry.server().stretchables.hoseMaxLength.get(), 2)) {
                    target = null;
                    other.target = null;
                    this.detach();
                    other.detach();
                }
            }
        }
    }
}

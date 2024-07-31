package com.github.guyapooye.clockworkadditions.blocks.bearings.heli;

import com.github.guyapooye.clockworkadditions.util.GlueAssembler;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.apigame.constraints.*;
import org.valkyrienskies.core.impl.game.ships.ShipDataCommon;
import org.valkyrienskies.core.impl.game.ships.ShipTransformImpl;
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.assembly.ShipAssemblyKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;
import java.util.Map;
import java.util.Objects;

public class PhysicsBearingBlockEntity extends GeneratingKineticBlockEntity implements IBearingBlockEntity, IDisplayAssemblyExceptions{

    protected float angle;
    protected boolean running;
    protected boolean assembleNextTick;
    protected float clientAngleDiff;
    protected AssemblyException lastException;
    private float prevAngle;
    private long shiptraptionId = -1L;
    private long otherShipId = -1L;
    private int constraintId = -1;

    public PhysicsBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean isWoodenTop() {
        return false;
    }

    @Override
    public void setAngle(float forcedAngle) {
        this.angle = forcedAngle;
    }

    @Override
    public boolean isAttachedTo(AbstractContraptionEntity contraption) {return false;}

    @Override
    public void attach(ControlledContraptionEntity contraption) {}

    @Override
    public void onStall() {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    public float getInterpolatedAngle(float partialTicks) {
        if (this.isVirtual()) {
            return Mth.lerp(partialTicks + 0.5F, this.prevAngle, this.angle);
        } else {
            if (this.shiptraptionId == -1L || !this.running) {
                partialTicks = 0.0F;
            }

            return Mth.lerp(partialTicks, this.angle, this.angle + this.getAngularSpeed());
        }
//        return getShiptraption().getTransform().getShipToWorldRotation().angle();
    }

    public final float getInterpolatedCoreAngle(float partialTicks) {
        this.prevAngle = this.angle;
        int var2 = (int) this.angle++;
        if (this.angle == 360.0F) {
            this.angle = 0.0F;
        }

        return this.isVirtual() ? Mth.lerp(partialTicks + 0.5F, this.prevAngle, this.angle) : Mth.lerp(partialTicks, this.angle, this.angle + 4.0F);
    }

    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (this.shiptraptionId != -1 && Math.signum(prevSpeed) != Math.signum(this.getSpeed()) && prevSpeed != 0.0F) {
        }
    }

    public final float getAngularSpeed() {
        float speed = GeneratingKineticBlockEntity.convertToAngular(this.isWindmill() ? this.getGeneratedSpeed() : this.getSpeed());
        if (this.getSpeed() == 0.0F) {
            speed = 0.0F;
        }

        Level var10000 = this.level;
        Objects.requireNonNull(var10000);
        if (var10000.isClientSide) {
            speed *= ServerSpeedProvider.get();
            speed += this.clientAngleDiff / 3.0F;
        }

        return speed;
    }

    @Nullable
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
    }

    protected boolean isWindmill() {
        return false;
    }

    @NotNull
    public BlockPos getBlockPosition() {
        return this.worldPosition;
    }

    public void setAssembleNextTick(boolean assembleNextTick) {
        this.assembleNextTick = assembleNextTick;
    }

    public boolean isRunning() {
        return running;
    }
    public long getShiptraptionId() {
        return shiptraptionId;
    }

    public ServerShip getShiptraption() {
        return (ServerShip) VSGameUtilsKt.getAllShips(level).getById(shiptraptionId);
    }

    public void tick() {
        if (!level.isClientSide && this.assembleNextTick) {
            this.assembleNextTick = false;
            if (!this.running) {
                this.assemble();
            }
        }
    }
    private void assemble() {

        if (!(level.getBlockState(this.worldPosition).getBlock() instanceof BearingBlock)) return;

        Direction direction = this.getBlockState().getValue(BearingBlock.FACING);
        BlockPos center = this.worldPosition.relative(direction);
        DenseBlockPosSet selection = null;

        try {
            GlueAssembler var35 = GlueAssembler.INSTANCE;
            selection = var35.collectGlued(level, center);
            this.lastException = null;
        } catch (Throwable var33) {
            if (var33 instanceof AssemblyException) {
                this.lastException = (AssemblyException) var33;
                this.sendData();
            }
            return;
        }

        if (selection == null) return;

        running = true;

        ServerLevel serverLevel = (ServerLevel) this.level;
        ServerShip shiptraption = ShipAssemblyKt.createNewShipWithBlocks(center, selection, serverLevel);

        this.shiptraptionId = shiptraption.getId();

        if (level.isClientSide) return;

        Vector3dc pos = VectorConversionsMCKt.toJOMLD(worldPosition);
        BlockPos directionNormal = new BlockPos(direction.getNormal());
        Vector3dc axis = VectorConversionsMCKt.toJOMLD(directionNormal);
        Ship shipOn = VSGameUtilsKt.getShipObjectManagingPos(level, worldPosition);
        Map<String, Long> shipObjectWorld = VSGameUtilsKt.getShipObjectWorld(serverLevel).getDimensionToGroundBodyIdImmutable();
        otherShipId = shipObjectWorld.get(VSGameUtilsKt.getDimensionId(level));

        if (shipOn != null) otherShipId = shipOn.getId();

        BlockPos position = this.worldPosition.relative(this.getBlockState().getValue( BlockStateProperties.FACING), 1);

        Vector3dc posInOwnerShip = VectorConversionsMCKt.toJOMLD(position).add(0.5, 0.5, 0.5);
        Vector3dc posInWorld = shiptraption.getTransform().getPositionInWorld();
        Quaterniondc rotInWorld = new Quaterniond();
        Vector3dc scaling = new Vector3d(1.0, 1.0, 1.0);
        int shipChunkX = shiptraption.getChunkClaim().getXMiddle();
        int shipChunkZ = shiptraption.getChunkClaim().getZMiddle();
        Vector3dc centerInShip = new Vector3d((shipChunkX << 4) + (center.getX() & 15), center.getY(), (shipChunkZ << 4) + (center.getZ() & 15));
        Vector3d bearingPos = new Vector3d();
        if (shipOn != null) {
            scaling = shipOn.getTransform().getShipToWorldScaling();
            shiptraption.getInertiaData().getCenterOfMassInShip().sub(centerInShip,  bearingPos);

            posInWorld = shipOn.getTransform().getShipToWorld().transformPosition(posInOwnerShip.add(bearingPos, new Vector3d()), new Vector3d());
            rotInWorld = shipOn.getTransform().getShipToWorldRotation();
        }

        Vector3dc transformedCenterInShip = centerInShip.add(0.5, 0.5, 0.5, new Vector3d());

        Vector3d var10005 = transformedCenterInShip.fma(-1, axis, new Vector3d());
        Vector3d var10006 = posInOwnerShip.fma(-1, axis, new Vector3d());

        ShipDataCommon shipDataCommon = (ShipDataCommon)shiptraption;
        ShipTransformImpl shipTransform = new ShipTransformImpl(posInWorld, shiptraption.getInertiaData().getCenterOfMassInShip(), rotInWorld, scaling);
        shipDataCommon.setTransform(shipTransform);

//                    VSFixedOrientationConstraint fixedOrientationConstraint = new VSFixedOrientationConstraint(shiptraptionID,otherShipID,0,rotInWorld,rotInWorld,10e10);
        VSAttachmentConstraint attachmentConstraint = new VSAttachmentConstraint(shiptraptionId, otherShipId,0,var10005,var10006,10e10,0);

        Integer attachmentConstraintId = VSGameUtilsKt.getShipObjectWorld(serverLevel).createNewConstraint(attachmentConstraint);
        Objects.requireNonNull(attachmentConstraintId);
        constraintId = attachmentConstraintId;
//                    Integer fixedOrientationConstraintId = VSGameUtilsKt.getShipObjectWorld(serverLevel).createNewConstraint(fixedOrientationConstraint);
    }
    public void write(@NotNull CompoundTag compound, boolean clientPacket) {
        if(shiptraptionId !=-1L) compound.putLong("ShiptraptionID", shiptraptionId);
        compound.putBoolean("IsRunning",running);
        compound.putLong("OtherShipId", otherShipId);
        compound.putInt("ConstraintId", constraintId);
        super.write(compound,clientPacket);
    }
    public void read(@NotNull CompoundTag compound, boolean clientPacket) {
        if (this.wasMoved) super.read(compound, clientPacket);
        else{
            this.shiptraptionId = compound.getLong("ShiptraptionId");
            this.running = compound.getBoolean("IsRunning");
            this.lastException = AssemblyException.read(compound);
            this.otherShipId = compound.getLong("OtherShipId");
            this.constraintId = compound.getInt("ConstraintId");
            super.read(compound,clientPacket);
        }
    }

    public final void destroy() {
        if (shiptraptionId == -1L) return;
        if (constraintId == -1) return;
        VSGameUtilsKt.getShipObjectWorld((ServerLevel)level).removeConstraint(constraintId);
    }





    public void lazyTick() {
        super.lazyTick();
        if (this.shiptraptionId != -1L) {
            Level var10000 = this.level;
            Objects.requireNonNull(var10000);
            if (!var10000.isClientSide) {
                this.sendData();
            }
        }

    }
}

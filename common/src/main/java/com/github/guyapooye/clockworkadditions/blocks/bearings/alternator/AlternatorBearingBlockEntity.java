package com.github.guyapooye.clockworkadditions.blocks.bearings.alternator;

import com.github.guyapooye.clockworkadditions.util.GlueAssembler;
import com.github.guyapooye.clockworkadditions.util.NumberUtil;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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

public class AlternatorBearingBlockEntity extends GeneratingKineticBlockEntity implements IDisplayAssemblyExceptions{

    protected float angle;
    protected boolean running;
    protected boolean assembleNextTick;
    protected float clientAngleDiff;
    protected AssemblyException lastException;
    private float prevAngle;
    private long shiptraptionId = -1L;
    private long otherShipId = -1L;
    private int constraintId = -1;

    public AlternatorBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
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

    @Override
    public void onLoad() {
        super.onLoad();
        updateConstraint();
    }
    public void updateConstraint() {
        if (running) return;
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
        DenseBlockPosSet selection;

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
        Ship shipOn = VSGameUtilsKt.getShipManagingPos(level,worldPosition);

        Map<String, Long> idkwhatthisis = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level).getDimensionToGroundBodyIdImmutable();
        otherShipId = idkwhatthisis.get(VSGameUtilsKt.getDimensionId(level));

        Vector3d posInWorld = VectorConversionsMCKt.toJOMLD(worldPosition).add(NumberUtil.blockPosOffset);
        Vector3d normalInWorld = VectorConversionsMCKt.toJOMLD(getBlockState().getValue(BearingBlock.FACING).getNormal());
        Vector3d axis = normalInWorld;
        Quaterniond rotationInWorld = new Quaterniond();
        int shipChunkX = shiptraption.getChunkClaim().getXMiddle();
        int shipChunkZ = shiptraption.getChunkClaim().getZMiddle();
        Vector3dc centerInShip = new Vector3d((shipChunkX << 4) + (center.getX() & 15), center.getY(), (shipChunkZ << 4) + (center.getZ() & 15)).add(NumberUtil.blockPosOffset);
        Vector3d scaling = new Vector3d(1);
        Vector3d posInOwnerShip = posInWorld.add(normalInWorld,new Vector3d());
        if (shipOn != null) {
            otherShipId = shipOn.getId();
            shipOn.getShipToWorld().transformPosition(posInWorld);
            shipOn.getTransform().getShipToWorldRotation().transform(normalInWorld);
            scaling = (Vector3d) shipOn.getTransform().getShipToWorldScaling();
            rotationInWorld = (Quaterniond) shipOn.getTransform().getShipToWorldRotation();
        }
        Quaterniond orientationInWorld = new Vector3d(1,0,0).rotationTo(normalInWorld, new Quaterniond());
        ShipDataCommon shipDataCommon = (ShipDataCommon)shiptraption;
        shipDataCommon.setTransform(ShipTransformImpl.Companion.create(posInWorld.add(normalInWorld,new Vector3d()), shiptraption.getInertiaData().getCenterOfMassInShip(), rotationInWorld, scaling));
        Vector3d transformedCenterInShip = centerInShip.fma(-1, normalInWorld, new Vector3d());
        Vector3d transformedPosInOwnerShip = posInOwnerShip.fma(-1, normalInWorld, new Vector3d());
        VSAttachmentConstraint attachmentConstraint = new VSAttachmentConstraint(shiptraptionId,otherShipId,0,transformedCenterInShip,transformedPosInOwnerShip,10E10,0);
        VSHingeOrientationConstraint hingeOrientationConstraint = new VSHingeOrientationConstraint(shiptraptionId,otherShipId,0,orientationInWorld,orientationInWorld,10E10);
        VSGameUtilsKt.getShipObjectWorld((ServerLevel)level).createNewConstraint(attachmentConstraint);
        VSGameUtilsKt.getShipObjectWorld((ServerLevel)level).createNewConstraint(hingeOrientationConstraint);
    }
    public void write(@NotNull CompoundTag compound, boolean clientPacket) {
        if(shiptraptionId !=-1L) compound.putLong("ShiptraptionID", shiptraptionId);
        compound.putBoolean("IsRunning",running);
        compound.putLong("OtherShipId", otherShipId);
        compound.putInt("ConstraintId",constraintId);
        super.write(compound,clientPacket);
    }
    public void read(@NotNull CompoundTag compound, boolean clientPacket) {
        if (this.wasMoved) super.read(compound, clientPacket);
        else{
            this.shiptraptionId = compound.getLong("ShiptraptionId");
            this.running = compound.getBoolean("IsRunning");
            this.lastException = AssemblyException.read(compound);
            this.otherShipId = compound.getLong("OtherShipId");
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

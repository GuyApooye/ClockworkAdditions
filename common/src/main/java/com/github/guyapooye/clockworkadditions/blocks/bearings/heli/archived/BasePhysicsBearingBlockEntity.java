package com.github.guyapooye.clockworkadditions.blocks.bearings.heli.archived;

import com.github.guyapooye.clockworkadditions.util.GlueAssembler;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.valkyrienskies.clockwork.platform.api.ContraptionController;
import org.valkyrienskies.clockwork.util.ClockworkConstants;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BasePhysicsBearingBlockEntity extends GeneratingKineticBlockEntity implements IBearingBlockEntity, IDisplayAssemblyExceptions {

    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private ContraptionController.LockedMode movementMode = ContraptionController.LockedMode.UNLOCKED;
    private boolean shouldRefresh;
    private float bearingAngle;
    private boolean isRunning;
    private boolean assembleNextTick;
    private float clientAngleDiff;
    @Nullable
    private AssemblyException lastException;
    private boolean disassembleWhenPossible;
    private float prevAngle;
    private long shiptraptionID = -1L;
    @Nullable
    private Integer bearingID;
    private float coreAngle;
    private float wingAngle;
    private float previousCoreAngle;
    private boolean opening;
    private boolean open;
    private boolean closing;
    private float openProgress;
    private float openProgressMax = 70.0F;
    private float inOutCorner;
    private boolean cornerShrinking;

    public BasePhysicsBearingBlockEntity(@Nullable BlockEntityType type, @Nullable BlockPos pos, @Nullable BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(3);
    }

    @Nullable
    public final ContraptionController.LockedMode getMovementMode() {
        return this.movementMode;
    }

    public final void setMovementMode(@Nullable ContraptionController.LockedMode var1) {
        this.movementMode = var1;
    }

    public final boolean getShouldRefresh() {
        return this.shouldRefresh;
    }

    public final void setShouldRefresh(boolean var1) {
        this.shouldRefresh = var1;
    }

    protected final float getBearingAngle() {
        return this.bearingAngle;
    }

    protected final void setBearingAngle(float var1) {
        this.bearingAngle = var1;
    }

    public final boolean isRunning() {
        return this.isRunning;
    }

    protected final void setRunning(boolean var1) {
        this.isRunning = var1;
    }

    public final boolean getAssembleNextTick() {
        return this.assembleNextTick;
    }

    public final void setAssembleNextTick(boolean var1) {
        this.assembleNextTick = var1;
    }

    protected final float getClientAngleDiff() {
        return this.clientAngleDiff;
    }

    protected final void setClientAngleDiff(float var1) {
        this.clientAngleDiff = var1;
    }

    @Nullable
    protected final AssemblyException getLastException() {
        return this.lastException;
    }

    protected final void setLastException(@Nullable AssemblyException var1) {
        this.lastException = var1;
    }

    protected final boolean getDisassembleWhenPossible() {
        return this.disassembleWhenPossible;
    }

    protected final void setDisassembleWhenPossible(boolean var1) {
        this.disassembleWhenPossible = var1;
    }

    public final float getCoreAngle() {
        return this.coreAngle;
    }

    public final void setCoreAngle(float var1) {
        this.coreAngle = var1;
    }

    public final float getWingAngle() {
        return this.wingAngle;
    }

    public final void setWingAngle(float var1) {
        this.wingAngle = var1;
    }

    public final float getPreviousCoreAngle() {
        return this.previousCoreAngle;
    }

    public final void setPreviousCoreAngle(float var1) {
        this.previousCoreAngle = var1;
    }

    public final boolean getOpening() {
        return this.opening;
    }

    public final void setOpening(boolean var1) {
        this.opening = var1;
    }

    public final boolean getOpen() {
        return this.open;
    }

    public final void setOpen(boolean var1) {
        this.open = var1;
    }

    public final boolean getClosing() {
        return this.closing;
    }

    public final void setClosing(boolean var1) {
        this.closing = var1;
    }

    public boolean isWoodenTop() {
        return false;
    }

    public void addBehaviours(@NotNull List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        ScrollOptionBehaviour<ContraptionController.LockedMode> movementModeBehaviour = new ScrollOptionBehaviour<>(ContraptionController.LockedMode.class, Component.literal("Locked or Unlocked"), this, this.getMovementModeSlot());
        movementModeBehaviour.value = 0;
        ScrollOptionBehaviour<ContraptionController.LockedMode> var10000 = movementModeBehaviour;
        Objects.requireNonNull(var10000);
        var10000.requiresWrench();
        ScrollOptionBehaviour<ContraptionController.LockedMode> var10001 = movementModeBehaviour;
        Objects.requireNonNull(var10001);
        behaviours.add(var10001);
    }

    public void remove() {
        super.remove();
    }

    public void write(@NotNull CompoundTag compound, boolean clientPacket) {
        Objects.requireNonNull(compound, "compound");
        compound.putBoolean(ClockworkConstants.Nbt.INSTANCE.getRUNNING(), this.isRunning);
        compound.putFloat(ClockworkConstants.Nbt.INSTANCE.getANGLE(), this.bearingAngle);
        if (this.bearingID != null) {
            String var10001 = ClockworkConstants.Nbt.INSTANCE.getBEARING_ID();
            Integer var10002 = this.bearingID;
            Objects.requireNonNull(var10002);
            compound.putInt(var10001, var10002);
        }

        if (this.shiptraptionID != -1L) {
            compound.putLong(ClockworkConstants.Nbt.INSTANCE.getSHIPTRAPTION_ID(), this.shiptraptionID);
        }

        AssemblyException.write(compound, this.lastException);
        compound.putBoolean(ClockworkConstants.Nbt.INSTANCE.getOPEN(), this.open);
        super.write(compound, clientPacket);
    }

    protected void read(@NotNull CompoundTag compound, boolean clientPacket) {
        if (this.wasMoved) {
            super.read(compound, clientPacket);
        } else {
            float angleBefore = this.bearingAngle;
            this.open = compound.getBoolean(ClockworkConstants.Nbt.INSTANCE.getOPEN());
            this.isRunning = compound.getBoolean(ClockworkConstants.Nbt.INSTANCE.getRUNNING());
            this.bearingAngle = compound.getFloat(ClockworkConstants.Nbt.INSTANCE.getANGLE());
            this.lastException = AssemblyException.read(compound);
            if (compound.contains(ClockworkConstants.Nbt.INSTANCE.getBEARING_ID())) {
                this.bearingID = compound.getInt(ClockworkConstants.Nbt.INSTANCE.getBEARING_ID());
            }

            if (compound.contains(ClockworkConstants.Nbt.INSTANCE.getSHIPTRAPTION_ID())) {
                this.shiptraptionID = compound.getLong(ClockworkConstants.Nbt.INSTANCE.getSHIPTRAPTION_ID());
            }

            if (this.isRunning) {
                if (this.shiptraptionID == -1L) {
                    this.clientAngleDiff = AngleHelper.getShortestAngleDiff(angleBefore, this.bearingAngle);
                    this.bearingAngle = angleBefore;
                }
            } else {
                this.shiptraptionID = -1L;
            }

            this.shouldRefresh = true;
            super.read(compound, clientPacket);
        }
    }

    public float getInterpolatedAngle(float partialTicks) {
//        System.out.println(bearingAngle);
        if (this.isVirtual()) {
            return Mth.lerp(partialTicks + 0.5F, this.prevAngle, this.bearingAngle);
        } else {
            if (this.shiptraptionID == -1L || !this.isRunning) {
                partialTicks = 0.0F;
            }

            return Mth.lerp(partialTicks, this.bearingAngle, this.bearingAngle + this.getAngularSpeed());
        }
    }

    public final float getOpeningProgress() {
        return this.openProgress;
    }

    public final float getWingRotOffset() {
        return this.open ? (float)((double)this.openProgressMax) : (this.isRunning ? (float)Mth.lerp(this.openProgress, 0.0, this.openProgressMax) : 0.0F);
    }

    public final float getInterpolatedCoreAngle(float partialTicks) {
        this.previousCoreAngle = this.coreAngle;
        int var2 = (int) this.coreAngle++;
        if (this.coreAngle == 360.0F) {
            this.coreAngle = 0.0F;
        }

        return this.isVirtual() ? Mth.lerp(partialTicks + 0.5F, this.previousCoreAngle, this.coreAngle) : Mth.lerp(partialTicks, this.coreAngle, this.coreAngle + 4.0F);
    }

    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (this.shiptraptionID != -1L && Math.signum(prevSpeed) != Math.signum(this.getSpeed()) && prevSpeed != 0.0F) {
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
        BlockPos var10000 = this.worldPosition;
        Objects.requireNonNull(var10000, "worldPosition");
        return var10000;
    }




    private void assemble() {
        Level var10000 = this.level;
        Objects.requireNonNull(var10000);
        if (var10000.getBlockState(this.worldPosition).getBlock() instanceof BearingBlock) {
            Direction direction = this.getBlockState().getValue(BearingBlock.FACING);
            BlockPos center = this.worldPosition.relative(direction);
            DenseBlockPosSet selection = null;

            Level var10001;
            try {
                GlueAssembler var35 = GlueAssembler.INSTANCE;
                var10001 = this.level;
                Objects.requireNonNull(var10001);
                Objects.requireNonNull(center);
                selection = var35.collectGlued(var10001, center);
                this.lastException = null;
            } catch (Throwable var33) {
                if (var33 instanceof AssemblyException) {
                    this.lastException = (AssemblyException) var33;
                    this.sendData();
                }
                return;
            }

            if (selection != null) {
                ServerLevel var10002 = (ServerLevel) this.level;
                Objects.requireNonNull(var10002);
                ServerShip shiptraption = ShipAssemblyKt.createNewShipWithBlocks(center, selection, var10002);

                //ClockworkSounds.SoundEntry var36 = ClockworkSounds.INSTANCE.getPHYSICS_INFUSER_LIGHTNING();
                var10001 = this.level;
                BlockPos var40 = this.worldPosition;
                Objects.requireNonNull(var40, "worldPosition");
                //ClockworkSounds.SoundEntry.playOnServer(var36, var10001, (Vec3i)var40, 0.0F, 0.0F, 12, (Object)null);
                this.shiptraptionID = shiptraption.getId();
                var10000 = this.level;
                Objects.requireNonNull(var10000);
                if (!var10000.isClientSide) {
                    BlockPos var37 = this.worldPosition;
                    Vector3dc pos = VectorConversionsMCKt.toJOMLD(var37);
                    BlockPos var38 = new BlockPos(direction.getNormal());
                    Vector3dc axis = VectorConversionsMCKt.toJOMLD(var38);
                    var10000 = this.level;
                    BlockPos var39 = this.worldPosition;
                    Ship shipOn = VSGameUtilsKt.getShipObjectManagingPos(var10000, var39);
                    var10000 = this.level;
                    Map<String, Long> var42 = VSGameUtilsKt.getShipObjectWorld((ServerLevel) var10000).getDimensionToGroundBodyIdImmutable();
                    var10001 = this.level;
                    Objects.requireNonNull(var10001);
                    Number var45 = var42.get(VSGameUtilsKt.getDimensionId(var10001));
                    Objects.requireNonNull(var45);
                    long otherShipID = var45.longValue();
                    if (shipOn != null) {
                        otherShipID = shipOn.getId();
                    }

                    int veryUncoolFix = 1;
                    Quaterniond var14;
                    Quaterniond var46;
                    switch (direction == null ? -1 : WhenMappings.$EnumSwitchMapping$0[direction.ordinal()]) {
                        case 1:
                            var46 = new Quaterniond(new AxisAngle4d(Math.PI, new Vector3d(1.0, 0.0, 0.0)));
                            break;
                        case 2:
                            var14 = (new Quaterniond(new AxisAngle4d(Math.PI, new Vector3d(0.0, 1.0, 0.0)))).mul(new Quaterniond(new AxisAngle4d(1.5707963267948966, new Vector3d(1.0, 0.0, 0.0)))).normalize();
                            Objects.requireNonNull(var14);
                            var46 = var14;
                            break;
                        case 3:
                            var14 = (new Quaterniond(new AxisAngle4d(1.5707963267948966, new Vector3d(0.0, 1.0, 0.0)))).mul(new Quaterniond(new AxisAngle4d(1.5707963267948966, new Vector3d(1.0, 0.0, 0.0)))).normalize();
                            Objects.requireNonNull(var14);
                            var46 = var14;
                            break;
                        case 4:
                            var14 = (new Quaterniond(new AxisAngle4d(1.5707963267948966, new Vector3d(1.0, 0.0, 0.0)))).normalize();
                            Objects.requireNonNull(var14);
                            var46 = var14;
                            break;
                        case 5:
                            veryUncoolFix = -veryUncoolFix;
                            var14 = (new Quaterniond(new AxisAngle4d(4.71238898038469, new Vector3d(0.0, 1.0, 0.0)))).mul(new Quaterniond(new AxisAngle4d(1.5707963267948966, new Vector3d(1.0, 0.0, 0.0)))).normalize();
                            Objects.requireNonNull(var14);
                            var46 = var14;
                            break;
                        default:
                            var46 = new Quaterniond();
                    }

                    Quaterniond rotationQuaternion = var46;
                    var37 = this.worldPosition.relative(this.getBlockState().getValue( BlockStateProperties.FACING), 1);
                    Objects.requireNonNull(var37, "relative(...)");
                    Vector3d var48 = VectorConversionsMCKt.toJOMLD(var37).add(0.5, 0.5, 0.5);
                    Objects.requireNonNull(var48, "add(...)");
                    Vector3dc posInOwnerShip = var48;
                    Vector3dc posInWorld = shiptraption.getTransform().getPositionInWorld();
                    Quaterniondc rotInWorld = new Quaterniond();
                    Vector3dc scaling = new Vector3d(1.0, 1.0, 1.0);
                    int shipChunkX = shiptraption.getChunkClaim().getXMiddle();
                    int shipChunkZ = shiptraption.getChunkClaim().getZMiddle();
                    Vector3dc centerInShip = new Vector3d((shipChunkX << 4) + (center.getX() & 15), center.getY(), (shipChunkZ << 4) + (center.getZ() & 15));
                    Vector3dc bearingPos;
                    if (shipOn != null) {
                        scaling = shipOn.getTransform().getShipToWorldScaling();
                        var48 = shiptraption.getInertiaData().getCenterOfMassInShip().sub(centerInShip, new Vector3d());
                        Objects.requireNonNull(var48, "sub(...)");
                        bearingPos = var48;
                        posInWorld = shipOn.getTransform().getShipToWorld().transformPosition(posInOwnerShip.add(bearingPos, new Vector3d()), new Vector3d());
                        rotInWorld = shipOn.getTransform().getShipToWorldRotation();
                    }

                    Objects.requireNonNull(shiptraption, "null cannot be cast to non-null type org.valkyrienskies.core.impl.game.ships.ShipDataCommon");
                    ShipDataCommon var50 = (ShipDataCommon)shiptraption;
                    ShipTransformImpl.Companion var41 = ShipTransformImpl.Companion;
                    Objects.requireNonNull(posInWorld);
                    var50.setTransform(var41.create(posInWorld, shiptraption.getInertiaData().getCenterOfMassInShip(), rotInWorld, scaling));
                    var48 = centerInShip.add(0.5, 0.5, 0.5, new Vector3d());
                    Objects.requireNonNull(var48, "add(...)");
                    bearingPos = var48;
                    var46 = rotationQuaternion.mul(new Quaterniond(new AxisAngle4d(Math.toRadians(90.0), 0.0, 0.0, 1.0)), new Quaterniond()).normalize();
                    Objects.requireNonNull(var46, "normalize(...)");
                    Quaterniondc hingeOrientation = var46;

                    VSHingeOrientationConstraint hingeConstraint = new VSHingeOrientationConstraint(this.shiptraptionID, otherShipID, 0, hingeOrientation, hingeOrientation, Double.MAX_VALUE);
                    double extraDist = 1.0;
                    long var43 = this.shiptraptionID;
                    Vector3d var10005 = bearingPos.fma(-extraDist, axis, new Vector3d());
                    Vector3dc var47 = var10005;
                    Vector3d var10006 = posInOwnerShip.fma(-extraDist, axis, new Vector3d());
                    VSAttachmentConstraint firstAttachment = new VSAttachmentConstraint(var43, otherShipID, 0, var47, var10006, Double.MAX_VALUE, 0.0);
                    var43 = this.shiptraptionID;
                    var10005 = bearingPos.fma(extraDist, axis, new Vector3d());
                    var47 = var10005;
                    var10006 = posInOwnerShip.fma(extraDist, axis, new Vector3d());
                    VSAttachmentConstraint secondAttachment = new VSAttachmentConstraint(var43, otherShipID, 0, var47, var10006, Double.MAX_VALUE, 0.0);
                    var10000 = this.level;
                    Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                    Integer var52 = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).createNewConstraint(firstAttachment);
                    if (var52 != null) {
                        int firstAttachmentId = var52;
                        var10000 = this.level;
                        Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                        var52 = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).createNewConstraint(hingeConstraint);
                        if (var52 != null) {
                            int hingeID = var52;
                            var10000 = this.level;
                            Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                            var52 = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).createNewConstraint(secondAttachment);
                            if (var52 != null) {
                                int secondAttachmentID = var52;
                                VSConstraintAndId firstAttachmentConstraint = new VSConstraintAndId(firstAttachmentId, firstAttachment);
                                VSConstraintAndId hingeContraptionConstraint = new VSConstraintAndId(hingeID, hingeConstraint);
                                VSConstraintAndId secondAttachmentConstraint = new VSConstraintAndId(secondAttachmentID, secondAttachment);
                                double var10004 = this.bearingAngle;
                                float var49 = (float)veryUncoolFix * this.getSpeed();
                                BasePhysicsBearingCreateData data = new BasePhysicsBearingCreateData(pos, axis, var10004, var49, movementMode == ContraptionController.LockedMode.LOCKED, this.shiptraptionID, firstAttachmentConstraint, hingeContraptionConstraint, null, null, secondAttachmentConstraint);
                                var10000 = this.level;
                                Objects.requireNonNull(var10000);
                                System.out.println("is client side: "+var10000.isClientSide);
                                if (!var10000.isClientSide) {
                                    BaseBearingController var44 = BaseBearingController.Companion.getOrCreate(shiptraption);
                                    Objects.requireNonNull(var44);
                                    this.bearingID = var44.addPhysBearing(data);
                                    System.out.println("bearing id: "+this.bearingID+ "getSignalStrength()");
                                    System.out.println("bearing data initial size: "+var44.getBearingData().size());
                                }

                                this.isRunning = true;
                                this.bearingAngle = 0.0F;
                                this.sendData();
                                this.updateGeneratedRotation();
                            }
                        }
                    }
                }
            }
        }
    }

    public final void destroy() {
        if (this.level != null && this.bearingID != null) {
            Level var10000 = this.level;
            Objects.requireNonNull(var10000);
            if (!var10000.isClientSide) {
                var10000 = this.level;
                Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                ServerShip ship = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).getAllShips().getById(this.shiptraptionID);
                if (ship != null) {
                    BaseBearingController var6 = BaseBearingController.Companion.getOrCreate(ship);
                    Objects.requireNonNull(var6);
                    BaseBearingController controller = var6;
                    System.out.println("bearing data size: "+controller.getBearingData().size());
                    System.out.println("bearing id 2: "+this.bearingID);
                    BasePhysicsBearingData var7 = (controller.getBearingData()).get(this.bearingID);
                    Objects.requireNonNull(var7);
                    Integer var8 = var7.getAttachID();
                    if (var8 == null) {
                        return;
                    }

                    int attachID = var8;
                    var10000 = this.level;
                    Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                    VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).removeConstraint(attachID);
                    var7 = ((Map<Integer,BasePhysicsBearingData>)controller.getBearingData()).get(this.bearingID);
                    Objects.requireNonNull(var7);
                    var8 = var7.getHingeID();
                    if (var8 == null) {
                        return;
                    }

                    int hingeID = var8;
                    var10000 = this.level;
                    Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                    VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).removeConstraint(hingeID);
                    var7 = ((Map<Integer,BasePhysicsBearingData>)controller.getBearingData()).get(this.bearingID);
                    Objects.requireNonNull(var7);
                    Integer secondAttachId = var7.getSecondAttachId();
                    if (secondAttachId != null) {
                        var10000 = this.level;
                        Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                        VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).removeConstraint(secondAttachId);
                    }

                    Integer var10001 = this.bearingID;
                    Objects.requireNonNull(var10001);
                    controller.removePhysBearing(var10001);
                }
            }
        }

    }

    public final void disassemble() {
        if (this.isRunning || this.shiptraptionID != -1L) {
            this.bearingAngle = 0.0F;
            if (this.shiptraptionID != -1L) {
                Level var10000 = this.level;
                Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                ServerShip ship = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).getAllShips().getById(this.shiptraptionID);
                if (ship != null) {
                }

                AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.level, this.worldPosition);
            } else {
                this.shiptraptionID = -1L;
                this.isRunning = false;
                this.updateGeneratedRotation();
                this.assembleNextTick = false;
                this.sendData();
            }
        }
    }

    private void shipDisassemble() {
        if (this.shiptraptionID != -1L) {
            Level var10000 = this.level;
            Objects.requireNonNull(var10000);
            if (!var10000.isClientSide) {
                var10000 = this.level;
                Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                ServerShip var5 = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).getAllShips().getById(this.shiptraptionID);
                if (var5 != null) {
                    ServerShip ship = var5;
                    if (this.bearingID != null) {
                        BaseBearingController var6 = BaseBearingController.Companion.getOrCreate(ship);
                        Objects.requireNonNull(var6);
                        BaseBearingController controller = var6;
                        Direction direction = this.getBlockState().getValue(BearingBlock.FACING);
                        BlockPos var7 = this.worldPosition.relative(direction, 1);
                        Objects.requireNonNull(var7, "relative(...)");
                        Vector3dc inWorld = VectorConversionsMCKt.toJOMLD(var7);
                        if (!controller.canDisassemble()) {
                            return;
                        }
                    }

                }
            }
        }
    }

    public void tick() {
        super.tick();
        this.prevAngle = this.bearingAngle;
        Level var10000 = this.level;
        Objects.requireNonNull(var10000);
        if (var10000.isClientSide) {
            this.clientAngleDiff /= 2.0F;
        }

        var10000 = this.level;
        Objects.requireNonNull(var10000);
        if (!var10000.isClientSide && this.assembleNextTick) {
            this.assembleNextTick = false;
            if (!this.isRunning) {
                this.assemble();
            }
        }


        ServerShip ship;
        BaseBearingController var45;
        if (this.shouldRefresh) {
            var10000 = this.level;
            Objects.requireNonNull(var10000);
            if (!var10000.isClientSide) {
                var10000 = this.level;
                Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                ship = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).getAllShips().getById(this.shiptraptionID);
                if (ship != null && this.bearingID != null) {
                    System.out.println((float) ship.getTransform().getShipToWorldRotation().angle());
//                    bearingAngle = (float) ship.getTransform().getShipToWorldRotation().angle();
                    var45 = BaseBearingController.Companion.getOrCreate(ship);
                    Objects.requireNonNull(var45);
                    BasePhysicsBearingData bearingData = (BasePhysicsBearingData)((Map)var45.getBearingData()).get(this.bearingID);
                    if (bearingData != null) {
                        VSAttachmentConstraint var47 = bearingData.getAttachConstraint();
                        Objects.requireNonNull(var47);
                        VSAttachmentConstraint var3 = var47;
                        long shipId0 = var3.component1();
                        double compliance = var3.component3();
                        Vector3dc localPos0 = var3.component4();
                        Vector3dc localPos1 = var3.component5();
                        double maxForce = var3.component6();
                        double fixedDistance = var3.component7();
                        VSHingeOrientationConstraint var48 = bearingData.getHingeConstraint();
                        Objects.requireNonNull(var48);
                        VSHingeOrientationConstraint var14 = var48;
                        long shipId01 = var14.component1();
                        double compliance1 = var14.component3();
                        Quaterniondc localRot0 = var14.component4();
                        Quaterniondc localRot1 = var14.component5();
                        double maxTorque = var14.component6();
                        var10000 = this.level;
                        Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                        ServerLevel var50 = (ServerLevel)var10000;
                        BlockPos var10001 = this.worldPosition;
                        Objects.requireNonNull(var10001, "worldPosition");
                        Ship shipOn = VSGameUtilsKt.getShipObjectManagingPos(var50, var10001);
                        var10000 = this.level;
                        Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                        Map var51 = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).getDimensionToGroundBodyIdImmutable();
                        Level var46 = this.level;
                        Objects.requireNonNull(var46);
                        Object var52 = var51.get(VSGameUtilsKt.getDimensionId(var46));
                        Objects.requireNonNull(var52);
                        long shipOnID = ((Number)var52).longValue();
                        if (shipOn != null) {
                            shipOnID = shipOn.getId();
                        } else {
                            var10000 = this.level;
                            Objects.requireNonNull(var10000);
                            var10001 = this.worldPosition;
                            Objects.requireNonNull(var10001, "worldPosition");
                            if (VSGameUtilsKt.isBlockInShipyard(var10000, var10001)) {
                                this.isRunning = false;
                                this.assembleNextTick = false;
                                this.shouldRefresh = false;
                                return;
                            }
                        }

                        VSAttachmentConstraint attachConstraint = new VSAttachmentConstraint(shipId0, shipOnID, 0, localPos0, localPos1, maxForce, fixedDistance);
                        VSHingeOrientationConstraint hingeConstraint = new VSHingeOrientationConstraint(shipId01, shipOnID, 0, localRot0, localRot1, maxTorque);
                        VSAttachmentConstraint secondAttachConstraint = null;
                        if (bearingData.getSecondAttachConstraint() != null) {
                            var47 = bearingData.getSecondAttachConstraint();
                            Objects.requireNonNull(var47);
                            VSAttachmentConstraint var29 = var47;
                            long shipId02 = var29.component1();
                            double compliance2 = var29.component3();
                            Vector3dc localPos02 = var29.component4();
                            Vector3dc localPos12 = var29.component5();
                            double maxForce2 = var29.component6();
                            double fixedDistance2 = var29.component7();
                            secondAttachConstraint = new VSAttachmentConstraint(shipId02, shipOnID, 0, localPos02, localPos12, maxForce2, fixedDistance2);
                        }

                        boolean createdAttachment = false;
                        boolean createdHinge = false;
                        var10000 = this.level;
                        Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                        Integer secondAttachId = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).createNewConstraint(attachConstraint);
                        if (secondAttachId != null) {
                            var45 = BaseBearingController.Companion.getOrCreate(ship);
                            Objects.requireNonNull(var45);
                            var52 = ((Map)var45.getBearingData()).get(this.bearingID);
                            Objects.requireNonNull(var52);
                            ((BasePhysicsBearingData)var52).setAttachConstraint(attachConstraint);
                            var45 = BaseBearingController.Companion.getOrCreate(ship);
                            Objects.requireNonNull(var45);
                            var52 = ((Map)var45.getBearingData()).get(this.bearingID);
                            Objects.requireNonNull(var52);
                            ((BasePhysicsBearingData)var52).setAttachID(secondAttachId);
                            createdAttachment = true;
                        }

                        var10000 = this.level;
                        Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                        secondAttachId = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).createNewConstraint(hingeConstraint);
                        if (secondAttachId != null) {
                            var45 = BaseBearingController.Companion.getOrCreate(ship);
                            Objects.requireNonNull(var45);
                            var52 = ((Map)var45.getBearingData()).get(this.bearingID);
                            Objects.requireNonNull(var52);
                            ((BasePhysicsBearingData)var52).setHingeConstraint(hingeConstraint);
                            var45 = BaseBearingController.Companion.getOrCreate(ship);
                            Objects.requireNonNull(var45);
                            var52 = ((Map)var45.getBearingData()).get(this.bearingID);
                            Objects.requireNonNull(var52);
                            ((BasePhysicsBearingData)var52).setHingeID(secondAttachId);
                            createdHinge = true;
                        }

                        if (secondAttachConstraint != null) {
                            var10000 = this.level;
                            Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                            secondAttachId = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).createNewConstraint(secondAttachConstraint);
                            if (secondAttachId != null) {
                                var45 = BaseBearingController.Companion.getOrCreate(ship);
                                Objects.requireNonNull(var45);
                                var52 = ((Map)var45.getBearingData()).get(this.bearingID);
                                Objects.requireNonNull(var52);
                                ((BasePhysicsBearingData)var52).setSecondAttachConstraint(secondAttachConstraint);
                                var45 = BaseBearingController.Companion.getOrCreate(ship);
                                Objects.requireNonNull(var45);
                                var52 = ((Map)var45.getBearingData()).get(this.bearingID);
                                Objects.requireNonNull(var52);
                                ((BasePhysicsBearingData)var52).setSecondAttachId(secondAttachId);
                                createdAttachment = true;
                            }
                        }

                        if (createdHinge && createdAttachment) {
                            this.shouldRefresh = false;
                        }
                    }
                }
            }
        }

        if (this.inOutCorner < 1.0F && !this.cornerShrinking) {
            this.inOutCorner += 0.0075F;
        } else if (this.inOutCorner >= 1.0F) {
            this.cornerShrinking = true;
        }

        if (this.inOutCorner > 0.0F && this.cornerShrinking) {
            this.inOutCorner -= 0.0075F;
        } else if (this.inOutCorner <= 0.0F) {
            this.cornerShrinking = false;
        }

        if (this.isRunning && !this.open && !this.opening) {
            this.opening = true;
        }

        if (this.opening && this.isRunning && this.openProgress < 1.0F) {
            this.openProgress += 0.05F;
        } else if (this.openProgress >= 1.0F) {
            this.opening = false;
            this.open = true;
            this.openProgress = 1.0F;
        }

        if (this.isRunning) {
            if (this.shiptraptionID != -1L) {
                float angularSpeed = this.getAngularSpeed();
                float newAngle = this.bearingAngle + angularSpeed;
                this.bearingAngle = newAngle % (float)360;
            }

            if (this.isRunning) {
                var10000 = this.level;
                Objects.requireNonNull(var10000);
                if (!var10000.isClientSide && this.shiptraptionID != -1L) {
                    var10000 = this.level;
                    Objects.requireNonNull(var10000, "null cannot be cast to non-null type net.minecraft.server.level.ServerLevel");
                    ship = VSGameUtilsKt.getShipObjectWorld((ServerLevel)var10000).getAllShips().getById(this.shiptraptionID);
                    if (ship != null) {
                        var45 = BaseBearingController.Companion.getOrCreate(ship);
                        Objects.requireNonNull(var45);
                        if (((Map)var45.getBearingData()).get(this.bearingID) == null) {
                            return;
                        }

                        int dumbFix = 1;
                        Direction direction = this.getBlockState().getValue(BearingBlock.FACING);
                        if (direction == Direction.WEST || direction == Direction.NORTH || direction == Direction.DOWN) {
                            dumbFix = -dumbFix;
                        }

                        double var10002 = this.bearingAngle;
                        float var10003 = (float)dumbFix * this.getSpeed();
                        BasePhysicsBearingUpdateData data = new BasePhysicsBearingUpdateData(var10002, var10003, movementMode == ContraptionController.LockedMode.LOCKED, null, null);
                        var45 = BaseBearingController.Companion.getOrCreate(ship);
                        Objects.requireNonNull(var45);
                        Integer var49 = this.bearingID;
                        Objects.requireNonNull(var49);
                        var45.updatePhysBearing(var49, data);

                    }
                }
            }

            if (this.disassembleWhenPossible) {
                this.shipDisassemble();
            }

            this.applyRotation();
        }

    }

    public final boolean isNearInitialAngle() {
        return Math.abs(this.bearingAngle) < 45.0F || Math.abs(this.bearingAngle) > 315.0F;
    }

    public void lazyTick() {
        super.lazyTick();
        if (this.shiptraptionID != -1L) {
            Level var10000 = this.level;
            Objects.requireNonNull(var10000);
            if (!var10000.isClientSide) {
                this.sendData();
            }
        }

    }

    private void applyRotation() {
    }

    public void attach(@NotNull ControlledContraptionEntity contraption) {}

    public void onStall() {
        Level var10000 = this.level;
        Objects.requireNonNull(var10000);
        if (!var10000.isClientSide) {
            this.sendData();
        }

    }

    public boolean isValid() {
        return !this.isRemoved();
    }

    public boolean isAttachedTo(@NotNull AbstractContraptionEntity contraption) {
        return false;
    }

    public boolean addToTooltip(@NotNull List tooltip, boolean isPlayerSneaking) {
        if (super.addToTooltip(tooltip, isPlayerSneaking)) {
            return true;
        } else if (isPlayerSneaking) {
            return false;
        } else if (!this.isWindmill() && this.getSpeed() == 0.0F) {
            return false;
        } else if (this.isRunning) {
            return false;
        } else {
            BlockState state = this.getBlockState();
            if (!(state.getBlock() instanceof BearingBlock)) {
                return false;
            } else {
                BlockState attachedState = level.getBlockState(this.worldPosition.relative(state.getValue(BearingBlock.FACING)));
                TooltipHelper.addHint(tooltip, "hint.empty_bearing");
                return true;
            }
        }
    }

    public void setAngle(float forcedAngle) {
        this.bearingAngle = forcedAngle;
    }

    public boolean isShipContraptionController() {
        return true;
    }

    @Nullable
    public Ship getConnectedShip() {
        return null;
    }

    public final float getAngle() {
        return this.bearingAngle;
    }

    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(Object obj) {
            this();
        }
    }

    // $FF: synthetic class
    public static class WhenMappings {
        // $FF: synthetic field
        public static final int[] $EnumSwitchMapping$0;

        static {
            int[] var0 = new int[Direction.values().length];

            try {
                var0[Direction.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                var0[Direction.NORTH.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                var0[Direction.EAST.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                var0[Direction.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                var0[Direction.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

            $EnumSwitchMapping$0 = var0;
        }
    }
}

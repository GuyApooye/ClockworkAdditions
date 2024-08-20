package com.github.guyapooye.clockworkadditions.blocks.bearings.flap;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.simibubi.create.content.contraptions.*;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.clockwork.content.contraptions.flap.contraption.FlapContraption;
import org.valkyrienskies.clockwork.util.ClockworkConstants;

import java.util.List;

import static net.minecraft.util.Mth.sign;

public class KineticFlapBearingBlockEntity extends KineticBlockEntity implements IBearingBlockEntity {
    private boolean redstoneSideOne;
    private boolean redstoneSideTwo;
    private float bearingAngle;
    private float clientAngleDiff;
    private boolean isRunning;
    private boolean assembleNextTick;
    @Nullable
    private AssemblyException lastException;
    @Nullable
    private ControlledContraptionEntity flap;
    private float prevForcedAngle;
    private int redstoneLevel;
    @Nullable
    private BlockPos redstonePos;
    private ScrollOptionBehaviour<PoweredMode> poweredMode;
    public KineticFlapBearingBlockEntity(@Nullable BlockEntityType type, @NotNull BlockPos pos, @NotNull BlockState state) {
        super(type, pos, state);
    }

    public final boolean getRedstoneSideOne() {
        return this.redstoneSideOne;
    }

    public final void setRedstoneSideOne(boolean val) {
        this.redstoneSideOne = val;
    }

    public final boolean getRedstoneSideTwo() {
        return this.redstoneSideTwo;
    }

    public final void setRedstoneSideTwo(boolean val) {
        this.redstoneSideTwo = val;
    }

    protected final float getBearingAngle() {
        return this.bearingAngle;
    }

    protected final void setBearingAngle(float val) {
        this.bearingAngle = val;
    }

    protected final float getClientAngleDiff() {
        return this.clientAngleDiff;
    }

    protected final void setClientAngleDiff(float val) {
        this.clientAngleDiff = val;
    }

    public final boolean isRunning() {
        return this.isRunning;
    }

    protected final void setRunning(boolean val) {
        this.isRunning = val;
    }

    public final boolean getAssembleNextTick() {
        return this.assembleNextTick;
    }

    public final void setAssembleNextTick(boolean val) {
        this.assembleNextTick = val;
    }

    @Nullable
    protected final AssemblyException getLastException() {
        return this.lastException;
    }

    protected final void setLastException(@Nullable AssemblyException val) {
        this.lastException = val;
    }

    @Nullable
    protected final ControlledContraptionEntity getFlap() {
        return this.flap;
    }

    protected final void setFlap(@Nullable ControlledContraptionEntity val) {
        this.flap = val;
    }

    public void addBehaviours(@NotNull List behaviours) {
        super.addBehaviours(behaviours);
        this.poweredMode = new ScrollOptionBehaviour<>(PoweredMode.class, ClockworkAdditions.asTranslatable("contraptions.kinetic_flap_bearing.redstone_input_mode"), this, this.getMovementModeSlot());
        ScrollOptionBehaviour<PoweredMode> var10000 = this.poweredMode;
        var10000.requiresWrench();
        ScrollOptionBehaviour<PoweredMode> var10001 = this.poweredMode;
        behaviours.add(var10001);
    }

    public final boolean isFlap() {
        return true;
    }

    private int getPower(Level worldIn, BlockPos pos) {
        int power = 0;
        Direction[] var10000 = Iterate.directions;
        Direction[] var4 = var10000;
        int var5 = 0;

        for(int var6 = var4.length; var5 < var6; ++var5) {
            Direction direction = var4[var5];
//            power = Math.max(worldIn.getSignal(pos.relative(direction), direction), power);
            if (power < (worldIn.getSignal(pos.relative(direction), direction))) {
                this.redstonePos = pos.relative(direction);
                power = worldIn.getSignal(pos.relative(direction), direction);
            }
        }
        if (redstonePos==null) power = 0;

        return power;
    }
    public void setSidesFromSpeed() {
        this.redstoneSideOne = sign(getSpeed()) == -1;
        this.redstoneSideTwo = sign(getSpeed()) == 1;
    }

    public void tick() {
        super.tick();
//        if (getSpeed() == 0) bearingAngle -= 1;
        ControlledContraptionEntity var10000;
        if (this.flap != null) {
            var10000 = this.flap;
            var10000.tick();
        }

        Level var4 = this.level;
        if (var4.isClientSide) {
            this.prevForcedAngle = this.bearingAngle;
            this.clientAngleDiff /= 2.0F;
        }

        Level var10002 = this.level;
        BlockPos var10003 = this.worldPosition;
        if(poweredMode.get() == PoweredMode.NORMAL) this.redstoneLevel = 15-this.getPower(var10002, var10003);
        else this.redstoneLevel = this.getPower(var10002, var10003);

//        if (this.getBlockState().getValue((Property) BlockStateProperties.FACING) != Direction.UP && this.getBlockState().getValue((Property)BlockStateProperties.FACING) != Direction.DOWN) {
//            if (this.getBlockState().getValue((Property)BlockStateProperties.FACING) != Direction.NORTH && this.getBlockState().getValue((Property)BlockStateProperties.FACING) != Direction.SOUTH) {
//                if (this.getBlockState().getValue((Property)BlockStateProperties.FACING) == Direction.EAST || this.getBlockState().getValue((Property)BlockStateProperties.FACING) == Direction.WEST) {
//                    setSidesFromSpeed();
//                }
//            } else {
//                setSidesFromSpeed();
//            }
//        } else {
//            setSidesFromSpeed();
//        }
        setSidesFromSpeed();

        var4 = this.level;
        if (!var4.isClientSide && this.assembleNextTick) {
            this.assembleNextTick = false;
            if (this.isRunning) {
                boolean canDisassemble = true;
                if (this.speed == 0.0F) {
                    if (this.flap != null) {
                        var10000 = this.flap;
                        var10000.getContraption().stop(this.level);
                    }

                    this.disassemble();
                }

            } else {
                this.assemble();
            }
        } else {
            label52: {
                if (this.flap != null) {
                    var10000 = this.flap;
                    if (var10000.isStalled()) {
                        break label52;
                    }
                }

                float testSpeed = this.getAngularSpeed() / 2.0F;
                float newAngle = this.bearingAngle + this.getFlapSpeed();
                this.bearingAngle = (newAngle % (float)360);
            }

            if (this.isRunning) {
                this.applyRotations();
            }
        }
    }

    public void write(@NotNull CompoundTag compound, boolean clientPacket) {
        compound.putBoolean(ClockworkConstants.Nbt.INSTANCE.getRUNNING(), this.isRunning);
        compound.putFloat(ClockworkConstants.Nbt.INSTANCE.getANGLE(), this.bearingAngle);
        AssemblyException.write(compound, this.lastException);
        super.write(compound, clientPacket);
    }

    protected void read(@NotNull CompoundTag compound, boolean clientPacket) {
        float angleBefore = this.bearingAngle;
        this.isRunning = compound.getBoolean(ClockworkConstants.Nbt.INSTANCE.getRUNNING());
        this.bearingAngle = compound.getFloat(ClockworkConstants.Nbt.INSTANCE.getANGLE());
        this.lastException = AssemblyException.read(compound);
        super.read(compound, clientPacket);
        if (clientPacket) {
            if (this.isRunning) {
                this.clientAngleDiff = AngleHelper.getShortestAngleDiff(angleBefore, this.bearingAngle);
                this.bearingAngle = angleBefore;
            } else {
                this.flap = null;
            }

        }
    }

    public final void assemble() {
        Level var10000 = this.level;
        Intrinsics.checkNotNull(var10000);
        if (var10000.getBlockState(this.worldPosition).getBlock() instanceof KineticFlapBearingBlock) {
            Direction direction = (Direction)this.getBlockState().getValue((Property)BlockStateProperties.FACING);
            FlapContraption contraption = null;

            try {
                FlapContraption.Companion var5 = FlapContraption.Companion;
                Level var10001 = this.level;
                BlockPos var10002 = this.worldPosition;
                contraption = var5.assembleFlap(var10001, var10002, direction);
                this.lastException = null;
            } catch (AssemblyException var4) {
                this.lastException = var4;
                this.sendData();
                return;
            }

            if (contraption != null) {
                if (!contraption.getBlocks().isEmpty()) {
                    BlockPos anchor = this.worldPosition.relative(direction);
                    contraption.removeBlocksFromWorld(this.level, BlockPos.ZERO);
                    this.flap = ControlledContraptionEntity.create(this.level, this, contraption);
                    ControlledContraptionEntity var6 = this.flap;
                    var6.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
                    var6 = this.flap;
                    var6.setRotationAxis(direction.getAxis());
                    var10000 = this.level;
                    ControlledContraptionEntity var7 = this.flap;
                    var10000.addFreshEntity(var7);
                    this.isRunning = true;
                    this.bearingAngle = 0.0F;
                    this.sendData();
                }
            }
        }
    }

    public void remove() {
        if (!level.isClientSide) {
            this.disassemble();
        }

        super.remove();
    }

    public final void disassemble() {
        if (this.isRunning || this.flap != null) {
            this.bearingAngle = 0.0F;
            this.applyRotations();
            if (this.flap != null) {
                ControlledContraptionEntity var10000 = this.flap;
                Intrinsics.checkNotNull(var10000);
                var10000.disassemble();
            }

            this.flap = null;
            this.isRunning = false;
            this.sendData();
        }
    }

    protected final void applyRotations() {
        BlockState blockState = this.getBlockState();
        Direction.Axis axis = Direction.Axis.X;
        if (blockState.hasProperty((Property)BlockStateProperties.FACING)) {
            axis = ((Direction)blockState.getValue((Property)BlockStateProperties.FACING)).getAxis();
        }

        if (this.flap != null) {
            ControlledContraptionEntity var10000 = this.flap;
            Intrinsics.checkNotNull(var10000);
            var10000.setAngle(this.bearingAngle);
            var10000 = this.flap;
            Intrinsics.checkNotNull(var10000);
            var10000.setRotationAxis(axis);
        }

    }

    public void attach(@NotNull ControlledContraptionEntity contraption) {
        Intrinsics.checkNotNullParameter(contraption, "contraption");
        BlockState blockState = this.getBlockState();
        if (contraption.getContraption() instanceof FlapContraption) {
            if (blockState.hasProperty((Property) BearingBlock.FACING)) {
                this.flap = contraption;
                this.setChanged();
                BlockPos anchor = this.worldPosition.relative((Direction)blockState.getValue((Property)BearingBlock.FACING));
                ControlledContraptionEntity var10000 = this.flap;
                Intrinsics.checkNotNull(var10000);
                var10000.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
                Level var4 = this.level;
                Intrinsics.checkNotNull(var4);
                if (!var4.isClientSide) {
                    this.isRunning = true;
                    this.sendData();
                }

            }
        }
    }

    public void lazyTick() {
        super.lazyTick();
        if (this.flap != null) {
            Level var10000 = this.level;
            Intrinsics.checkNotNull(var10000);
            if (!var10000.isClientSide) {
                this.sendData();
            }
        }

    }

    public final float getFlapSpeed() {
        float speed = this.getAngularSpeed() / 2.0F;
        if (speed != 0.0F) {
            float flapTarget = this.getFlapTarget(this.redstoneSideOne, this.redstoneSideTwo);
            float shortestAngleDiff = AngleHelper.getShortestAngleDiff(this.bearingAngle, flapTarget);
            speed = shortestAngleDiff < 0.0F ? Math.max(speed, shortestAngleDiff) : Math.min(-speed, shortestAngleDiff);
        }

        return speed + this.clientAngleDiff / 3.0F;
    }

    protected final float getFlapTarget(boolean negativeActivated, boolean positiveActivated) {
        if (negativeActivated && !positiveActivated) {
            return -22.5F * ((float)this.redstoneLevel / (float)15);
        } else if (positiveActivated && !negativeActivated) {
            return 22.5F * ((float)this.redstoneLevel / (float)15);
        } else {
            return 0.0F;
        }
    }

    public void setAngle(float forcedAngle) {
        this.bearingAngle = forcedAngle;
    }

    public final float getAngularSpeed() {
        float speed = -Math.abs(this.getSpeed() * (float)3 / 10.0F);
        if (level.isClientSide) {
            speed *= ServerSpeedProvider.get();
        }

        return speed;
    }
    @Override
    public float getInterpolatedAngle(float partialTicks) {
        if (this.isVirtual()) {
            return Mth.lerp(partialTicks, this.prevForcedAngle, this.bearingAngle);
        } else {
            if (this.flap != null) {
                ControlledContraptionEntity var10000 = this.flap;
                Intrinsics.checkNotNull(var10000);
                if (!var10000.isStalled()) {
                    return Mth.lerp(partialTicks, this.bearingAngle, this.bearingAngle + this.getFlapSpeed());
                }
            }

            partialTicks = 0.0F;
            return Mth.lerp(partialTicks, this.bearingAngle, this.bearingAngle + this.getFlapSpeed());
        }
    }

    public boolean isWoodenTop() {
        return false;
    }

    public boolean isAttachedTo(@NotNull AbstractContraptionEntity contraption) {
        return contraption.getContraption() instanceof FlapContraption && this.flap == contraption;
    }

    public void onStall() {
        Level var10000 = this.level;
        Intrinsics.checkNotNull(var10000);
        if (!var10000.isClientSide) {
            this.sendData();
        }

    }

    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        this.assembleNextTick = true;
    }

    public boolean isValid() {
        return !this.isRemoved();
    }

    @NotNull
    public BlockPos getBlockPosition() {
        BlockPos var10000 = this.worldPosition;
        Intrinsics.checkNotNullExpressionValue(var10000, "worldPosition");
        return var10000;
    }
    private enum PoweredMode implements INamedIconOptions {
        NORMAL(AllIcons.I_ROTATE_PLACE,"contraptions.kinetic_flap_bearing.normal"),
        INVERTED(AllIcons.I_ROTATE_NEVER_PLACE,"contraptions.kinetic_flap_bearing.inverted");

        PoweredMode(AllIcons icon, String translationKey) {
            this.translationKey = translationKey;
            this.icon = icon;
        }
        final String translationKey;
        final AllIcons icon;
        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }
}

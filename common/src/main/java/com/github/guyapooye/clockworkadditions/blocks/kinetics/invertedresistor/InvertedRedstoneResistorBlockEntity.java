package com.github.guyapooye.clockworkadditions.blocks.kinetics.invertedresistor;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InvertedRedstoneResistorBlockEntity extends SplitShaftBlockEntity implements IHaveGoggleInformation {
    private int state;
    private int lastChange;

    public InvertedRedstoneResistorBlockEntity(@Nullable BlockEntityType type, @NotNull BlockPos pos, @NotNull BlockState state) {
        super(type, pos, state);
    }

    public final int getState() {
        return this.state;
    }

    public final void setState(int set) {
        this.state = set;
    }

    public final int getLastChange() {
        return this.lastChange;
    }

    public final void setLastChange(int set) {
        this.lastChange = set;
    }

    public void tick() {
        super.tick();
        this.lastChange = this.state;
        this.state = this.getPower(this.level, this.worldPosition);
        if (this.state != this.lastChange) {
            this.detachKinetics();
        }

    }

    public void detachKinetics() {
        RotationPropagator.handleRemoved(this.level, this.worldPosition, this);
        this.level.scheduleTick(this.worldPosition, this.getBlockState().getBlock(), 0, TickPriority.EXTREMELY_HIGH);
    }

    public void write(@NotNull CompoundTag compound, boolean clientPacket) {
        compound.putInt("RedstoneLevel", this.state);
        compound.putInt("ChangeTimer", this.lastChange);
        super.write(compound, clientPacket);
    }

    protected void read(@NotNull CompoundTag compound, boolean clientPacket) {
        this.state = compound.getInt("RedstoneLevel");
        this.lastChange = compound.getInt("ChangeTimer");
        super.read(compound, clientPacket);
    }

    private int getPower(Level worldIn, BlockPos pos) {
        int power = 0;
        Direction[] var4 = Iterate.directions;
        Direction direction;
        for (Direction value : var4) {
            direction = value;
            power = Math.max(worldIn.getSignal(pos.relative(direction), direction), power);
        }

        for (Direction value : var4) {
            direction = value;
            power = Math.max(worldIn.getSignal(pos.relative(direction), Direction.UP), power);
        }

        return power;
    }

    public float getRotationSpeedModifier(@NotNull Direction face) {
        if (this.hasSource() && face != this.getSourceFacing()) {
            return (float)Math.abs(this.getState()) / 15.0F;
        } else {
            return 1.0F;
        }
    }

    public boolean addToGoggleTooltip(@NotNull List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(SplitShaftBlockEntity.componentSpacing.plainCopy().append(Lang.translateDirect("tooltip.analogStrength", this.state)));
        return true;
    }
}

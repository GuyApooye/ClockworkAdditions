package com.github.guyapooye.clockworkadditions.blocks.redstone.gyro;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

import static com.github.guyapooye.clockworkadditions.blocks.redstone.AbstractFourSidedPoweredBlock.*;
import static com.github.guyapooye.clockworkadditions.blocks.redstone.gyro.RedstoneGyroBlockEntity.Mode.PID;
import static com.github.guyapooye.clockworkadditions.util.NumberUtil.isNaN;
import static java.lang.Math.abs;
import static java.lang.Math.round;
import static net.minecraft.util.Mth.sign;

public class RedstoneGyroBlockEntity extends SmartBlockEntity {
    private int signalNorth;
    private int signalSouth;
    private int signalEast;
    private int signalWest;
    private Vector3d integralVector = new Vector3d();
    private Vector3d lastTransformedUp = new Vector3d();

    private ScrollOptionBehaviour<Mode> operationMode;

    public RedstoneGyroBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public ValueBoxTransform getOutputModeSlot() {
        return new CenteredSideValueBoxTransform((state, d) -> d==Direction.UP);
    }
    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag,clientPacket);
        tag.putInt("SignalNorth",signalNorth);
        tag.putInt("SignalSouth",signalSouth);
        tag.putInt("SignalEast",signalEast);
        tag.putInt("SignalWest",signalWest);
    }
    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        signalNorth = tag.getInt("SignalNorth");
        signalSouth = tag.getInt("SignalSouth");
        signalEast = tag.getInt("SignalEast");
        signalWest = tag.getInt("SignalWest");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        operationMode = new ScrollOptionBehaviour<>(Mode.class,
                ClockworkAdditions.asTranslatable("circuits.gyro.output_mode"), this, getOutputModeSlot());
        behaviours.add(operationMode);
    }

    public void tick() {
        super.tick();
        BlockPos pos = getBlockPos();
        if (level.isClientSide) return;
        Ship ship = VSGameUtilsKt.getShipManagingPos(level,pos);
        if (ship == null) return;
        if (operationMode.get() == PID) {
            tickPID(ship, pos);
            return;
        }
        BlockState newState = level.getBlockState(pos).getBlock().defaultBlockState();
        Vector3d shipUp = new Vector3d(0,1,0);
        ship.getTransform().getShipToWorldRotation().transformInverse(shipUp);
        double x1 = shipUp.x;
        double z1 = shipUp.z;
//        writeSignalsRotation(x,z);
        Vector3d shipOmega = (Vector3d) ship.getOmega();
        ship.getTransform().getShipToWorldRotation().transformInverse(shipOmega);
        shipOmega.rotateAxis(90,0,1,0,shipOmega);
        double x2 = shipOmega.x;
        double z2 = shipOmega.z;
        switch (operationMode.get()) {
            case OFF -> toSignals(0,0,0,0);
            case POWERED -> toSignals(15,15,15,15);
            case TILT -> writeSignals(x1,z1,0,0);
            case OMEGA -> writeSignals(0,0,x2,z2);
            case BOTH -> writeSignals(x1,z1,x2,z2);
        }
        sendState(pos);
    }
    public void writeSignals(double x1, double z1, double x2, double z2) {
        final float sensitivity1 = ConfigRegistry.server().redstone.gyro.sensitivityToRotation.getF();
        final float sensitivity2 = ConfigRegistry.server().redstone.gyro.sensitivityToOmega.getF();
        int signalX = (int) Math.min(round(abs(x1*sensitivity1+x2*sensitivity2)*15),15);
        int signalZ = (int) Math.min(round(abs(z1*sensitivity1+z2*sensitivity2)*15),15);
        toSignals(sign(x1+x2) == -1 ? signalX : 0,
                sign(x1+x2) == 1 ? signalX : 0,
                sign(z1+z2) == -1 ? signalZ : 0,
                sign(z1+z2) == 1 ? signalZ : 0);
    }
    public void toSignals(int east, int west, int south, int north) {
        signalEast = east;
        signalWest = west;
        signalSouth = south;
        signalNorth = north;
    }
    public void writeSignalsPID(double x, double z) {
        int signalX = (int) Math.min(round(abs(x)*15),15);
        int signalZ = (int) Math.min(round(abs(z)*15),15);
        signalEast = sign(x) == -1 ? signalX : 0;
        signalWest = sign(x) == 1 ? signalX : 0;
        signalSouth = sign(z) == -1 ? signalZ : 0;
        signalNorth = sign(z) == 1 ? signalZ : 0;
    }
    public void sendState(BlockPos pos) {
        BlockState newState = level.getBlockState(pos);
        level.setBlock(pos,newState
                .setValue(EAST,signalEast != 0)
                .setValue(WEST,signalWest != 0)
                .setValue(SOUTH,signalSouth != 0)
                .setValue(NORTH,signalNorth != 0),3);
        level.blockUpdated(pos,newState.getBlock());
    }
    public void tickPID(Ship ship,BlockPos pos) {
        Vector3d shipUp, worldUp = new Vector3d(0,1,0);
        shipUp = ship.getTransform().getShipToWorldRotation().transform(worldUp, new Vector3d());
        double errorAngle = shipUp.angle(worldUp);
//        System.out.println(errorAngle);
        Vector3d proportionalVector = ship.getTransform().getShipToWorldRotation().transformInverse(worldUp, new Vector3d());
        System.out.println("Proportional: "+ proportionalVector);
        double x1 = proportionalVector.x;
        double z1 = proportionalVector.z;
//        System.out.println("first X1: " + x1);
//        System.out.println("first Z1: " + z1);
        proportionalVector.normalize(-errorAngle/400);
        integralVector = integralVector.add(new Vector3d(isNaN(proportionalVector.x),isNaN(proportionalVector.y),isNaN(proportionalVector.z)));
        System.out.println("Integral: "+ integralVector);
        x1 += integralVector.x;
        z1 += integralVector.z;
//        System.out.println("second X1: " + x1);
//        System.out.println("second Z1: " + z1);
        Vector3d errorDerivative = lastTransformedUp.sub(proportionalVector).mul(20, new Vector3d());
        System.out.println("Derivative:" + errorDerivative);
        x1 += errorDerivative.x;
        z1 += errorDerivative.y;
//        System.out.println("X: "+x1);
//        System.out.println("Z: "+z1);
        writeSignalsPID(x1,z1);
        sendState(pos);
        lastTransformedUp = worldUp;
    }
    enum Mode implements INamedIconOptions {
        OFF(AllIcons.I_FX_FIELD_OFF,"redstone.gyro.unpowered"),
        TILT(AllIcons.I_ROTATE_NEVER_PLACE,"redstone.gyro.tilt"),
        OMEGA(AllIcons.I_ROTATE_PLACE_RETURNED,"redstone.gyro.omega"),
        BOTH(AllIcons.I_ROTATE_PLACE,"redstone.gyro.both"),
        PID(AllIcons.I_FX_BLEND,"redstone.gyro.pid"),
        POWERED(AllIcons.I_FX_FIELD_ON,"redstone.gyro.powered");
        final AllIcons icon;
        final String translationKey;

        Mode(AllIcons icon, String translationKey) {
            this.icon = icon;
            this.translationKey = translationKey;
        }

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

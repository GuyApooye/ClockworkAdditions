package com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.tterrag.registrate.fabric.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Conversion;

import javax.annotation.Nullable;
import java.util.*;

import static java.lang.Math.*;
import static net.minecraft.util.Mth.sign;

public abstract class HandlebarBlockEntity extends GeneratingKineticBlockEntity {
    protected UUID user;
    protected UUID prevUser;	// used only on client
    protected boolean deactivatedThisTick;	// used only on server
    public float independentAngle;
    public float maxAngle;
    public float velocity;
    public final float configAngle = ConfigRegistry.server().kinetics.handlebar.handleMaxAngle.getF();
    public float baseSpeed = ConfigRegistry.server().kinetics.handlebar.handleSpeed.get();
    public float sign = 1;
    public float multiplier = 1;
    private Collection<Integer> pressedKeys = new HashSet<>();

    public HandlebarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) { }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (user != null)
            compound.putUUID("User", user);
//        compound.putFloat("Angle",independentAngle);
    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        user = compound.hasUUID("User") ? compound.getUUID("User") : null;
//        independentAngle = compound.getFloat("Angle");
    }
    public float getIndependentAngle(float partialTicks) {

//        updateVars();
        float angle = (independentAngle+partialTicks*velocity)/360;
//        if (round(maxAngle-(independentAngle+getSpeed())/360*70) == 0) {
//            angle = maxAngle*360/70;
////            updateGeneratedRotation();
//        }
        return angle;
    }


    public boolean hasUser() { return user != null; }

    public boolean isUsedBy(Player player) {
        return hasUser() && user.equals(player.getUUID());
    }

    public void tryStartUsing(Player player) {
        if (!deactivatedThisTick && !hasUser() && !this.playerIsUsingHandle(player) && playerInRange(player, level, worldPosition))
            startUsing(player);
    }
    public void updateInput(Collection<Integer> keys) {
//        if (!Objects.equals(this.pressedKeys, keys)) {
//            this.pressedKeys = keys;
//            updateGeneratedRotation();
//        }
        this.pressedKeys = keys;
        updateGeneratedRotation();
    }
    public void updateVars() {
        float dummyMax = 0;
        if (pressedKeys.contains(2)) {
            dummyMax += ConfigRegistry.server().kinetics.handlebar.handleMaxAngle.getF();
        }
        if (pressedKeys.contains(3)) {
            dummyMax -= ConfigRegistry.server().kinetics.handlebar.handleMaxAngle.getF();
        }
        multiplier = pressedKeys.contains(6) ? 2 : 1;
//        System.out.println(pressedKeys);
        maxAngle = dummyMax;
    }

    public void pleaseStopUsing(Player player) {
        stopUsing(player);
    }
    public void tryStopUsing() {
        Entity playerEntity = ((ServerLevel) level).getEntity(user);
        if (playerEntity instanceof Player)
            if (isUsedBy((Player) playerEntity))
                stopUsing((Player) playerEntity);
    }

    protected abstract void startUsing(Player player);

    protected abstract void stopUsing(Player player);

    public abstract boolean playerIsUsingHandle(Player player);
    @Override
    public float getGeneratedSpeed() {
        updateVars();
        sign = sign(round(maxAngle-independentAngle/360*70));
        float speed = sign*ConfigRegistry.server().kinetics.handlebar.handleSpeed.get()*multiplier;
        return speed;
    }

    @Environment(EnvType.CLIENT)
    protected void tryToggleActive() {
        if (user == null && Minecraft.getInstance().player.getUUID().equals(prevUser)) {
            HandlebarClientHandler.deactivate();
        } else if (prevUser == null && Minecraft.getInstance().player.getUUID().equals(user)) {
            HandlebarClientHandler.activate(worldPosition);
        }
    }
    protected abstract void runWhenOn();

    @Override
    public void tick() {
        super.tick();
//        independentAngle += getSpeed();

        float actualSpeed = getSpeed();
        velocity = actualSpeed;
        independentAngle += velocity;
        if (sign(round(maxAngle-independentAngle/360*70)) == 0 /*|| sign(round(maxAngle-(independentAngle+getSpeed())/360*70)) ==0*/ ) {
            independentAngle = maxAngle*360/70;
            updateGeneratedRotation();
        }

        runWhenOn();

        if (!level.isClientSide) {
            deactivatedThisTick = false;

            if (!(level instanceof ServerLevel))
                return;
            if (user == null)
                return;

            Entity entity = ((ServerLevel) level).getEntity(user);
            if (!(entity instanceof Player)) {
                stopUsing(null);
                return;
            }

            Player player = (Player) entity;
            if (!playerInRange(player, level, worldPosition) || !this.playerIsUsingHandle(player))
                stopUsing(player);
        }
    }
    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        //double modifier = world.isRemote ? 0 : 1.0;
        double reach = 0.7* (player.isCreative() ? 5 : 4.5);// + modifier;
        return player.distanceToSqr(Vec3.atCenterOf(pos)) < reach*reach;
    }

}

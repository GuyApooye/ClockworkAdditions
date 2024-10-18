package com.github.guyapooye.clockworkadditions.util.fabric;

import com.tterrag.registrate.fabric.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class PlatformUtilImpl {
    public static CompoundTag getPlayerCustomData(Player player) {
        return player.getCustomData();
    }
    public static void runWhenOn(Enum<?> e, Runnable supplier) {
        EnvExecutor.runWhenOn((EnvType) e,() -> supplier);
    }
}

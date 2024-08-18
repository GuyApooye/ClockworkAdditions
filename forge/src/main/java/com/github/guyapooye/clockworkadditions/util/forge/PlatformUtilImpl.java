package com.github.guyapooye.clockworkadditions.util.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class PlatformUtilImpl {
    public static CompoundTag getPlayerCustomData(Player player) {
        return player.getPersistentData();
    }
    public static void runWhenOn(Enum e, Supplier<Runnable> supplier) {
        DistExecutor.unsafeRunWhenOn((Dist) e,supplier);
    }
}

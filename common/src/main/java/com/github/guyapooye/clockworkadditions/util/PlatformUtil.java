package com.github.guyapooye.clockworkadditions.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class PlatformUtil {
    @ExpectPlatform
    public static CompoundTag getPlayerCustomData(Player player) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void runWhenOn(Enum e, Supplier<Runnable> supplier) {
        throw new AssertionError();
    }
}

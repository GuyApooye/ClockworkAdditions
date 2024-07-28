package com.github.guyapooye.clockworkaddons.forge;

import com.github.guyapooye.clockworkaddons.ClockworkAddons;
import com.github.guyapooye.clockworkaddons.forge.registries.EntityRegistryImpl;
import net.minecraftforge.fml.common.Mod;

import static com.github.guyapooye.clockworkaddons.ClockworkAddons.*;

@Mod(MOD_ID)
public class ClockworkAddonsForge {
    public ClockworkAddonsForge() {
        ClockworkAddons.init();
        EntityRegistryImpl.register();
        registerRegistrate();
    }
}
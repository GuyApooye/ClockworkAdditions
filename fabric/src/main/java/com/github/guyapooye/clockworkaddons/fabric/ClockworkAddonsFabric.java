package com.github.guyapooye.clockworkaddons.fabric;

import com.github.guyapooye.clockworkaddons.ClockworkAddons;
import com.github.guyapooye.clockworkaddons.fabric.registries.EntityRegistryImpl;
import net.fabricmc.api.ModInitializer;

import static com.github.guyapooye.clockworkaddons.ClockworkAddons.registerRegistrate;

public class ClockworkAddonsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClockworkAddons.init();
        EntityRegistryImpl.register();
        registerRegistrate();
    }
}
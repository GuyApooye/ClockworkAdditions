package com.github.guyapooye.clockworkadditions.fabric;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.github.guyapooye.clockworkadditions.fabric.registries.BlockEntityRegistryImpl;
import com.github.guyapooye.clockworkadditions.fabric.registries.ConfigRegistryImpl;
import com.github.guyapooye.clockworkadditions.fabric.registries.EntityRegistryImpl;
import com.github.guyapooye.clockworkadditions.fabric.registries.events.ClientEventRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.*;

public class ClockworkAdditionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation("clockworkadditions")), CWACreativeModeTab);
        ClockworkAdditions.init();
        EntityRegistryImpl.register();
        ConfigRegistryImpl.register();
        BlockEntityRegistryImpl.register();
        ClientEventRegistry.register();
        REGISTRATE.register();
    }
}
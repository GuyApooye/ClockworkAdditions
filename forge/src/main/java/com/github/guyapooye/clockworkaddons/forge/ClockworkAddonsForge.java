package com.github.guyapooye.clockworkaddons.forge;

import com.github.guyapooye.clockworkaddons.ClockworkAddons;
import com.github.guyapooye.clockworkaddons.forge.registries.EntityRegistryImpl;
import com.github.guyapooye.clockworkaddons.registries.BlockRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.github.guyapooye.clockworkaddons.ClockworkAddons.*;

@Mod(MOD_ID)
public class ClockworkAddonsForge {
    private void initializeCreativeModeTab() {
        CWACreativeModeTab = new CreativeModeTab("clockworkaddons") {
            @Override
            public ItemStack makeIcon() {
                return BlockRegistry.KINETIC_FLAP_BEARING.asStack();
            }
        };
    }
    public ClockworkAddonsForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        initializeCreativeModeTab();
        ClockworkAddons.init();
        EntityRegistryImpl.register();
    }
}
package com.github.guyapooye.clockworkaddons.fabric;

import com.github.guyapooye.clockworkaddons.ClockworkAddons;
import com.github.guyapooye.clockworkaddons.fabric.registries.EntityRegistryImpl;
import com.github.guyapooye.clockworkaddons.registries.BlockRegistry;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static com.github.guyapooye.clockworkaddons.ClockworkAddons.CWACreativeModeTab;
import static com.github.guyapooye.clockworkaddons.ClockworkAddons.REGISTRATE;

public class ClockworkAddonsFabric implements ModInitializer {
    private void initializeCreativeModeTab() {
        CWACreativeModeTab = new CreativeModeTab(ItemGroupUtil.expandArrayAndGetId(),"clockworkaddons") {
            @Override
            public ItemStack makeIcon() {
                return BlockRegistry.KINETIC_FLAP_BEARING.asStack();
            }
        };
    }
    @Override
    public void onInitialize() {
        initializeCreativeModeTab();
        ClockworkAddons.init();
        EntityRegistryImpl.register();
        REGISTRATE.register();
    }
}
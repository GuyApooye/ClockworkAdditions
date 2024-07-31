package com.github.guyapooye.clockworkadditions.fabric;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.github.guyapooye.clockworkadditions.fabric.registries.EntityRegistryImpl;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.CWACreativeModeTab;
import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class ClockworkAdditionsFabric implements ModInitializer {
    private void initializeCreativeModeTab() {
        CWACreativeModeTab = new CreativeModeTab(ItemGroupUtil.expandArrayAndGetId(),"clockworkadditions") {
            @Override
            public ItemStack makeIcon() {
                return BlockRegistry.KINETIC_FLAP_BEARING.asStack();
            }
        };
    }
    @Override
    public void onInitialize() {
        initializeCreativeModeTab();
        ClockworkAdditions.init();
        EntityRegistryImpl.register();
        REGISTRATE.register();
    }
}
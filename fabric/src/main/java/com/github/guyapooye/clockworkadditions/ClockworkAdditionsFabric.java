package com.github.guyapooye.clockworkadditions;

import com.github.guyapooye.clockworkadditions.registries.fabric.BlockRegistryImpl;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistryImpl;
import com.github.guyapooye.clockworkadditions.registries.fabric.EntityRegistryImpl;
import com.github.guyapooye.clockworkadditions.registries.events.ClientEventRegistry;
import com.github.guyapooye.clockworkadditions.registries.*;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.*;

public class ClockworkAdditionsFabric implements ModInitializer {
    private void initializeCreativeModeTab() {
        CWACreativeModeTab = new CreativeModeTab(ItemGroupUtil.expandArrayAndGetId(),MOD_ID) {
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
        ConfigRegistryImpl.register();
        BlockRegistryImpl.register();
        ClientEventRegistry.register();
        REGISTRATE.register();
    }
}
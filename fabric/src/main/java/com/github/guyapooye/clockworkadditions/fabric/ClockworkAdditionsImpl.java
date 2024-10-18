package com.github.guyapooye.clockworkadditions.fabric;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.CWACreativeModeTab;
import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.MOD_ID;

public class ClockworkAdditionsImpl {
    public static CreativeModeTab registerCreativeModeTab() {
        return new CreativeModeTab(ItemGroupUtil.expandArrayAndGetId(),MOD_ID) {
            @Override
            public ItemStack makeIcon() {
                return BlockRegistry.KINETIC_FLAP_BEARING.asStack();
            }
        };
    }
}

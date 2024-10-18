package com.github.guyapooye.clockworkadditions.forge;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.MOD_ID;

public class ClockworkAdditionsImpl {
    public static CreativeModeTab registerCreativeModeTab() {
        return new CreativeModeTab(MOD_ID) {
            @Override
            public ItemStack makeIcon() {
                return BlockRegistry.KINETIC_FLAP_BEARING.asStack();
            }
        };
    }
}

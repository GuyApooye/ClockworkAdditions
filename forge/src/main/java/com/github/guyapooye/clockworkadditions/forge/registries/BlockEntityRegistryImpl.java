package com.github.guyapooye.clockworkadditions.forge.registries;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarInstance;
import com.github.guyapooye.clockworkadditions.forge.handlebar.HandlebarBlockEntityImpl;
import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class BlockEntityRegistryImpl extends BlockEntityRegistry {
    static {
        HANDLEBAR = REGISTRATE
                .blockEntity("handlebar", HandlebarBlockEntityImpl::new)
                .instance(() -> HandlebarInstance::new)
                .validBlocks(BlockRegistry.HANDLEBAR)
                .register();
    }
    public static void register() {}
}

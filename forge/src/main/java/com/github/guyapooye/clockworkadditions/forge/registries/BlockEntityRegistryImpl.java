package com.github.guyapooye.clockworkadditions.forge.registries;

import com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose.ExtensibleHoseInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarInstance;
import com.github.guyapooye.clockworkadditions.forge.blocks.copycats.CWACopycatBlockEntityImpl;
import com.github.guyapooye.clockworkadditions.forge.blocks.fluid.extensible_hose.ExtensibleHoseBlockEntityImpl;
import com.github.guyapooye.clockworkadditions.forge.blocks.kinetics.handlebar.HandlebarBlockEntityImpl;
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
        COPYCAT = REGISTRATE.blockEntity("copycat", CWACopycatBlockEntityImpl::new)
                .validBlocks(BlockRegistry.COPYCAT_WING)
                .register();
        EXTENSIBLE_HOSE = REGISTRATE
                .blockEntity("extensible_hose", ExtensibleHoseBlockEntityImpl::new)
                .instance(() -> ExtensibleHoseInstance::new)
                .validBlocks(BlockRegistry.EXTENSIBLE_HOSE)
                //.renderer(() -> CVJointRenderer::new)
                .register();
    }
    public static void register() {}
}

package com.github.guyapooye.clockworkadditions.registries.fabric;

import com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose.ExtensibleHoseBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose.ExtensibleHoseBlockEntityImpl;
import com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose.ExtensibleHoseInstance;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class BlockEntityRegistryImpl {
    public static BlockEntityEntry<? extends ExtensibleHoseBlockEntity<?>> registerExtensibleHose() {
        return REGISTRATE
                .blockEntity("extensible_hose", ExtensibleHoseBlockEntityImpl::new)
                .instance(() -> ExtensibleHoseInstance::new)
                .validBlocks(BlockRegistry.EXTENSIBLE_HOSE)
                .register();
    }
}

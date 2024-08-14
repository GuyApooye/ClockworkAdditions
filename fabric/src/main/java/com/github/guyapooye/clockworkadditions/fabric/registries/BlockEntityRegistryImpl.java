package com.github.guyapooye.clockworkadditions.fabric.registries;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarInstance;
import com.github.guyapooye.clockworkadditions.fabric.blocks.copycats.CWACopycatBlockEntityImpl;
import com.github.guyapooye.clockworkadditions.fabric.blocks.kinetics.handlebar.HandlebarBlockEntityImpl;
import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class BlockEntityRegistryImpl extends BlockEntityRegistry {
    static {
        HANDLEBAR = REGISTRATE
                .blockEntity("handlebar", HandlebarBlockEntityImpl::new)
                .instance(() -> HandlebarInstance::new)
                .validBlocks(BlockRegistry.HANDLEBAR)
                .register();
        COPYCAT = REGISTRATE
                .blockEntity("copycat", CWACopycatBlockEntityImpl::new)
                .validBlocks(BlockRegistry.COPYCAT_WING)
                .register();
    }
    public static void register() {}
}

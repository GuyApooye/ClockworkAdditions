package com.github.guyapooye.clockworkadditions.registries.forge;

import com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes.*;
import com.github.guyapooye.clockworkadditions.forge.BuilderTransformersImpl;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class BlockRegistryImpl extends BlockRegistry {
    public static BlockEntry<CopycatWingBlockImpl> registerCopycatWing() {
        return REGISTRATE.block("copycat_wing", CopycatWingBlockImpl::new)
                .transform(BuilderTransformers.copycat())
                .onRegister(CreateRegistrate.blockModel(() -> CopycatWingalikeModel::new))
                .item()
                .transform(customItemModel("copycat_base", "wing"))
                .register();
    }
    public static BlockEntry<CopycatFlapBlockImpl> registerCopycatFlap() {
        return REGISTRATE.block("copycat_flap", CopycatFlapBlockImpl::new)
                .transform(BuilderTransformers.copycat())
                .onRegister(CreateRegistrate.blockModel(() -> CopycatWingalikeModel::new))
                .item()
                .transform(customItemModel("copycat_base", "flap"))
                .register();
    }
    public static void register() {}
}

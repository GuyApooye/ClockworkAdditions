package com.github.guyapooye.clockworkadditions.fabric.registries;

import com.github.guyapooye.clockworkadditions.fabric.BuilderTransformersImpl;
import com.github.guyapooye.clockworkadditions.fabric.blocks.copycats.wingalikes.CopycatFlapBlockImpl;
import com.github.guyapooye.clockworkadditions.fabric.blocks.copycats.wingalikes.CopycatWingBlockImpl;
import com.github.guyapooye.clockworkadditions.fabric.blocks.copycats.wingalikes.CopycatWingalikeModel;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.foundation.data.CreateRegistrate;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class BlockRegistryImpl extends BlockRegistry {
    static {
        COPYCAT_WING =
                REGISTRATE.block("copycat_wing", CopycatWingBlockImpl::new)
                        .transform(BuilderTransformersImpl.copycat())
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatWingalikeModel::new))
                        .item()
                        .transform(customItemModel("copycat_base", "wing"))
                        .register();
        COPYCAT_FLAP =
                REGISTRATE.block("copycat_flap", CopycatFlapBlockImpl::new)
                        .transform(BuilderTransformersImpl.copycat())
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatWingalikeModel::new))
                        .item()
                        .transform(customItemModel("copycat_base", "flap"))
                        .register();
    }
    public static void register() {}
}

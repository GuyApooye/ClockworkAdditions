package com.github.guyapooye.clockworkadditions.config;


import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class CWAConfigBase extends ConfigBase {
    public void registerAll(ForgeConfigSpec.Builder builder) {
        if (this.allValues != null) {
            super.registerAll(builder);
        }

    }
}

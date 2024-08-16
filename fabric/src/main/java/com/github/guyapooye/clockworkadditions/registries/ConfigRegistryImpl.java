package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.config.CWAClient;
import com.github.guyapooye.clockworkadditions.config.CWACommon;
import com.github.guyapooye.clockworkadditions.config.CWAServer;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.MOD_ID;

public class ConfigRegistryImpl extends ConfigRegistry {
    public static void register() {
        client = register(CWAClient::new, ModConfig.Type.CLIENT);
        common = register(CWACommon::new, ModConfig.Type.COMMON);
        server = register(CWAServer::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            ModLoadingContext.registerConfig(MOD_ID, pair.getKey(), pair.getValue().specification);

        BlockStressValues.registerProvider(MOD_ID, server().kinetics.stressValues);

        ModConfigEvent.LOADING.register(ConfigRegistryImpl::onLoad);
        ModConfigEvent.RELOADING.register(ConfigRegistryImpl::onReload);
    }

    public static void onLoad(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onReload();
    }
}

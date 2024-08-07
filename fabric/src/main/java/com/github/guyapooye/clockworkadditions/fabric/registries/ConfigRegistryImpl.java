package com.github.guyapooye.clockworkadditions.fabric.registries;

import com.github.guyapooye.clockworkadditions.config.CWAClient;
import com.github.guyapooye.clockworkadditions.config.CWACommon;
import com.github.guyapooye.clockworkadditions.config.CWAServer;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.MOD_ID;

public class ConfigRegistryImpl extends ConfigRegistry {
    public static void register() {
        client = register(CWAClient::new, ModConfig.Type.CLIENT);
        common = register(CWACommon::new, ModConfig.Type.COMMON);
        server = register(CWAServer::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            ForgeConfigRegistry.INSTANCE.register(MOD_ID, pair.getKey(), pair.getValue().specification);

        BlockStressValues.registerProvider(MOD_ID, server().kinetics.stressValues);

        ModConfigEvents.loading(MOD_ID).register(ConfigRegistryImpl::onLoad);
        ModConfigEvents.reloading(MOD_ID).register(ConfigRegistryImpl::onReload);
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

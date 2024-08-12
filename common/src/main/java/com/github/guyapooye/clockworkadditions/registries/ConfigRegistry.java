package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.config.CWAClient;
import com.github.guyapooye.clockworkadditions.config.CWACommon;
import com.github.guyapooye.clockworkadditions.config.CWAConfigBase;
import com.github.guyapooye.clockworkadditions.config.CWAServer;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConfigRegistry {
    protected static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    protected static CWAClient client;
    protected static CWACommon common;
    protected static CWAServer server;

    public static CWAClient client() {
        return client;
    }

    public static CWACommon common() {
        return common;
    }

    public static CWAServer server() {return server;}

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    protected static <T extends CWAConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure((builder) -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });
        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }
}

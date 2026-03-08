//package com.github.guyapooye.clockworkadditions.registries;
//
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.ModLoadingContext;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.config.ModConfigEvent;
//
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//public class ConfigRegistryImpl extends ConfigRegistry {
//    public static void register(ModLoadingContext context) {
////        client = register(CWAClient::new, ModConfig.Type.CLIENT);
////        common = register(CWACommon::new, ModConfig.Type.COMMON);
////        server = register(CWAServer::new, ModConfig.Type.SERVER);
//
////        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
////            context.registerConfig(pair.getKey(), pair.getValue().specification);
////
////        BlockStressValues.registerProvider(context.getActiveNamespace(), server().kinetics.stressValues);
//    }
//
//    @SubscribeEvent
//    public static void onLoad(ModConfigEvent.Loading event) {
////        for (ConfigBase config : CONFIGS.values())
////            if (config.specification == event.getConfig()
////                    .getSpec())
////                config.onLoad();
//    }
//
//    @SubscribeEvent
//    public static void onReload(ModConfigEvent.Reloading event) {
////        for (ConfigBase config : CONFIGS.values())
////            if (config.specification == event.getConfig()
////                    .getSpec())
////                config.onReload();
//    }
//}

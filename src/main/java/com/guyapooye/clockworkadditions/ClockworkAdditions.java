package com.guyapooye.clockworkadditions;

import com.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.guyapooye.clockworkadditions.registries.EntityRegistry;
import com.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

@Mod("clockworkadditions")
public class ClockworkAdditions
{
	public static final String MOD_ID = "clockworkadditions";

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public ClockworkAdditions() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		REGISTRATE.registerEventListeners(modEventBus);

		BlockRegistry.register();
		EntityRegistry.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		BlockEntityRegistry.register();

		if (!FMLEnvironment.dist.isDedicatedServer()) {
			PartialModelRegistry.register();
		}
	}


	@NotNull
	public static ResourceLocation asResource(@NotNull String path) {
		return new ResourceLocation("clockworkadditions", path);
	}
	public static Component asTranslatable(String translatable) {
		return Component.translatable(translatable);
	}
}

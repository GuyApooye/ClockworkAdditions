package com.guyapooye.clockworkadditions;

import com.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

@Mod("clockworkadditions")
public class ClockworkAdditions
{
	public static final String MOD_ID = "clockworkadditions";

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public ClockworkAdditions() {
		DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "com/guyapooye/clockworkadditions");

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		REGISTRATE.registerEventListeners(modEventBus);
//		TAB_REGISTER.register("general", () -> CWACreativeModeTab);
		TAB_REGISTER.register(modEventBus);

		ClockworkAdditions.init();

		ModLoadingContext modLoadingContext = ModLoadingContext.get();
	}

	//public static final CreativeModeTab CWACreativeModeTab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.clockworkadditions")).icon(BlockRegistry.HANDLEBAR::asStack).displayItems(ClockworkAdditions::allItems).build();
	private static void allItems(CreativeModeTab.ItemDisplayParameters var0, CreativeModeTab.Output output) {
//		output.accept(BlockRegistry.HANDLEBAR);
//		output.accept(BlockRegistry.PEDALS);
//		output.accept(BlockRegistry.EXTENSIBLE_HOSE);
	}
	public static void init() {
		BlockRegistry.register();
		BlockEntityRegistry.register();
	}

	@NotNull
	public static ResourceLocation asResource(@NotNull String path) {
		return new ResourceLocation("com/guyapooye/clockworkadditions", path);
	}
	public static Component asTranslatable(String translatable) {
		return Component.translatable(translatable);
	}

//	public static class ClockworkAdditionsClient {
//		public static void init() {
//			ModelRegistry.register();
//		}
//	}
}

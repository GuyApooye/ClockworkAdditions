package com.github.guyapooye.clockworkadditions;

import com.github.guyapooye.clockworkadditions.registries.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.ClockworkBlocks;
import org.valkyrienskies.clockwork.ClockworkItems;
import org.valkyrienskies.clockwork.ClockworkMod;

public class ClockworkAdditions
{

	public static final String MOD_ID = "clockworkadditions";

	public static CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static CreativeModeTab getCreativeModeTab() {
		return CWACreativeModeTab;
	}
	public static CreativeModeTab CWACreativeModeTab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.clockworkadditions")).icon(BlockRegistry.KINETIC_FLAP_BEARING::asStack).displayItems(ClockworkAdditions::allItems).build();;
	private static void allItems(CreativeModeTab.ItemDisplayParameters var0, CreativeModeTab.Output output) {
		output.accept(BlockRegistry.HANDLEBAR);
		output.accept(BlockRegistry.PEDALS);
		output.accept(BlockRegistry.KINETIC_FLAP_BEARING);
		output.accept(BlockRegistry.COPYCAT_WING);
		output.accept(BlockRegistry.COPYCAT_FLAP);
	}
	public static void init() {
		PartialModelRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		PacketRegistry.register();

	}

	@NotNull
	public static ResourceLocation asResource(@NotNull String path) {
		return new ResourceLocation("clockworkadditions", path);
	}
	public static Component asTranslatable(String translatable) {
		return Component.translatable(translatable);
	}
}

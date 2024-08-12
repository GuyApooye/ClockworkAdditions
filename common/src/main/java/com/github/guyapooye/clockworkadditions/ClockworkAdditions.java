package com.github.guyapooye.clockworkadditions;

import com.github.guyapooye.clockworkadditions.registries.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

public class ClockworkAdditions
{

	public static final String MOD_ID = "clockworkadditions";

	public static CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static CreativeModeTab CWACreativeModeTab;

	public static void init() {
		PartialModelRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		PacketRegistry.register();
	}

	public static ResourceLocation asResource(@NotNull String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	public static TranslatableComponent asTranslatable(@NotNull String path) {
		return new TranslatableComponent(MOD_ID +"."+path);
	}
}

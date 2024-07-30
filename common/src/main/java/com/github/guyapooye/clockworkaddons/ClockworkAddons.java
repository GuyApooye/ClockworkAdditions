package com.github.guyapooye.clockworkaddons;

import com.github.guyapooye.clockworkaddons.registries.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

public class ClockworkAddons
{

	public static final String MOD_ID = "clockworkaddons";

	public static CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static CreativeModeTab CWACreativeModeTab;

	public static void init() {
		PartialModelRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		PacketRegistry.register();
	}

	@NotNull
	public static ResourceLocation asResource(@NotNull String path) {
		return new ResourceLocation("clockworkaddons", path);
	}
}

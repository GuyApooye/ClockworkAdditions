package com.github.guyapooye.clockworkadditions;

import com.github.guyapooye.clockworkadditions.registries.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
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

	@NotNull
	public static ResourceLocation asResource(@NotNull String path) {
		return new ResourceLocation("clockworkadditions", path);
	}
}

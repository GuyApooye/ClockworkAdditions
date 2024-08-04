package com.github.guyapooye.clockworkadditions.forge.registries.events;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarClientHandler;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jozufozu.flywheel.backend.Backend.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventRegistry {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        if (event.phase == TickEvent.Phase.START) {
            HandlebarClientHandler.tick();
            return;
        }

    }
}

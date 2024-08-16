package com.github.guyapooye.clockworkadditions.registries.events;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarClientHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class ClientEventRegistry {
    public static void onTickStart(Minecraft client) {
        HandlebarClientHandler.tick();
    }
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientEventRegistry::onTickStart);
    }
}

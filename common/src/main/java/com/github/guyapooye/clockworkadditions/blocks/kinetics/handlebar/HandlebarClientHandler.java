package com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar;

import com.github.guyapooye.clockworkadditions.packets.handlebar.HandlebarDrivingPacket;
import com.github.guyapooye.clockworkadditions.packets.handlebar.HandlebarStopDrivingPacket;
import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.*;
import com.simibubi.create.foundation.utility.ControlsUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.valkyrienskies.clockwork.platform.SharedValues;

import java.util.*;

public class HandlebarClientHandler {
    public static Mode MODE = Mode.IDLE;
    public static int PACKET_RATE = 5;
    public static Collection<Integer> currentlyPressed = new HashSet<>();
    private static BlockPos handlebarPos;
    private static int packetCooldown;


    public static void toggle() {
        if (MODE == Mode.IDLE) {
            MODE = Mode.ACTIVE;
            handlebarPos = null;
        } else {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static void activate(BlockPos handlebarAt) {
        if (MODE == Mode.IDLE) {
            MODE = Mode.ACTIVE;
            handlebarPos = handlebarAt;
        }
    }

    public static void deactivate() {
        if (MODE == Mode.ACTIVE && inBlock()) {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static boolean inBlock() {
        return handlebarPos != null;
    }

    protected static void onReset() {
        ControlsUtil.getControls()
                .forEach(kb -> kb.setDown(ControlsUtil.isActuallyPressed(kb)));
        packetCooldown = 0;

        if (inBlock()) {
            Collection<Integer> noKeys = new HashSet<>();
            SharedValues.getPacketChannel().sendToServer(new HandlebarStopDrivingPacket(handlebarPos));
//            SharedValues.getPacketChannel().sendToServer(new HandlebarDrivingPacket(noKeys, handlebarPos));
        }
        handlebarPos = null;

//        if (!currentlyPressed.isEmpty())
//            SharedValues.getPacketChannel().sendToServer(new HandlebarDrivingPacket(currentlyPressed, lecternPos));
//        currentlyPressed.clear();

    }

    public static void tick() {


        if (MODE == Mode.IDLE)
            return;
        if (packetCooldown > 0)
            packetCooldown--;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemStack heldItem = player.getMainHandItem();

        if (player.isSpectator()) {
            MODE = Mode.IDLE;
            onReset();
            return;
        }


        if (inBlock() && BlockRegistry.HANDLEBAR.get()
                .getBlockEntityOptional(mc.level, handlebarPos)
                .map(be -> !be.isUsedBy(mc.player))
                .orElse(true)) {
            deactivate();
            return;
        }

        if (mc.screen != null) {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        if (InputConstants.isKeyDown(mc.getWindow()
                .getWindow(), GLFW.GLFW_KEY_ESCAPE)) {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        Vector<KeyMapping> controls = ControlsUtil.getControls();
        Collection<Integer> pressedKeys = new HashSet<>();
        for (int i = 0; i < controls.size(); i++) {
            if (ControlsUtil.isActuallyPressed(controls.get(i)))
                pressedKeys.add(i);
        }
        Collection<Integer> newKeys = new HashSet<>(pressedKeys);
        Collection<Integer> releasedKeys = currentlyPressed;
        newKeys.removeAll(releasedKeys);
        releasedKeys.removeAll(pressedKeys);

        if (MODE == Mode.ACTIVE) {
            // Released Keys
            if (!releasedKeys.isEmpty()) {
                SharedValues.getPacketChannel().sendToServer(new HandlebarDrivingPacket(releasedKeys, handlebarPos));
//                AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .5f, true);
            }

            // Newly Pressed Keys
            if (!newKeys.isEmpty()) {
                SharedValues.getPacketChannel().sendToServer(new HandlebarDrivingPacket(newKeys, handlebarPos));
                packetCooldown = PACKET_RATE;
//                AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .75f, true);
            }

            // Keepalive Pressed Keys
            if (packetCooldown == 0) {
                if (!pressedKeys.isEmpty()) {
                    SharedValues.getPacketChannel().sendToServer(new HandlebarDrivingPacket(pressedKeys, handlebarPos));
                    packetCooldown = PACKET_RATE;
                }
            }
        }

        currentlyPressed = pressedKeys;
        controls.forEach(kb -> kb.setDown(false));
    }

    public enum Mode {
        IDLE, ACTIVE
    }
}

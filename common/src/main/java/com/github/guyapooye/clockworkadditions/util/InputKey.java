package com.github.guyapooye.clockworkadditions.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public enum InputKey {
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT,
    SPRINT;
    public static int asInt(@NotNull Set<InputKey> keys) {
        int i = keys.stream().map(InputKey::asInt$lambda$0).reduce(0, InputKey::asInt$lambda$1);
        return i;
    }

    @NotNull
    public static Set<InputKey> fromInt(final int keys) {
        InputKey[] var3 = InputKey.values();
        return Set.of(var3.clone()).stream().filter(p -> fromInt$lambda$2(p,keys)).collect(Collectors.toSet());
    }

    private static int asInt$lambda$0(InputKey p0) {
        return switch (p0) {
            case FORWARD -> 0;
            case BACKWARD -> 1;
            case RIGHT -> 2;
            case LEFT -> 3;
            case SPRINT -> 4;
        };
    }

    private static Integer asInt$lambda$1(int a, int b) {
        return a | 1 << b;
    }

    private static boolean fromInt$lambda$2(InputKey p0, int keys) {
        return (keys >> asInt$lambda$0(p0)) % 2 == 1;
    }
    public static Set<InputKey> checkFor(InputKey[] input) {
        Minecraft mc = Minecraft.getInstance();
        Options options = mc.options;
        List<InputKey> inputKeys = Arrays.stream(input).toList();
        Set<InputKey> keys = new HashSet<>();
        if (options.keyUp.isDown() & inputKeys.contains(InputKey.FORWARD)) {
            keys.add(InputKey.FORWARD);
        }

        if (options.keyDown.isDown() & inputKeys.contains(InputKey.BACKWARD)) {
            keys.add(InputKey.BACKWARD);
        }

        if (options.keyLeft.isDown() & inputKeys.contains(InputKey.LEFT)) {
            keys.add(InputKey.LEFT);
        }

        if (options.keyRight.isDown() & inputKeys.contains(InputKey.RIGHT)) {
            keys.add(InputKey.RIGHT);
        }

        if (options.keySprint.isDown() & inputKeys.contains(InputKey.SPRINT)) {
            keys.add(InputKey.SPRINT);
        }

        return keys;
    }
}

package com.github.guyapooye.clockworkaddons.util;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.jvm.internal.impl.metadata.jvm.deserialization.BitEncoding;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.content.kinetics.sequenced_seat.InputKey;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

public enum PedalsInputKey {

    FORWARD,
    BACKWARD,
    SPRINT;

    public static int asInt(@NotNull Set<PedalsInputKey> keys) {
        int i = keys.stream().map(PedalsInputKey::asInt$lambda$0).reduce(0, PedalsInputKey::asInt$lambda$1);
        System.out.println(i);
        return i;
    }

    @NotNull
    public static Set<PedalsInputKey> fromInt(final int keys) {
        PedalsInputKey[] var3 = PedalsInputKey.values();
        return Set.of(var3.clone()).stream().filter(p -> fromInt$lambda$2(p,keys)).collect(Collectors.toSet());
    }

    private static int asInt$lambda$0(PedalsInputKey p0) {
        return switch (p0) {
            case FORWARD -> 0;
            case BACKWARD -> 1;
            case SPRINT -> 2;
        };
    }
    private static int asInt$lambda$4(PedalsInputKey p0) {
        return switch (p0) {
            case FORWARD -> 1;
            case BACKWARD -> 2;
            case SPRINT -> 4;
        };
    }

    private static Integer asInt$lambda$1(int a, int b) {
        return a | 1 << b;
    }

    private static boolean fromInt$lambda$2(PedalsInputKey p0, int keys) {
        return (keys >> asInt$lambda$0(p0)) % 2 == 1;
    }

}

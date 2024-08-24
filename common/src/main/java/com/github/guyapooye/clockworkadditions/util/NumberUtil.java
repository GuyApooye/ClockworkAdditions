package com.github.guyapooye.clockworkadditions.util;

import org.joml.Vector3d;

public class NumberUtil {
    public static double isNaN(double d) {
        return Double.isNaN(d) ? 0 : d;
    }
    public static final Vector3d blockPosOffset = new Vector3d(.5);
    public static boolean isBasicallyZeroD(double d, int inverseMarginOfError) {
        return Math.abs(d) > (10^-inverseMarginOfError);
    }
    public static boolean isBasicallyZeroF(float d, int inverseMarginOfError) {
        return Math.abs(d) > (10^-inverseMarginOfError);
    }
}

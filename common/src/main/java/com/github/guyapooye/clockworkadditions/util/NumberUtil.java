package com.github.guyapooye.clockworkadditions.util;

import org.joml.Vector3d;

public class NumberUtil {
    public static double isNaN(double d) {
        return Double.isNaN(d) ? 0 : d;
    }
    public static final Vector3d blockPosOffset = new Vector3d(.5);
}

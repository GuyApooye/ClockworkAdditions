package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWAHandlebar extends ConfigBase {
    public ConfigFloat handleMaxAngle = f(50, 0, 180f, "handleMaxAngle", Comments.handleMax);
    public ConfigInt handleSpeed = i(16, 0, 256, "handleSpeed", Comments.handleSpeed);
    @Override
    public String getName() {
        return "handlebar";
    }
    private static class Comments {
        static String handleMax = "Maximum angle for the handlebar (might break idk)";
        static String handleSpeed = "Speed for the handlebar (might break idk)";
    }
}

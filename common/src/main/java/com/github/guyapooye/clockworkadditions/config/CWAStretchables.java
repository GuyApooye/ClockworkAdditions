package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWAStretchables extends ConfigBase {
    public final ConfigFloat cvJointMaxLength = f(3,0,10,"constantVelocityJointMaxLength", Comments.cvJoint);
    public final ConfigFloat hoseMaxLength = f(10,0,25,"hoseMaxLength", Comments.hose);
    @Override
    public String getName() {
        return "stretchables";
    }
    private static class Comments {
        static String cvJoint = "Max stretching length for the Constant Velocity Joint";
        static String hose = "Max stretching length for the Constant Velocity Joint";
    }
}

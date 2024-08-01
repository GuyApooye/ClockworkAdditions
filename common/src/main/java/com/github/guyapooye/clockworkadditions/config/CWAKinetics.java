package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWAKinetics extends ConfigBase {
    public final CWAStress stressValues = nested(1, CWAStress::new, Comments.stress);
    @Override
    public String getName() {
        return "kinetics";
    }
    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
    }
}

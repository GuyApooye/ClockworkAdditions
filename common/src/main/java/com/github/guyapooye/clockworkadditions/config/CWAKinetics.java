package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWAKinetics extends ConfigBase {

    public final CWAStress stressValues = nested(1, CWAStress::new, Comments.stress);
    public final CWAHandlebar handlebar = nested(0, CWAHandlebar::new, Comments.handlebar);
    public final ConfigFloat pedalsSeatHightOffset = f(0.5f,-0.5f,1,"pedalsSeatHightOffset",Comments.pedals);
    @Override
    public String getName() {
        return "kinetics";
    }
    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
        static String handlebar = "Settings for the handlebar";
        static String pedals = "Offset the seat hight for the mechanical pedals";
    }
}

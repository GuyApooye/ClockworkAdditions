package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWAServer extends CWAConfigBase {
    public final CWAKinetics kinetics = nested(0, CWAKinetics::new,Comments.kinetics);
    public final CWAClockwork clockwork = nested(0, CWAClockwork::new, Comments.clockwork);
    public final CWARedstone redstone = nested(0, CWARedstone::new, Comments.redstone);
    public String getName() {
        return "server";
    }
    private static class Comments {
        static String kinetics = "Parameters and abilities of Clockwork Addons' kinetic mechanisms";
        static String clockwork = "Changes and tweaks to Clockwork's blocks and items";
        static String redstone = "Parameters that change the way Clockwork Addons' redstone components work";
    }
}

package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWAClockwork extends ConfigBase {
    public final ConfigFloat encasedFanForceMultiplier = f(1,0, "encasedFanForceMultiplier");
    public String getName() {
        return "clockwork";
    }
}

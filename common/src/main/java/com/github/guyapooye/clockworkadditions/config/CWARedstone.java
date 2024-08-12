package com.github.guyapooye.clockworkadditions.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWARedstone extends ConfigBase {
    public final CWAGyro gyro = nested(1, CWAGyro::new, Comments.gyro);

    @Override
    public String getName() {
        return "redstone";
    }
    private class Comments {
        static String gyro = "Changes to how sensitive the Redstone Gyro is to some forces";
    }

}

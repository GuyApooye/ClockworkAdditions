package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.jozufozu.flywheel.core.PartialModel;

public class PartialModelRegistry {
    public static PartialModel BEARING_TOP = new PartialModel(ClockworkAdditions.asResource("block/kinetic_flap_bearing/top"));
    public static PartialModel PEDALS_BASE = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/block"));
    public static PartialModel PEDALS_CRANK = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/crank"));
    public static PartialModel PEDAL_LEFT = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/pedal_left"));
    public static PartialModel PEDAL_RIGHT = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/pedal_right"));

    public static void register() {}
}

package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.jozufozu.flywheel.core.PartialModel;

public class PartialModelRegistry {
    public static PartialModel BEARING_TOP = new PartialModel(ClockworkAdditions.asResource("block/kinetic_flap_bearing/top")),
                               PEDALS_BASE = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/block")),
                               PEDALS_CRANK = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/crank")),
                               PEDAL_LEFT = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/pedal_left")),
                               PEDAL_RIGHT = new PartialModel(ClockworkAdditions.asResource("block/mechanical_pedals/pedal_right")),
                               HANDLEBAR = new PartialModel(ClockworkAdditions.asResource("block/handlebar/handle")),
                               CV_JOINT_BASE = new PartialModel(ClockworkAdditions.asResource("block/cv_joint/base")),
                               CV_JOINT_CONNECTOR = new PartialModel(ClockworkAdditions.asResource("block/cv_joint/connector")),
                               CV_JOINT_ROD = new PartialModel(ClockworkAdditions.asResource("block/cv_joint/rod")),
                               INVERTED_RESISTOR_INDICATOR = new PartialModel(ClockworkAdditions.asResource("block/inverted_redstone_resistor/inverted_redstone_resistor")),
                               EXTENSIBLE_HOSE_CONNECTOR = new PartialModel(ClockworkAdditions.asResource("block/extensible_hose/connector")),
                               EXTENSIBLE_HOSE_HOSE = new PartialModel(ClockworkAdditions.asResource("block/extensible_hose/hose"))
    ;

    public static void register() {}
}

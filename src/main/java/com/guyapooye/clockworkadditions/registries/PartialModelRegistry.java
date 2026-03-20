package com.guyapooye.clockworkadditions.registries;

import com.guyapooye.clockworkadditions.ClockworkAdditions;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class PartialModelRegistry {
    public static final PartialModel
            BEARING_TOP = PartialModel.of(ClockworkAdditions.asResource("block/kinetic_flap_bearing/top")),
            PEDALS_BASE = PartialModel.of(ClockworkAdditions.asResource("block/mechanical_pedals/block")),
            PEDALS_CRANK = PartialModel.of(ClockworkAdditions.asResource("block/mechanical_pedals/crank")),
            RIGHT_PEDAL = PartialModel.of(ClockworkAdditions.asResource("block/mechanical_pedals/right_pedal")),
            LEFT_PEDAL = PartialModel.of(ClockworkAdditions.asResource("block/mechanical_pedals/left_pedal")),
            HANDLEBAR = PartialModel.of(ClockworkAdditions.asResource("block/handlebar/handle")),
            INVERTED_RESISTOR_INDICATOR = PartialModel.of(ClockworkAdditions.asResource("block/inverted_redstone_resistor/inverted_redstone_resistor")),
            EXTENSIBLE_HOSE_CONNECTOR = PartialModel.of(ClockworkAdditions.asResource("block/extensible_hose/connector")),
            EXTENSIBLE_HOSE_HOSE = PartialModel.of(ClockworkAdditions.asResource("block/extensible_hose/hose"))
    ;

    public static void register() {}
}

package com.github.guyapooye.clockworkadditions.blocks.phys.helicopter;

import com.github.guyapooye.clockworkadditions.blocks.phys.AbstractPhysController;
import org.valkyrienskies.core.api.ships.ServerShip;
import com.github.guyapooye.clockworkadditions.blocks.phys.helicopter.HelicopterBearingData.AlternatorBearingUpdateData;


public class HelicopterBearingController extends AbstractPhysController<HelicopterBearingData,AlternatorBearingUpdateData> {

    public static HelicopterBearingController getOrCreate(ServerShip ship) {
        HelicopterBearingController attachment = ship.getAttachment(HelicopterBearingController.class);
        if (attachment == null) {
            attachment = new HelicopterBearingController();
            ship.saveAttachment(HelicopterBearingController.class,attachment);
        }
        return attachment;
    }

}

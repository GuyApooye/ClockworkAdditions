package com.github.guyapooye.clockworkadditions.blocks.phys.alternator;

import com.github.guyapooye.clockworkadditions.blocks.phys.AbstractPhysController;
import org.valkyrienskies.core.api.ships.ServerShip;
import com.github.guyapooye.clockworkadditions.blocks.phys.alternator.AlternatorBearingData.AlternatorBearingUpdateData;


public class AlternatorBearingController extends AbstractPhysController<AlternatorBearingData,AlternatorBearingUpdateData> {

    public static AlternatorBearingController getOrCreate(ServerShip ship) {
        AlternatorBearingController attachment = ship.getAttachment(AlternatorBearingController.class);
        if (attachment == null) {
            attachment = new AlternatorBearingController();
            ship.saveAttachment(AlternatorBearingController.class,attachment);
        }
        return attachment;
    }

}

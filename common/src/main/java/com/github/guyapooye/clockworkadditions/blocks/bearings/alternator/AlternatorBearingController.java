package com.github.guyapooye.clockworkadditions.blocks.bearings.alternator;

import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.api.ships.ShipForcesInducer;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

import static java.lang.Math.sqrt;

@SuppressWarnings("deprecation")
public class AlternatorBearingController implements ShipForcesInducer {
    private final long otherShipId;

    public AlternatorBearingController(long otherShipId) {
        this.otherShipId = otherShipId;
    }

    @Override
    public void applyForces(@NotNull PhysShip physShip) {}
    public void applyForcesAndLookupPhysShips(@NotNull PhysShip physShip, @NotNull Function1<? super Long, ? extends PhysShip> lookupPhysShip) {
        physShip.applyRotDependentTorque(((PhysShipImpl)physShip).getPoseVel().getOmega().div(-sqrt(((PhysShipImpl) physShip).getInertia().getShipMass()),  new Vector3d()));
    }
    public static AlternatorBearingController getOrCreate(ServerShip ship, Ship otherShip) {
        AlternatorBearingController attachment = ship.getAttachment(AlternatorBearingController.class);
        if (attachment == null) {
            attachment = new AlternatorBearingController(otherShip.getId());
            ship.saveAttachment(AlternatorBearingController.class,attachment);
        }
        return attachment;
    }
}

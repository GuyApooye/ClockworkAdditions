package com.github.guyapooye.clockworkaddons.blocks.bearings.heli;

import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ShipForcesInducer;

@SuppressWarnings("deprecation")
public class PhysicsBearingController implements ShipForcesInducer {
    @Override
    public void applyForces(@NotNull PhysShip physShip) {}
    public void applyForcesAndLookupPhysShips(@NotNull PhysShip physShip, @NotNull Function1<? super Long, ? extends PhysShip> lookupPhysShip) {
    }
}

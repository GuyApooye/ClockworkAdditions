package com.github.guyapooye.clockworkadditions.blocks.phys;

import kotlin.jvm.functions.Function1;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ShipForcesInducer;

@Getter
@SuppressWarnings("deprecation")
public abstract class AbstractPhysController<D extends PhysData<U>,U> implements ShipForcesInducer {
    private D blockData;
    @Override
    public void applyForces(@NotNull PhysShip physShip) {}
    @Override
    public void applyForcesAndLookupPhysShips(@NotNull PhysShip physShip, @NotNull Function1<? super Long, ? extends PhysShip> lookupPhysShip) {
    }
    public void addBlock(@NotNull D data) {
        blockData = data;
    }

    public void removeBlock() {
        blockData = null;
    }

    public void updateBlock(@NotNull U data) {
        if (blockData == null ) return;
        blockData.updateWith(data);
    }
}

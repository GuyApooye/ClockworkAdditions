package com.github.guyapooye.clockworkadditions.fabric.registries;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsEntity;
import com.github.guyapooye.clockworkadditions.registries.EntityRegistry;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class EntityRegistryImpl extends EntityRegistry {
    static {
         EntityRegistry.PEDALS = register("pedals", PedalsEntity::new, () -> PedalsEntity.Render::new,
                MobCategory.MISC, 5, Integer.MAX_VALUE, false, true,
                 b -> ((FabricEntityTypeBuilder<?>)b).dimensions(EntityDimensions.fixed(0.25f, 0.35f))
                 ).register();
    }

    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory,
                                                                         NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                         MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
                                                                         NonNullConsumer propertyBuilder) {
        String id = Lang.asId(name);
        return (CreateEntityBuilder<T, ?>) REGISTRATE
                .entity(id, factory, group)
                .properties(b -> b.trackRangeChunks(range)
                        .trackedUpdateRate(updateFrequency)
                        .forceTrackedVelocityUpdates(sendVelocity))
                .properties((NonNullConsumer<FabricEntityTypeBuilder<T>>) propertyBuilder)
                .properties(b -> {
                    if (immuneToFire)
                        b.fireImmune();
                })
                .renderer(renderer);
    }
    public static void register() {
    }
//    static {
//        System.out.println(PEDALS.get().toString());
//        System.out.println(PEDALS.get());
//        System.out.println(PEDALS.get());
//        System.out.println(PEDALS.get());
//        System.out.println(PEDALS.getId());
//    }
}

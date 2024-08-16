package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityRegistry {
    public static final EntityEntry<PedalsEntity> PEDALS = register("pedals", PedalsEntity::new, () -> PedalsEntity.Render::new,
            MobCategory.MISC, 5, Integer.MAX_VALUE, false, true,
            b -> sized(b,0.25f, 0.35f)
    ).register();;

    @ExpectPlatform
    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory,
                                                                         NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                         MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
                                                                         NonNullConsumer propertyBuilder) {
        throw new AssertionError();
    }
    @ExpectPlatform
    private static void sized(Object obj, float width, float height) {
        throw new AssertionError();
    }
    public static void register() {

    }
}

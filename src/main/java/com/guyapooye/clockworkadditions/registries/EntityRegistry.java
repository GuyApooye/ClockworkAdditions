package com.guyapooye.clockworkadditions.registries;

import com.guyapooye.clockworkadditions.ClockworkAdditions;
import com.guyapooye.clockworkadditions.entities.pedals.PedalsEntity;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ClockworkAdditions.MOD_ID);

    public static final RegistryObject<EntityType<PedalsEntity>> PEDALS =
            ENTITIES.register("pedals",
                    () -> EntityType.Builder.<PedalsEntity>of(PedalsEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .setUpdateInterval(1)
                            .setTrackingRange(256)
                            .build("pedals"));

}

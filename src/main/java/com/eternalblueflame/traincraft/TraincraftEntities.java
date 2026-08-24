package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.entity.EntityLocoSteam4_4_0;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.core.Registry;

public final class TraincraftEntities {
    public static final EntityType<EntityLocoSteam4_4_0> LOCO_STEAM_4_4_0 = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "loco_steam_4_4_0"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityLocoSteam4_4_0::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    private TraincraftEntities() {
    }

    public static void register() {
        Traincraft.LOGGER.info("Registering Traincraft entities");
    }
}

package com.slimeeystudios.orbitalrod.config;

import com.mojang.serialization.MapCodec;
import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.RegistryOps;

public class ModResourceConditions {
    public record CraftingCondition() implements ResourceCondition {
        public static final MapCodec<CraftingCondition> CODEC = MapCodec.unit(new CraftingCondition());

        @Override
        public ResourceConditionType<?> getType() {
            return ModResourceConditions.CRAFTING_TYPE;
        }

        @Override
        public boolean test(RegistryOps.@org.jspecify.annotations.Nullable RegistryInfoLookup registryLookup) {
            return ModConfig.INSTANCE.craftingEnabled;
        }
    }

    public static final ResourceConditionType<CraftingCondition> CRAFTING_TYPE = ResourceConditionType.create(OrbitalRodMod.id("crafting_enabled"), CraftingCondition.CODEC);

    public static void register() {
        ResourceConditions.register(CRAFTING_TYPE);
    }
}

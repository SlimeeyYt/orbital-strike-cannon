package com.slimeeystudios.orbitalrod.config;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;

public class ModResourceConditions {
    public static class CraftingEnabledCondition implements ICondition {
        public static final CraftingEnabledCondition INSTANCE = new CraftingEnabledCondition();
        public static final MapCodec<CraftingEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

        @Override
        public boolean test(IContext context) {
            return ModConfig.INSTANCE.craftingEnabled;
        }

        @Override
        public MapCodec<? extends ICondition> codec() {
            return CODEC;
        }
    }
}

package com.slimeeystudios.orbitalrod.mixin;

import com.slimeeystudios.orbitalrod.config.ModConfig;
import com.slimeeystudios.orbitalrod.item.ModItems;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingResultInventory.class)
public class CraftingResultInventoryMixin {
    @Inject(method = "shouldCraftRecipe", at = @At("HEAD"), cancellable = true)
    private void orbitalrod$blockOrbitalRecipeWhenDisabled(World world, ServerPlayerEntity player, Recipe<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.isCraftingRecipeEnabled() && recipe.getOutput(world.getRegistryManager()).isOf(ModItems.ORBITAL_TNT_ROD)) {
            cir.setReturnValue(false);
        }
    }
}

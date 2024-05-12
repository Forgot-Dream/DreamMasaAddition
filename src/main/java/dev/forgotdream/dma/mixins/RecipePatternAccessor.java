package dev.forgotdream.dma.mixins;

import fi.dy.masa.itemscroller.recipes.RecipePattern;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RecipePattern.class,remap = false)
public interface RecipePatternAccessor {
    @Accessor("result")
    void setResult(ItemStack itemStack);

    @Accessor("recipe")
    void setRecipe(ItemStack[] recipe);
}

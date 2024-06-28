package dev.forgotdream.dma.mixins.features.quickCraftWithRecipeBook;

import dev.forgotdream.dma.Reference;
import fi.dy.masa.itemscroller.recipes.RecipePattern;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Dependencies(and = @Dependency(Reference.ITEMSCROLLER_MOD_ID))
@Mixin(value = RecipePattern.class,remap = false)
public interface RecipePatternAccessor {
    @Accessor("result")
    void setResult(ItemStack itemStack);

    @Accessor("recipe")
    void setRecipe(ItemStack[] recipe);
}

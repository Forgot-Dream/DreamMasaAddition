package dev.forgotdream.dma.mixins;

import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.config.Configs;
import fi.dy.masa.itemscroller.recipes.RecipePattern;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

import java.util.Objects;

//#if MC>=12004
import net.minecraft.world.item.crafting.RecipeHolder;
//#endif

@Dependencies(and = @Dependency(Reference.ITEMSCROLLER_MOD_ID))
@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentMixin {
    @Shadow
    protected Minecraft minecraft;

    @Shadow
    @Final
    private StackedContents stackedContents;

    @Shadow
    protected abstract void updateStackedContents();

//#if MC<12004
//$$ @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handlePlaceRecipe(ILnet/minecraft/world/item/crafting/Recipe;Z)V"))
//$$    public void handlePlaceRecipe(MultiPlayerGameMode instance, int i, Recipe<?> recipes, boolean bl) {
//$$        if (bl && Configs.quickCraftWithRecipeBook) {
//$$            if (minecraft.level != null) {
//$$                recipe(recipes);
//$$            }
//#else
    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handlePlaceRecipe(ILnet/minecraft/world/item/crafting/RecipeHolder;Z)V"))
    public void handlePlaceRecipe(MultiPlayerGameMode instance, int i, RecipeHolder recipes, boolean bl) {
        if (bl && Configs.quickCraftWithRecipeBook) {
            if (minecraft.level != null) {
                recipe(recipes.value());
            }
//#endif
        } else {
            Objects.requireNonNull(this.minecraft.gameMode).handlePlaceRecipe(i, recipes,bl);
        }
    }


    @Unique
    private void recipe(Recipe<?> recipe) {
        IntList intList = new IntArrayList();
        while (this.stackedContents.canCraft(recipe, intList)) {
            var recipeArr = intList.stream().map(StackedContents::fromStackingIndex).toList();
            ItemStack[] recipeArray;
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                var recipeIter = recipeArr.iterator();
                recipeArray = new ItemStack[9];
                for (int j = 0; j < 9; j++) {
                    if (j % 3 < shapedRecipe.getWidth() && j / 3 < shapedRecipe.getHeight()) {
                        recipeArray[j] = recipeIter.next();
                    } else {
                        recipeArray[j] = ItemStack.EMPTY;
                    }
                }
            } else {
                recipeArray = recipeArr.toArray(new ItemStack[0]);
            }


            RecipePattern recipePattern = new RecipePattern();
            ((RecipePatternAccessor) recipePattern).setRecipe(recipeArray);
            ((RecipePatternAccessor) recipePattern).setResult(recipe.getResultItem(minecraft.level.registryAccess()));

            if (GuiUtils.getCurrentScreen() != null) {
                InventoryUtils.craftEverythingPossibleWithCurrentRecipe(recipePattern, (AbstractContainerScreen<? extends AbstractContainerMenu>) GuiUtils.getCurrentScreen());
            }

            this.updateStackedContents();
        }
    }

}

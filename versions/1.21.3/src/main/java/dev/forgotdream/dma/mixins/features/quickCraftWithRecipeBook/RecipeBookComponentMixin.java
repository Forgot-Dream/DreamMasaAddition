package dev.forgotdream.dma.mixins.features.quickCraftWithRecipeBook;

import com.llamalad7.mixinextras.sugar.Local;
import dev.forgotdream.dma.Reference;
import fi.dy.masa.itemscroller.recipes.RecipePattern;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

import java.util.List;


@Dependencies(require = @Dependency(Reference.ITEMSCROLLER_MOD_ID))
@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentMixin {
    @Shadow
    @Final
    private StackedItemContents stackedContents;

    @Shadow
    protected abstract void updateStackedContents();

    @Inject(
            method = "mouseClicked",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;tryPlaceRecipe(Lnet/minecraft/client/gui/screens/recipebook/RecipeCollection;Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;)Z")
    )
    public void dma$onShiftClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> cir, @Local RecipeCollection recipeCollection) {
        if (!Screen.hasShiftDown())
            return;

        for (RecipeDisplayEntry recipe : recipeCollection.getRecipes()) {
            if (!recipe.canCraft(this.stackedContents))
                continue;

            RecipePattern pattern = new RecipePattern();
            ItemStack[] recipeArray;

            if (recipe.display() instanceof ShapedCraftingRecipeDisplay shapedCraftingRecipeDisplay) {
                int recipeSize;
                if (shapedCraftingRecipeDisplay.width() > 2
                        || shapedCraftingRecipeDisplay.height() > 2
                        || GuiUtils.getCurrentScreen() instanceof CraftingScreen)
                    recipeSize = 3;
                else
                    recipeSize = 2;

                pattern.ensureRecipeSizeAndClearRecipe(recipeSize * recipeSize);
                recipeArray = new ItemStack[recipeSize * recipeSize];

                var recipeIter = shapedCraftingRecipeDisplay.ingredients().iterator();

                for (int j = 0; j < recipeSize * recipeSize; j++) {
                    if (j % recipeSize < shapedCraftingRecipeDisplay.width() && j / recipeSize < shapedCraftingRecipeDisplay.height()) {
                        var item = recipeIter.next();
                        if (item instanceof SlotDisplay.Composite(List<SlotDisplay> contents)) {
                            recipeArray[j] = ((SlotDisplay.ItemSlotDisplay) contents.getFirst()).item().value().getDefaultInstance();
                        } else if (item instanceof SlotDisplay.TagSlotDisplay tagSlotDisplay) {
                            ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
                            recipeArray[j] = tagSlotDisplay.resolveForFirstStack(contextMap);
                        } else {
                            recipeArray[j] = ItemStack.EMPTY;
                        }
                    } else {
                        recipeArray[j] = ItemStack.EMPTY;
                    }
                }

                ((RecipePatternAccessor) pattern).setRecipe(recipeArray);
                ((RecipePatternAccessor) pattern).setResult(((SlotDisplay.ItemStackSlotDisplay) shapedCraftingRecipeDisplay.result()).stack());


            } else if (recipe.display() instanceof ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay) {
                recipeArray = new ItemStack[4];
                pattern.ensureRecipeSizeAndClearRecipe(4);

                if (shapelessCraftingRecipeDisplay.ingredients().getFirst() instanceof SlotDisplay.TagSlotDisplay tagSlotDisplay) {
                    ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
                    recipeArray[0] = tagSlotDisplay.resolveForFirstStack(contextMap);
                } else {
                    recipeArray[0] = ItemStack.EMPTY;
                }

                for (int j = 1; j < 4; j++) {
                    recipeArray[j] = ItemStack.EMPTY;
                }

                ((RecipePatternAccessor) pattern).setRecipe(recipeArray);
                ((RecipePatternAccessor) pattern).setResult(((SlotDisplay.ItemStackSlotDisplay) shapelessCraftingRecipeDisplay.result()).stack());
            }


            AbstractContainerScreen<? extends AbstractContainerMenu> gui = (AbstractContainerScreen<? extends AbstractContainerMenu>) GuiUtils.getCurrentScreen();
            pattern.storeNetworkRecipeId(recipe.id());
            pattern.storeRecipeDisplayEntry(recipe);
            InventoryUtils.craftEverythingPossibleWithCurrentRecipe(pattern, gui);

            this.updateStackedContents();
        }

    }

}

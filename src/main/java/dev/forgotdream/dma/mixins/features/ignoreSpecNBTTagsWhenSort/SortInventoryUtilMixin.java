package dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort;

import com.llamalad7.mixinextras.sugar.Local;
import com.plusls.ommc.feature.sortInventory.SortInventoryUtil;
import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.config.Configs;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

import java.util.List;

@Dependencies(and = @Dependency(Reference.OMMC_MOD_ID))
@Mixin(value = SortInventoryUtil.class, remap = false)
public abstract class SortInventoryUtilMixin {
    @Inject(method = "quickSort",
            at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V", shift = At.Shift.AFTER)
    )
    private static void quickSort(List<ItemStack> itemStacks, int startSlot, int endSlot, CallbackInfoReturnable<List<Tuple<Integer, Integer>>> cir, @Local(ordinal = 2) List<ItemStack> sortedlist) {
        if (Configs.ignoreSpecNBTTagsWhenSort.getBooleanValue()) {
            var fixed_list = itemStacks.stream().filter(SortInventoryUtilMixin::isIgnoredItem).toList();
            sortedlist.removeAll(fixed_list);
            for (var item : fixed_list) {
                int index = itemStacks.indexOf(item);
                sortedlist.add(index, item);
            }
        }
    }

    @Unique
    private static boolean isIgnoredItem(ItemStack itemStack) {
        if (Configs.ignoreSpecNBTTagsWhenSort.getBooleanValue()) {
            for (var tag : Configs.ignoreSpecNBTTagsList.getStrings()) {
                if (itemStack.getTag() != null && itemStack.getTag().contains(tag)) {
                    return true;
                }
            }
        }
        return false;
    }
}

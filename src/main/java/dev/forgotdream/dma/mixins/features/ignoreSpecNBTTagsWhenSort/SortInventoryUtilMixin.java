package dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort;

import com.llamalad7.mixinextras.sugar.Local;
import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.config.Configs;
import dev.forgotdream.dma.features.ignoreSpecNBTTagsWhenSort.ignoreNBTSortUtil;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

import java.util.List;

//import com.plusls.ommc.feature.sortInventory.SortInventoryUtil;
@SuppressWarnings({"UnresolvedMixinReference", "UnusedMixin"})
@Dependencies(require = {
        @Dependency(Reference.OMMC_MOD_ID),
        @Dependency(value = Reference.MINECRAFT_ID, versionPredicates = "<1.20.6")
})
@Pseudo
@Mixin(targets = "com.plusls.ommc.feature.sortInventory.SortInventoryUtil")
public abstract class SortInventoryUtilMixin {
    @Inject(method = "quickSort",
            at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V", shift = At.Shift.AFTER),
            remap = false
    )
    private static void quickSort(List<ItemStack> originItemList, int startSlot, int endSlot, CallbackInfoReturnable<List<Tuple<Integer, Integer>>> cir, @Local(ordinal = 2) List<ItemStack> sortedlist) {
        if (Configs.ignoreSpecNBTTagsWhenSort.getBooleanValue()) {
            // 需要固定的物品
            var fixed_list = originItemList.subList(startSlot, endSlot).stream().filter(ignoreNBTSortUtil::isIgnoredItem).toList();
            // 从已经排序的列表删去需要固定的物品，即将需要固定的物品从排序队列中剔除
            sortedlist.removeAll(fixed_list);
            // 将固定列表修改回原位置
            fixed_list.forEach((item) -> sortedlist.add(originItemList.indexOf(item), item));
        }
    }
}

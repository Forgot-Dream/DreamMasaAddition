package dev.forgotdream.dma.features.ignoreSpecNBTTagsWhenSort;

import dev.forgotdream.dma.config.Configs;
import dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort.InventoryUtilsAccessor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;

import java.util.List;

//#if MC<12006
//$$ import net.minecraft.nbt.CompoundTag;
//#else
import net.minecraft.core.component.DataComponents;

import static fi.dy.masa.itemscroller.util.InventoryUtils.swapSlots;
//#endif

public class ignoreNBTSortUtil {
    public static boolean isIgnoredItem(ItemStack itemStack) {
        if (Configs.ignoreSpecNBTTagsWhenSort.getBooleanValue()) {
            for (var tag : Configs.ignoreSpecNBTTagsList.getStrings()) {
                //#if MC<12006
                //$$ if (itemStack.getTag() != null && itemStack.getTag().contains(tag)) {
                //#else
                var itemTag = itemStack.get(DataComponents.CUSTOM_DATA);
                if (itemTag != null && !itemTag.isEmpty() && itemTag.contains(tag)) {
                //#endif
                    return true;
                }
            }
        }
        return false;
    }

//#if MC>12006
    public static void quickSort(AbstractContainerScreen<?> gui, int start, int end) {
        var itemList = gui.getMenu().getItems();
        var ignoredList = itemList.subList(start, end).stream().filter(ignoreNBTSortUtil::isIgnoredItem).map(itemList::indexOf).toList();
        _quickSort(gui, start, end, ignoredList);
    }

    private static void _quickSort(AbstractContainerScreen<?> gui, int start, int end, List<Integer> ignoredList) {
        if (start < end) {
            ItemStack mid = gui.getMenu().getSlot(end).getItem();
            int l = start;
            int r = end - 1;

            while (l < r) {
                while (l < r && (InventoryUtilsAccessor.invokeCompareStacks(gui.getMenu().getSlot(l).getItem(), mid) < 0 || ignoredList.contains(l))) {
                    ++l;
                }

                while (l < r && (InventoryUtilsAccessor.invokeCompareStacks(gui.getMenu().getSlot(r).getItem(), mid) >= 0 || ignoredList.contains(r))) {
                    --r;
                }

                if (l != r) {
                    swapSlots(gui, l, r);
                }
            }

            if (InventoryUtilsAccessor.invokeCompareStacks(gui.getMenu().getSlot(l).getItem(), gui.getMenu().getSlot(end).getItem()) >= 0
                    && !(ignoredList.contains(l) || ignoredList.contains(end))) {
                swapSlots(gui, l, end);
            } else {
                ++l;
            }

            _quickSort(gui, start, l - 1, ignoredList);
            _quickSort(gui, l + 1, end, ignoredList);
        }
    }
//#endif
}

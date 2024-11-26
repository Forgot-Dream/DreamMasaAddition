package dev.forgotdream.dma.features.ignoreSpecNBTTagsWhenSort;

import dev.forgotdream.dma.config.Configs;
import dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort.InventoryUtilsAccessor;
import fi.dy.masa.itemscroller.ItemScroller;
import it.unimi.dsi.fastutil.Pair;
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
    private static Pair<Integer, Integer> lastSwapTry=Pair.of(-1, -1);

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
    /**
     * quickSort() modified from fi.dy.masa.itemscroller.util.InventoryUtils#quickSort(AbstractContainerScreen, int, int)
     */
    public static void quickSort(AbstractContainerScreen<?> gui, int start, int end,boolean shulkerBoxFix) {
        var itemList = gui.getMenu().getItems();
        var ignoredList = itemList.subList(start, end).stream().filter(ignoreNBTSortUtil::isIgnoredItem).map(itemList::indexOf).toList();
        lastSwapTry = Pair.of(-1, -1);
        _quickSort(gui, start, end, ignoredList,shulkerBoxFix);
    }

    private static void _quickSort(AbstractContainerScreen<?> gui, int start, int end, List<Integer> ignoredList,boolean shulkerBoxFix) {
        if (start < end) {
            ItemStack mid = gui.getMenu().getSlot(end).getItem();
            int l = start;
            int r = end - 1;

            while (l < r) {
                while (l < r && (InventoryUtilsAccessor.invokeCompareStacks(gui.getMenu().getSlot(l).getItem(), mid,shulkerBoxFix) < 0 || ignoredList.contains(l))) {
                    ++l;
                }

                while (l < r && (InventoryUtilsAccessor.invokeCompareStacks(gui.getMenu().getSlot(r).getItem(), mid,shulkerBoxFix) >= 0 || ignoredList.contains(r))) {
                    --r;
                }

                if (l != r && (Integer)lastSwapTry.left() != l && (Integer)lastSwapTry.right() != r) {
                    swapSlots(gui, l, r);
                    lastSwapTry = Pair.of(l, r);
                } else if (l != r) {
                    ItemScroller.logger.warn("quickSort: Item swap failure. Duplicate pair of [{}, {}], cancelling sort task", l, r);
                    return;
                }
            }

            if (InventoryUtilsAccessor.invokeCompareStacks(gui.getMenu().getSlot(l).getItem(), gui.getMenu().getSlot(end).getItem(),shulkerBoxFix) >= 0
                    && !(ignoredList.contains(l) || ignoredList.contains(end))) {
                swapSlots(gui, l, end);
            } else {
                ++l;
            }

            _quickSort(gui, start, l - 1, ignoredList,shulkerBoxFix);
            _quickSort(gui, l + 1, end, ignoredList,shulkerBoxFix);
        }
    }
//#endif
}

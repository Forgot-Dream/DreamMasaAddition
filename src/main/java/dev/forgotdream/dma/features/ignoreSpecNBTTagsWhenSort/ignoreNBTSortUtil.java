package dev.forgotdream.dma.features.ignoreSpecNBTTagsWhenSort;

import dev.forgotdream.dma.config.Configs;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;

//#if MC<12006
//$$ import net.minecraft.nbt.CompoundTag;
//#else
import net.minecraft.core.component.DataComponents;
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
}

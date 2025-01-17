package dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort;

import com.llamalad7.mixinextras.sugar.Local;
import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.config.Configs;
import dev.forgotdream.dma.features.ignoreSpecNBTTagsWhenSort.ignoreNBTSortUtil;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

import java.util.ArrayList;

@Dependencies(require = {
        @Dependency(Reference.ITEMSCROLLER_MOD_ID),
        @Dependency(value = Reference.MINECRAFT_ID, versionPredicates = ">=1.21")
})
@Pseudo
@Mixin(InventoryUtils.class)
public abstract class InventoryUtilsMixin {
    //#if MC>12006
    @Inject(method = "quickSort", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;size()I"), remap = false)
    private static void afterFilter$dma(AbstractContainerScreen<?> gui, int start, int end, boolean shulkerBoxFix, int swapSlot, CallbackInfo ci, @Local ArrayList<Pair<Integer, ItemStack>> snapshot) {
        if (Configs.ignoreSpecNBTTagsWhenSort.getBooleanValue()) {
            snapshot.removeIf((pair)->ignoreNBTSortUtil.isIgnoredItem(pair.value()));
        }
    }
    //#endif
}

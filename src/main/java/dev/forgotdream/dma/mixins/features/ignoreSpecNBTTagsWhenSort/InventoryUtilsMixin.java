package dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort;

import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.config.Configs;
import dev.forgotdream.dma.features.ignoreSpecNBTTagsWhenSort.ignoreNBTSortUtil;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

@Dependencies(require = {
        @Dependency(Reference.ITEMSCROLLER_MOD_ID),
        @Dependency(value = Reference.MINECRAFT_ID, versionPredicates = ">=1.21")
})
@Pseudo
@Mixin(InventoryUtils.class)
public abstract class InventoryUtilsMixin {
    //#if MC>12006
    @Inject(method = "quickSort", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void redirectQuickSort$dma(AbstractContainerScreen<?> gui, int start, int end, boolean shulkerBoxFix, CallbackInfo ci) {
        if (Configs.ignoreSpecNBTTagsWhenSort.getBooleanValue()) {
            ignoreNBTSortUtil.quickSort(gui, start, end,shulkerBoxFix);
            ci.cancel();
        }
    }
    //#endif
}

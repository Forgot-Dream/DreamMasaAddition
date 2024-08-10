package dev.forgotdream.dma.mixins.features.ignoreSpecNBTTagsWhenSort;

import dev.forgotdream.dma.Reference;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

@SuppressWarnings("unused")
@Dependencies(require = {
        @Dependency(Reference.ITEMSCROLLER_MOD_ID),
        @Dependency(value = Reference.MINECRAFT_ID, versionPredicates = ">=1.21")
})
@Pseudo
@Mixin(InventoryUtils.class)
public interface InventoryUtilsAccessor {
//#if MC>12006
    @Invoker("compareStacks")
    static int invokeCompareStacks(ItemStack stack1, ItemStack stack2) {
        throw new AssertionError();
    }
//#endif
}

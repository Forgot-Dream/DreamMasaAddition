package dev.forgotdream.dma.mixins.fixes.fixTweakerooGammaOverride;

import dev.forgotdream.dma.Reference;
import fi.dy.masa.tweakeroo.config.Configs;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Dependencies(and = @Dependency(Reference.TWEAKEROO_MOD_ID))
@Mixin(value = Configs.class, remap = false)
public class ConfigsMixin {
    @Inject(method = "loadFromFile", at = @At("RETURN"))
    private static void loadFromFile(CallbackInfo ci) {
        if (dev.forgotdream.dma.config.Configs.fixTweakerooGammaOverride) {
            if (FeatureToggle.TWEAK_GAMMA_OVERRIDE.getBooleanValue())
                FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
        }
    }
}

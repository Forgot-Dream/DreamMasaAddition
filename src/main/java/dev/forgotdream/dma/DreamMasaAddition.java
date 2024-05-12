package dev.forgotdream.dma;

import dev.forgotdream.dma.config.Configs;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;

public class DreamMasaAddition implements ModInitializer {
    private static final int CONFIG_VERSION = 1;

    @Dependencies(and={
            @Dependency(value = "itemscroller",versionPredicate = ">=0.16.0", optional = true)
    })
    @Override
    public void onInitialize() {
        ConfigManager cm = ConfigManager.get(Reference.MOD_ID);
        cm.parseConfigClass(Configs.class);
        Reference.configHandler = new ConfigHandler(Reference.MOD_ID, cm, CONFIG_VERSION);
        ConfigHandler.register(Reference.configHandler);
        Reference.configHandler.postDeserializeCallback = Configs::postDeserialize;
        Configs.init(cm);
    }
}
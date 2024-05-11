package dev.forgotdream.dma.gui;

import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.config.Configs;
import lombok.Getter;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.gui.ConfigGui;

public class GuiConfigs extends ConfigGui {
    @Getter(lazy = true)
    private static final GuiConfigs instance = new GuiConfigs(Reference.MOD_ID, Configs.ConfigCategory.FEATURE_TOGGLE, Reference.configHandler.configManager);

    public GuiConfigs(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager, () -> Reference.translate("gui.title.configs", Reference.MOD_VERSION));
    }
}

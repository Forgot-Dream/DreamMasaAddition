package dev.forgotdream.dma.gui;

import dev.forgotdream.dma.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.util.collect.ValueContainer;


public class GuiConfigs extends MagicConfigGui {
    @Nullable
    private static GuiConfigs currentInstance = null;

    public GuiConfigs() {
        super(Reference.MOD_ID, Reference.getConfigManager(), I18n.tr(String.format("%s.config.gui.title", Reference.MOD_ID), Reference.MOD_VERSION));
    }

    @Override
    public void init() {
        super.init();
        GuiConfigs.currentInstance = this;
    }

    @Override
    public void removed() {
        super.removed();
        GuiConfigs.currentInstance = null;
    }

    //    @Override
//    public boolean hideUnAvailableConfigs() {
//        return Configs.hideUnavailableConfigs.getBooleanValue();
//    }

    public static @NotNull ValueContainer<GuiConfigs> getCurrentInstance() {
        return ValueContainer.ofNullable(GuiConfigs.currentInstance);
    }
}

package dev.forgotdream.dma.compat;

import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.gui.GuiConfigs;
import top.hendrixshen.magiclib.compat.modmenu.ModMenuCompatApi;

public class ModMenuApiImpl implements ModMenuCompatApi {
    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return (screen) -> {
            GuiConfigs gui = GuiConfigs.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }

    @Override
    public String getModIdCompat() {
        return Reference.MOD_ID;
    }
}
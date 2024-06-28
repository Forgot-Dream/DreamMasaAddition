package dev.forgotdream.dma.compat;

import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.gui.GuiConfigs;
import top.hendrixshen.magiclib.api.compat.modmenu.ModMenuApiCompat;

public class ModMenuApiImpl implements ModMenuApiCompat {
    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return (screen) -> {
            GuiConfigs configGui = new GuiConfigs();
            configGui.setParent(screen);
            return configGui;
        };
    }

    @Override
    public String getModIdCompat() {
        return Reference.MOD_ID;
    }
}
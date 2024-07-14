package dev.forgotdream.dma;

import dev.forgotdream.dma.config.Configs;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;

public class DreamMasaAddition implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        InitializationHandler.getInstance().registerInitializationHandler(
                ()-> ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID,Reference.getConfigHandler())
        );
        Configs.init();
        Reference.getConfigHandler().setPostDeserializeCallback(Configs::postDeserialize);
    }
}

package dev.forgotdream.dma;

import dev.forgotdream.dma.config.Configs;
import net.fabricmc.api.ClientModInitializer;

public class DreamMasaAddition implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        Configs.init();
        Reference.getConfigHandler().setPostDeserializeCallback(Configs::postDeserialize);
    }
}

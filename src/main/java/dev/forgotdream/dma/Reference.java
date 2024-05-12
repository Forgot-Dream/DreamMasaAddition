package dev.forgotdream.dma;

import fi.dy.masa.malilib.util.StringUtils;
import lombok.Getter;
import net.minecraft.network.chat.MutableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.language.api.I18n;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;

public class Reference {
    public static final String MOD_ID = "dma";
    public static final String MOD_NAME = "Dream Masa Addition";
    public static final String MOD_VERSION = StringUtils.getModVersionString(MOD_ID);

    public static final String ITEMSCROLLER_MOD_ID = "itemscroller";

    @Getter
    private static final Logger logger = LogManager.getLogger(MOD_ID);
    public static ConfigHandler configHandler;

    public static String translate(String key, Object... objects) {
        return I18n.get(Reference.MOD_ID + "." + key, objects);
    }

    public static @NotNull MutableComponent translatable(String key, Object... objects) {
        return ComponentCompatApi.translatable(Reference.MOD_ID + "." + key, objects);
    }
}

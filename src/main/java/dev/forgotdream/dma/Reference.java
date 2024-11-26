package dev.forgotdream.dma;

import fi.dy.masa.malilib.util.StringUtils;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigHandler;

public class Reference {
    public static final String MOD_ID = "dma";
    public static final String MOD_NAME = "Dream Masa Addition";
    public static final String MOD_VERSION = StringUtils.getModVersionString(MOD_ID);

    public static final String ITEMSCROLLER_MOD_ID = "itemscroller";
    public static final String TWEAKEROO_MOD_ID = "tweakeroo";
    public static final String OMMC_MOD_ID = "ommc";
    public static final String MINECRAFT_ID = "minecraft";


    @Getter
    private static final Logger logger = LogManager.getLogger(MOD_ID);

    @Getter
    private static MagicConfigManager configManager = GlobalConfigManager.getConfigManager(MOD_ID);

    @Getter
    public static MagicConfigHandler configHandler = new MagicConfigHandler(Reference.configManager,1);

    public static String translate(String key, Object... objects) {
        return I18n.tr(Reference.MOD_ID + "." + key, objects);
    }

    public static @NotNull MutableComponentCompat translatable(String key, Object... objects) {
        return ComponentCompat.translatableCompat(Reference.MOD_ID + "." + key, objects);
    }
}

package dev.forgotdream.dma.config;

import com.google.common.collect.ImmutableList;
import dev.forgotdream.dma.Reference;
import dev.forgotdream.dma.gui.GuiConfigs;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.annotation.Statistic;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigFactory;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.impl.malilib.config.option.MagicConfigBoolean;
import top.hendrixshen.magiclib.impl.malilib.config.option.MagicConfigHotkey;
import top.hendrixshen.magiclib.impl.malilib.config.option.MagicConfigStringList;

public class Configs {
    private static final MagicConfigManager cm = Reference.getConfigManager();
    private static final MagicConfigFactory cf = Configs.cm.getConfigFactory();

    @Statistic(hotkey = false)
    @Config(category = ConfigCategory.GENERIC)
    public static MagicConfigHotkey openConfigGui = Configs.cf.newConfigHotkey("openConfigGui", "D,M,A");

    @Config(category = ConfigCategory.FEATURE_TOGGLE)
    public static MagicConfigBoolean windowResizable = Configs.cf.newConfigBoolean("windowResizable", true);

    @Dependencies(require = @Dependency(Reference.ITEMSCROLLER_MOD_ID))
    @Config(category = ConfigCategory.FEATURE_TOGGLE)
    public static MagicConfigBoolean quickCraftWithRecipeBook = Configs.cf.newConfigBoolean("quickCraftWithRecipeBook", false);

    @Dependencies(require = {
            @Dependency(value = Reference.OMMC_MOD_ID,optional = true),
            @Dependency(value = Reference.ITEMSCROLLER_MOD_ID,optional = true)
    })
    @Config(category = ConfigCategory.FEATURE_TOGGLE)
    public static MagicConfigBoolean ignoreSpecNBTTagsWhenSort = Configs.cf.newConfigBoolean("ignoreSpecNBTTagsWhenSort", false);

    @Dependencies(require = {
            @Dependency(value = Reference.OMMC_MOD_ID,optional = true),
            @Dependency(value = Reference.ITEMSCROLLER_MOD_ID,optional = true)
    })
    @Config(category = ConfigCategory.LISTS)
    public static MagicConfigStringList ignoreSpecNBTTagsList = Configs.cf.newConfigStringList("ignoreSpecNBTTagsList", ImmutableList.of("GcaClear"));

    @Dependencies(require = @Dependency(Reference.TWEAKEROO_MOD_ID))
    @Config(category = ConfigCategory.FIXES)
    public static MagicConfigBoolean fixTweakerooGammaOverride = Configs.cf.newConfigBoolean("fixTweakerooGammaOverride", true);

    public static void init() {
        Configs.cm.parseConfigClass(Configs.class);

        MagicConfigManager.setHotkeyCallback(openConfigGui, GuiConfigs::openGui,true);

        Configs.windowResizable.setValueChangeCallback(
                newValue -> GLFW.glfwSetWindowAttrib(
                        Minecraft.getInstance().getWindow().getWindow(),
                        GLFW.GLFW_RESIZABLE,
                        newValue.getBooleanValue() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE
                )
        );
    }

    public static void postDeserialize(MagicConfigHandler magicConfigHandler) {
        if (!Configs.windowResizable.getBooleanValue())
            GLFW.glfwSetWindowAttrib(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
    }

    public static class ConfigCategory {
        public static final String GENERIC = "generic";
        public static final String FEATURE_TOGGLE = "feature_toggle";
        public static final String LISTS = "lists";
        public static final String FIXES = "fixes";
    }
}

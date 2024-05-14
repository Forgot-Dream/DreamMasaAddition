package dev.forgotdream.dma.config;

import com.google.common.collect.Lists;
import dev.forgotdream.dma.Reference;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;

import java.util.ArrayList;

public class Configs {
    @Hotkey
    @Config(category = ConfigCategory.FEATURE_TOGGLE)
    public static boolean windowResizable = true;

    @Hotkey
    @Config(category = ConfigCategory.FEATURE_TOGGLE, dependencies = @Dependencies(and = @Dependency(Reference.ITEMSCROLLER_MOD_ID)))
    public static boolean quickCraftWithRecipeBook = false;

    @Hotkey
    @Config(category = ConfigCategory.FEATURE_TOGGLE, dependencies = @Dependencies(and = @Dependency(Reference.OMMC_MOD_ID)))
    public static boolean ignoreSpecNBTTagsWhenSort = false;

    @Config(category = ConfigCategory.LISTS, dependencies = @Dependencies(and = @Dependency(Reference.OMMC_MOD_ID)))
    public static ArrayList<String> ignoreSpecNBTTagsList = Lists.newArrayList("GcaClear");

    @Config(category = ConfigCategory.FIXES, dependencies = @Dependencies(and = @Dependency(Reference.TWEAKEROO_MOD_ID)))
    public static boolean fixTweakerooGammaOverride = true;

    public static void init(@NotNull ConfigManager cm) {
        cm.setValueChangeCallback("windowResizable", configOption -> {
            long window = Minecraft.getInstance().getWindow().getWindow();
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_RESIZABLE, ((ConfigBoolean) configOption.getConfig()).getBooleanValue() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        });
    }

    public static void postDeserialize(@NotNull ConfigHandler configHandler) {
        if (!Configs.windowResizable)
            GLFW.glfwSetWindowAttrib(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
    }


    public static class ConfigCategory {
        //        public static final String GENERIC = "generic";
        public static final String FEATURE_TOGGLE = "feature_toggle";
        public static final String LISTS = "lists";
        public static final String FIXES = "fixes";
//        public static final String ADVANCED_INTEGRATED_SERVER = "advanced_integrated_server";
    }
}

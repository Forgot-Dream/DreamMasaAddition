package dev.forgotdream.dma.mixins;


import dev.forgotdream.dma.DreamMasaAddition;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "handleDebugKeys", at = @At("RETURN"), cancellable = true)
    private void handleDebugKeys(int i, CallbackInfoReturnable<Boolean> cir) {
        if (i == 82) {
            long window = Minecraft.getInstance().getWindow().getWindow();
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_RESIZABLE, !DreamMasaAddition.canResizeWindow ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            DreamMasaAddition.canResizeWindow = !DreamMasaAddition.canResizeWindow;
            this.minecraft.gui.getChat().addMessage(Component.translatable(DreamMasaAddition.canResizeWindow ? "dma.debug.resize_window.on" : "dma.debug.resize_window.off"));
            cir.setReturnValue(true);
        }

    }
}

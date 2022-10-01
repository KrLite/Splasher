package net.krlite.splasher.mixin;

import net.krlite.splasher.config.SplasherModConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.splasher.SplasherMod.LOGGER;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    @Shadow
    @Nullable
    private String splashText;

    private final Text SPLASH = new LiteralText("Splash!");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void injected(CallbackInfo ci) {
        if ( SplasherModConfigs.jeb ) splashText = MinecraftClient.getInstance().getSplashTextLoader().get();

        if ( SplasherModConfigs.RANDOM_RATE.onClick() ) {
            Screen screen = MinecraftClient.getInstance().currentScreen;
            TextRenderer textRenderer = ((ScreenAccessor) screen).getTextRenderer();

            int splashWidth = this.textRenderer.getWidth(SPLASH);

            this.addDrawableChild(new PressableTextWidget(width / 2 - splashWidth / 2, 2, splashWidth, 10, SPLASH, (button -> {
                LOGGER.warn("Clicked!");
                SplasherModConfigs.shouldReloadSplashText = true;
            }), textRenderer));
        }

        if ( SplasherModConfigs.shouldReloadSplashText ) {
            splashText = MinecraftClient.getInstance().getSplashTextLoader().get();

            SplasherModConfigs.shouldReloadSplashText = false;
        }
    }
}
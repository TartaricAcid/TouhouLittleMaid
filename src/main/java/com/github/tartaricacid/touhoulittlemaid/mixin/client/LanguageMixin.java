package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.client.resource.LanguageLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = ClientLanguage.class, priority = 943)
public class LanguageMixin {
    @Inject(method = "getOrDefault(Ljava/lang/String;)Ljava/lang/String;", at = @At(value = "HEAD"), cancellable = true)
    public void getCustomLanguage(String key, CallbackInfoReturnable<String> call) {
        String code = Minecraft.getInstance().getLanguageManager().getSelected().getCode();
        Map<String, String> languages = LanguageLoader.getLanguages(code);
        Map<String, String> alternative = LanguageLoader.getLanguages("en_us");
        if (languages != null && languages.containsKey(key)) {
            call.setReturnValue(languages.get(key));
        } else if (alternative != null && alternative.containsKey(key)) {
            call.setReturnValue(alternative.get(key));
        }
    }

    @Inject(method = "has(Ljava/lang/String;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void hasCustomLanguage(String key, CallbackInfoReturnable<Boolean> call) {
        String code = Minecraft.getInstance().getLanguageManager().getSelected().getCode();
        Map<String, String> languages = LanguageLoader.getLanguages(code);
        Map<String, String> alternative = LanguageLoader.getLanguages("en_us");
        if (languages != null && languages.containsKey(key)) {
            call.setReturnValue(true);
        } else if (alternative != null && alternative.containsKey(key)) {
            call.setReturnValue(true);
        }
    }
}

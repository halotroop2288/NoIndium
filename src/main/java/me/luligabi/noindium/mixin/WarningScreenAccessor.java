package me.luligabi.noindium.mixin;

import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WarningScreen.class)
public interface WarningScreenAccessor {
	@Accessor
	MultiLineLabel getMessage();

	@Accessor
	void setMessage(MultiLineLabel messageText);
}

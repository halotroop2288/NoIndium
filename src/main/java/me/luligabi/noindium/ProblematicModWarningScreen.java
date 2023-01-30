package me.luligabi.noindium;

import me.luligabi.noindium.mixin.WarningScreenAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class ProblematicModWarningScreen extends WarningScreen {
	private final NoIndium.Reason reason;

	public ProblematicModWarningScreen(@NotNull NoIndium.Reason reason, @Nullable Screen previousScreen) {
		super(
				reason.header.withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD),
				reason.message, NoIndium.CHECK_MESSAGE,
				reason.header.copy().append("\n").append(reason.message), previousScreen
		);
		this.reason = reason;
	}

	@Override
	protected void init() {
		((WarningScreenAccessor) this).setMessage(MultiLineLabel.create(font, reason.message, width - 50));
		int yOffset = (((WarningScreenAccessor) this).getMessage().getLineCount() + 1) * font.lineHeight * 2 - 20;
		if (NoIndium.config.allowToProceed) {
			this.stopShowing = new Checkbox(width / 2 - 155 + 80, 76 + yOffset, 150, 20, NoIndium.CHECK_MESSAGE, false);
			this.addRenderableWidget(stopShowing);
		}
		initButtons(yOffset);
	}

	@Override
	protected void initButtons(int yOffset) {
		final int width = 150;
		final int height = 20;
		final int x1 = (this.width / 2) - (width + 5);
		final int y = 100 + yOffset;
		final int x2 = x1 + width + 10;

		Button left = new Button(x1, y, width, height, reason.button1Label, reason.button1Action);
		Button right = new Button(x2, y, width, height, reason.button2Label, reason.button2Action);
		Stream.of(left, right).forEach(this::addRenderableWidget);

		if (NoIndium.config.allowToProceed) {
			this.addRenderableWidget(new Button(this.width / 2 - 75, 130 + yOffset, 150, 20,
					NoIndium.PROCEED, buttonWidget -> {
				if (this.stopShowing != null && this.stopShowing.isActive()) {
					switch (reason) {
						case SODIUM_NO_INDIUM -> NoIndium.config.showIndiumScreen = false;
						case USING_OPTIFABRIC -> NoIndium.config.showOptifabricScreen = false;
					}
					NoIndium.config.save();
				}
				Objects.requireNonNull(this.minecraft).setScreen(new TitleScreen(false));
			}));
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}

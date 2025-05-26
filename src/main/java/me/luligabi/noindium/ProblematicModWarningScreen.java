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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ProblematicModWarningScreen extends WarningScreen {
	public static final Component CHECK_MESSAGE = new TranslatableComponent("multiplayerWarning.check");
	public static final Component PROCEED       = new TranslatableComponent("label.noindium.proceed");

	private final StartupWarningReason reason;

	public ProblematicModWarningScreen(@NotNull StartupWarningReason reason, @Nullable Screen previousScreen) {
		super(
			new TranslatableComponent(reason.header()).withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD),
			new TranslatableComponent(reason.message()), CHECK_MESSAGE,
			new TranslatableComponent(reason.header()).append("\n").append(reason.message()), previousScreen
		);
		this.reason = reason;
	}

	@Override
	protected void init() {
		((WarningScreenAccessor) this).setMessage(MultiLineLabel.create(font, new TranslatableComponent(reason.message()), width - 50));
		int yOffset = (((WarningScreenAccessor) this).getMessage().getLineCount() + 1) * font.lineHeight * 2 - 20;
		if (NoIndium.config.allowToProceed) {
			this.stopShowing = new Checkbox(width / 2 - 155 + 80, 76 + yOffset, 150, 20, CHECK_MESSAGE, false);
			this.addRenderableWidget(stopShowing);
		}
		initButtons(yOffset);
	}

	@Override
	protected void initButtons(int yOffset) {
		final int width = 150;
		final int height = 20;

		final int x1 = (this.width / 2) - (width + 5);
		final int x2 = x1 + width + 10;
		final int y = 100 + yOffset;

		if (reason.leftButtonLabel() != null && reason.leftButtonAction() != null) {
			Button left = new Button(x1, y, width, height, new TranslatableComponent(reason.leftButtonLabel()), reason.leftButtonAction());
			this.addRenderableWidget(left);
		}
		if (reason.rightButtonLabel() != null) {
			Button right = new Button(x2, y, width, height, new TranslatableComponent(reason.rightButtonLabel()), reason.rightButtonAction());
			addRenderableWidget(right);
		}

		if (NoIndium.config.allowToProceed) {
			this.addRenderableWidget(new Button(this.width / 2 - 75, 130 + yOffset, 150, 20,
				PROCEED, buttonWidget -> {
				if (this.stopShowing != null && this.stopShowing.isActive()) {
					reason.enable();
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

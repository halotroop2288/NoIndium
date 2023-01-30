package me.luligabi.noindium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class NoIndium implements ClientModInitializer {
	public static final TranslatableComponent CHECK_MESSAGE, PROCEED;
	public static final TranslatableComponent INDIUM_HEADER, INDIUM_MESSAGE;
	public static final TranslatableComponent OPTIFABRIC_HEADER, OPTIFABRIC_MESSAGE;
	public static final TranslatableComponent OPEN_MODS_FOLDER, OPTIFINE_ALTERNATIVES;
	public static final TextComponent CURSEFORGE, MODRINTH;
	public static final String CURSEFORGE_LINK, MODRINTH_LINK;
	public static final File MODS_FOLDER;
	public static final String OPTIFINE_ALTERNATIVES_URI;

	private static final FabricLoader fabric_loader = FabricLoader.getInstance();
	public static final NoIndiumConfig config = NoIndiumConfig.load();

	static {
		CHECK_MESSAGE = new TranslatableComponent("multiplayerWarning.check");
		PROCEED = new TranslatableComponent("label.noindium.proceed");

		INDIUM_HEADER = new TranslatableComponent("header.noindium.indium");
		INDIUM_MESSAGE = new TranslatableComponent("message.noindium.indium");

		OPTIFABRIC_HEADER = new TranslatableComponent("header.noindium.optifabric");
		OPTIFABRIC_MESSAGE = new TranslatableComponent("message.noindium.optifabric");

		CURSEFORGE = new TextComponent("CurseForge");
		MODRINTH = new TextComponent("Modrinth");

		OPEN_MODS_FOLDER = new TranslatableComponent("label.noindium.open_mods_folder");
		OPTIFINE_ALTERNATIVES = new TranslatableComponent("label.noindium.optifine_alternatives");

		CURSEFORGE_LINK = "https://www.curseforge.com/minecraft/mc-mods/indium";
		MODRINTH_LINK = "https://modrinth.com/mod/indium";

		MODS_FOLDER = new File(FabricLoader.getInstance().getGameDir().toFile(), "mods");
		OPTIFINE_ALTERNATIVES_URI = "https://lambdaurora.dev/optifine_alternatives/";
	}

	@Override
	public void onInitializeClient() {
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			// Cover all our bases by showing each screen consecutively if needed
			for (Reason reason : Reason.values()) {
				if (!reason.trigger.get()) continue;
				@Nullable Screen previousScreen = client.screen;
				if (reason.disabled) continue;
				client.setScreen(new ProblematicModWarningScreen(reason, previousScreen));
			}
		});
	}

	/**
	 * Allows expansion into use for other mods.
	 */
	public enum Reason {
		SODIUM_NO_INDIUM(
				INDIUM_HEADER,
				INDIUM_MESSAGE,
				() -> fabric_loader.isModLoaded("sodium") && !fabric_loader.isModLoaded("indium"),
				CURSEFORGE, button -> Util.getPlatform().openUri(MODRINTH_LINK),
				MODRINTH, button -> Util.getPlatform().openUri(CURSEFORGE_LINK),
				config.showIndiumScreen
		),
		USING_OPTIFABRIC(
				OPTIFABRIC_HEADER,
				OPTIFABRIC_MESSAGE,
				() -> fabric_loader.isModLoaded("optifabric"),
				OPEN_MODS_FOLDER, button -> Util.getPlatform().openFile(MODS_FOLDER),
				OPTIFINE_ALTERNATIVES, button -> Util.getPlatform().openUri(OPTIFINE_ALTERNATIVES_URI),
				config.showOptifabricScreen
		),
		;

		@NotNull
		public final TranslatableComponent header, message;
		@NotNull
		public final Supplier<Boolean> trigger;
		@NotNull
		public final Component button1Label, button2Label;
		@NotNull
		public final Button.OnPress button1Action, button2Action;
		private final boolean disabled;

		Reason(@NotNull TranslatableComponent header, @NotNull TranslatableComponent message,
		       @NotNull Supplier<Boolean> trigger, @NotNull Component button1Label, @NotNull Button.OnPress button1Action,
		       @NotNull Component button2Label, @NotNull Button.OnPress button2Action, final boolean disabled) {
			this.header = header;
			this.message = message;
			this.trigger = trigger;
			this.button1Label = button1Label;
			this.button2Label = button2Label;
			this.button1Action = button1Action;
			this.button2Action = button2Action;
			this.disabled = disabled;
		}
	}
}

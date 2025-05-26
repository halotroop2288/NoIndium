package me.luligabi.noindium;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Allows expansion into use for other mods.
 *
 * @since 2.0.0
 */
public record StartupWarningReason(
	@NotNull String configName, @NotNull String header, @NotNull String message,
	@NotNull Supplier<Boolean> trigger,
	@Nullable String leftButtonLabel, @Nullable Button.OnPress leftButtonAction,
	@Nullable String rightButtonLabel, @Nullable Button.OnPress rightButtonAction
) {
	public static final  String       INDIUM_MODRINTH_LINK      = "https://modrinth.com/mod/indium";
	public static final  File         MODS_FOLDER               = new File(FabricLoader.getInstance().getGameDir().toFile(), "mods");
	public static final  String       OPTIFINE_ALTERNATIVES_URI = "https://lambdaurora.dev/optifine_alternatives";
	private static final FabricLoader fabricLoader              = FabricLoader.getInstance();

	private static final HashSet<StartupWarningReason> reasons = new HashSet<>();

	static {
		registerReason(new StartupWarningReason(
			"sodium_no_indium",
			"header.noindium.indium",
			"message.noindium.indium",
			() -> fabricLoader.isModLoaded("sodium") && !fabricLoader.isModLoaded("indium"),
			"Modrinth", button -> Util.getPlatform().openUri(INDIUM_MODRINTH_LINK),
			null, null
		));
		registerReason(new StartupWarningReason(
			"using_optifabric",
			"header.noindium.optifabric",
			"message.noindium.optifabric",
			() -> fabricLoader.isModLoaded("optifabric"),
			"label.noindium.open_mods_folder", button -> Util.getPlatform().openFile(MODS_FOLDER),
			"label.noindium.optifine_alternatives", button -> Util.getPlatform().openUri(OPTIFINE_ALTERNATIVES_URI)
		));
	}

	/**
	 * @since 2.0.0
	 */
	public boolean isEnabled() {
		@Nullable Boolean enabled = NoIndium.config.show.get(this.configName);
		if (enabled != null) return enabled;

		// Options should be enabled by default
		this.enable();
		return true;
	}

	/**
	 * @since 2.0.0
	 */
	public void enable() {
		NoIndium.config.show.put(this.configName, true);
		NoIndium.config.save();
	}

	/**
	 * @since 2.0.0
	 */
	public void disable() {
		NoIndium.config.show.put(this.configName, false);
		NoIndium.config.save();
	}

	/**
	 * @since 2.0.0
	 */
	public boolean test() {
		return this.trigger.get();
	}

	/**
	 * @since 2.0.0
	 */
	@Environment(EnvType.CLIENT)
	public boolean tryShowWarningScreen(Minecraft client) {
		if (!this.isEnabled() || !this.test()) return false;

		client.setScreen(new ProblematicModWarningScreen(this, client.screen));
		return true;
	}

	/**
	 * @return an immutable set of the registered reasons
	 * @since 2.0.0
	 */
	public static Set<StartupWarningReason> getReasons() {
		return ImmutableSet.copyOf(reasons);
	}

	/**
	 * Add a new reason for a warning to the list
	 *
	 * @param reason the new reason
	 * @since 2.0.0
	 */
	public static void registerReason(StartupWarningReason reason) {
		reasons.add(reason);
	}
}

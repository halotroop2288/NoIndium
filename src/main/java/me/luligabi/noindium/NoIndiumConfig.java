package me.luligabi.noindium;

import blue.endless.jankson.Comment;
import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NoIndiumConfig {
	private static final Jankson jankson = Jankson.builder().build();
	private static final File config_directory = FabricLoader.getInstance().getConfigDir().toFile();
	private static final File config_file = new File(config_directory, "noindium.json5");
	private static final Logger logger = LogManager.getLogger(NoIndiumConfig.class);

	@Comment(value = "Whether the 'missing indium' screen can show up. This config value is set to false if the user uses the checkbox.")
	public boolean showIndiumScreen = true;

	@Comment(value = "Whether the 'remove optifabric' screen can show up. This config value is set to false if the user uses the checkbox.")
	public boolean showOptifabricScreen = true;

	@Comment(value = "Allow the user to proceed to the Title Screen even if one of the screens shows up. Setting this to false also removes the checkbox.")
	public boolean allowToProceed = true;

	public static NoIndiumConfig load() {
		if (!config_file.exists()) {
			return new NoIndiumConfig().save();
		}

		JsonObject configJson;
		try {
			configJson = jankson.load(config_file);
		} catch (IOException | SyntaxError e) {
			throw new RuntimeException("Failed to load config file.", e);
		}

		return jankson.fromJson(configJson, NoIndiumConfig.class);
	}

	public NoIndiumConfig save() {
		final String result = jankson.toJson(this).toJson(true, true);
		try {
			if (!config_file.exists() || config_file.createNewFile()) {
				FileOutputStream out = new FileOutputStream(config_file, false);

				out.write(result.getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			logger.error("Failed to save config! %s", e);
		}

		return this;
	}
}

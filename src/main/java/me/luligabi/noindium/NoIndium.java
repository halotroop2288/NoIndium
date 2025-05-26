package me.luligabi.noindium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

@Environment(EnvType.CLIENT)
public class NoIndium implements ClientModInitializer {
	public static final NoIndiumConfig config = NoIndiumConfig.load();

	@Override
	public void onInitializeClient() {
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			// Cover all our bases by showing each screen consecutively if needed
			StartupWarningReason.getReasons().stream().filter(it -> it.tryShowWarningScreen(client))
				.findAny().ifPresent(it -> System.out.println(it.message()));
		});
	}
}

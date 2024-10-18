package dev.aspect404.hudelement;

import dev.aspect404.hudelement.hud.CustomHudElement;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class Loader extends Plugin {
	@Override
	public void onLoad() {
		RusherHackAPI.getHudManager().registerFeature(new CustomHudElement());
	}
	@Override
	public void onUnload() {}
}
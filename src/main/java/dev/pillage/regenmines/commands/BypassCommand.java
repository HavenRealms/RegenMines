package dev.pillage.regenmines.commands;

import dev.pillage.regenmines.MineManager;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public final class BypassCommand extends SimpleSubCommand {
	public BypassCommand(SimpleCommandGroup parent) {
		super(parent, "bypass");
	}

	@Override
	protected void onCommand() {
		if (MineManager.isBypassing(getPlayer())) {
			tell("&cYou are no longer bypassing the mine sequences.");
		} else {
			tell("&aYou are now bypassing the mine sequences.");
		}

		MineManager.toggleBypass(getPlayer());
	}
}

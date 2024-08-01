package dev.pillage.regenmines.commands;

import dev.pillage.regenmines.SequenceManager;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public final class BypassCommand extends SimpleSubCommand {
	public BypassCommand(SimpleCommandGroup parent) {
		super(parent, "bypass");
	}

	@Override
	protected void onCommand() {
		if (SequenceManager.isBypassing(getPlayer())) {
			tell("&cYou are no longer bypassing the mine sequences.");
		} else {
			tell("&aYou are now bypassing the mine sequences.");
		}

		SequenceManager.toggleBypass(getPlayer());
	}
}

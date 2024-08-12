package dev.pillage.regenmines.commands;

import dev.pillage.regenmines.MineManager;
import org.mineacademy.fo.command.SimpleSubCommand;

public class UpdateCmd extends SimpleSubCommand {
	protected UpdateCmd() {
		super("update|u");
	}

	@Override
	protected void onCommand() {
		tell("&7Updating mine states...");
		MineManager.scanInitialStates();
		tell("&aMine states updated!");
	}
}

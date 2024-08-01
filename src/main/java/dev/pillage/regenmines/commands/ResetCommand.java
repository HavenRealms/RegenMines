package dev.pillage.regenmines.commands;

import dev.pillage.regenmines.MineManager;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public final class ResetCommand extends SimpleSubCommand {

	public ResetCommand(SimpleCommandGroup parent) {
		super(parent, "reset");
		setPermission("regenmines.command.reset");
	}

	@Override
	protected void onCommand() {
		long start = System.currentTimeMillis();
		tell("&cResetting sequences...");
		MineManager.resetSequences();
		tell("&aSequences reset in &e" + (System.currentTimeMillis() - start) + "ms");
	}
}

package dev.pillage.regenmines.commands;

import dev.pillage.regenmines.SequenceManager;
import org.mineacademy.fo.annotation.AutoRegister;
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
		SequenceManager.resetSequences();
		tell("&aSequences reset in &e" + (System.currentTimeMillis() - start) + "ms");
	}
}

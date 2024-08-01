package dev.pillage.regenmines.commands;

import dev.pillage.regenmines.SequenceManager;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public final class RescanCommand extends SimpleSubCommand {

	public RescanCommand(SimpleCommandGroup parent) {
		super(parent, "rescan|scan");
		setPermission("regenmines.command.rescan");
	}

	@Override
	protected void onCommand() {
		long start = System.currentTimeMillis();
		tell("&cScanning regions...");
		SequenceManager.scanRegions();
		tell("&aRegions scanned in &e" + (System.currentTimeMillis() - start) + "ms");
	}
}

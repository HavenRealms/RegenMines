package dev.pillage.regenmines;

import dev.pillage.regenmines.commands.BypassCommand;
import dev.pillage.regenmines.commands.RescanCommand;
import dev.pillage.regenmines.commands.ResetCommand;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

@AutoRegister
public final class MainCommandGroup extends SimpleCommandGroup {

	public MainCommandGroup() {
		this.setLabel("mines|m");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new ReloadCommand());
		registerSubcommand(new RescanCommand(this));
		registerSubcommand(new ResetCommand(this));
		registerSubcommand(new BypassCommand(this));
	}
}

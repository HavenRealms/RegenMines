package dev.pillage.regenmines;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Map;
import java.util.Objects;

@Getter
public final class RegenMines extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		Common.log("RegenMines has been enabled!");
		this.saveDefaultConfig();
	}

	@Override
	protected void onReloadablesStart() {
		Bukkit.getScheduler().cancelTasks(this);
		MineManager.reset();
		reloadConfig();

		MineManager.init();
	}

	@Override
	protected void onPluginPreReload() {
		Bukkit.getScheduler().cancelTasks(this);
	}

	@Override
	protected void onPluginStop() {
		Bukkit.getScheduler().cancelTasks(this);
	}
}

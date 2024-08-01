package dev.pillage.regenmines;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineManager {

	@Getter
	private static final List<MineSequence> sequences = new ArrayList<>();

	private static final Map<Location, MineSequence> scannedSequences = new HashMap<>();

	private static final Map<Material, MineSequence> startMappings = new HashMap<>();

	private static final List<Player> bypassingPlayers = new ArrayList<>();

	public static void addSequence(MineSequence sequence) {
		sequences.add(sequence);
	}

	public static void init() {
		sequences.clear();
		startMappings.clear();
		scannedSequences.clear();

		FileConfiguration config = RegenMines.getInstance().getConfig();

		config.getMapList("sequences").forEach(sequenceMap -> {
			String name = (String) sequenceMap.get("name");
			String region = (String) sequenceMap.get("region");
			String replace = (String) sequenceMap.get("replace");
			double delay = ((Number) sequenceMap.get("delay")).doubleValue();
		});

		Common.log("Loaded " + sequences.size() + " sequences.");
	}

	public static void scanRegions() {
		FileConfiguration config = RegenMines.getInstance().getConfig();

		String defaultWorldName = config.getString("default-world");
		World defaultWorld = Bukkit.getWorld(defaultWorldName == null ? "world" : defaultWorldName);

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regions = container.get(BukkitAdapter.adapt(defaultWorld));

		long start = System.currentTimeMillis();

		sequences.forEach(sequence -> {
			String region = sequence.getRegion();

			ProtectedRegion protectedRegion = regions.getRegion(region);

			if (protectedRegion == null) {
				Common.warning("Region " + region + " not found.");
				return;
			}

			Location point1 = new Location(defaultWorld, protectedRegion.getMinimumPoint().x(), protectedRegion.getMinimumPoint().y(), protectedRegion.getMinimumPoint().z());
			Location point2 = new Location(defaultWorld, protectedRegion.getMaximumPoint().x(), protectedRegion.getMaximumPoint().y(), protectedRegion.getMaximumPoint().z());

			Vector max = Vector.getMaximum(point1.toVector(), point2.toVector());
			Vector min = Vector.getMinimum(point1.toVector(), point2.toVector());
			for (int i = min.getBlockX(); i <= max.getBlockX();i++) {
				for (int j = min.getBlockY(); j <= max.getBlockY(); j++) {
					for (int k = min.getBlockZ(); k <= max.getBlockZ();k++) {
						Material material = defaultWorld.getBlockAt(i,j,k).getType();

						if (startMappings.containsKey(material)) {
							scannedSequences.put(new Location(defaultWorld, i, j, k), startMappings.get(material));
						}
					}
				}
			}

		});

		Common.log("Scanned " + scannedSequences.size() + " sequence locations in " + (System.currentTimeMillis() - start) + "ms.");
	}

	public static void resetSequences() {
		startMappings.forEach((material, sequence) -> {
			scannedSequences.forEach((location, seq) -> {
				if (seq.equals(sequence)) {
					location.getBlock().setType(material);
				}
			});
		});
	}

	@Nullable
	public static MineSequence getSequence(Location location) {
		return scannedSequences.getOrDefault(location, null);
	}

	public static void toggleBypass(Player player) {
		if (bypassingPlayers.contains(player)) {
			bypassingPlayers.remove(player);
		} else {
			bypassingPlayers.add(player);
		}
	}

	public static boolean isBypassing(Player player) {
		return bypassingPlayers.contains(player);
	}
}

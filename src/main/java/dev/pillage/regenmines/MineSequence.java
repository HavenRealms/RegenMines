package dev.pillage.regenmines;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

@Data
@AllArgsConstructor
public class MineSequence {
	private String name;
	private String replace;
	private String region;
	private double delay;
	
	public Material getMaterial() {
		return Material.getMaterial(name.toUpperCase());
	}

	public Material getReplacementMaterial() {
		return Material.getMaterial(replace.toUpperCase());
	}
}

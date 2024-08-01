package dev.pillage.regenmines;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

@Data
@AllArgsConstructor
public class MineSequence {
	private String name;
	private String region;
	private String[] sequence;
	private double delay;

	public Material[] getSequence() {
		Material[] materials = new Material[sequence.length];
		for (int i = 0; i < sequence.length; i++) {
			materials[i] = Material.getMaterial(sequence[i].toUpperCase());
		}
		return materials;
	}

	public Material getNextMaterial(Material current) {
		for (int i = 0; i < sequence.length; i++) {
			if (Material.getMaterial(sequence[i].toUpperCase()) == current) {
				return Material.getMaterial(sequence[(i + 1) % sequence.length].toUpperCase());
			}
		}
		return Material.getMaterial(sequence[0].toUpperCase());
	}

	public Material getFirstMaterial() {
		return getSequence()[0];
	}
}

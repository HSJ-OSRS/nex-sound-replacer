package hsj.external.nexsoundreplacer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("nexSoundReplacer")
public interface nexSoundReplacerConfig extends Config
{
	@ConfigSection(
		name = "Teleport",
		description = "Teleport sound settings",
		position = 1,
		closedByDefault = false
	)
	String teleport = "teleportSection";

	@ConfigItem(
		keyName = "teleportSoundFile",
		name = "Sound file",
		description = "Specify a path to a .wav sound file you would like for the teleport sound effect.",
		position = 0,
		section = teleport
	)
	default String soundFile()
	{
		return "";
	}

	@Range(max = 100)
	@ConfigItem(
		name = "Volume",
		keyName = "teleportSoundVolume",
		description = "Sets the volume of the sound clip",
		position = 1,
		section = teleport
	)
	default int soundVolume()
	{
		return 50;
	}
}

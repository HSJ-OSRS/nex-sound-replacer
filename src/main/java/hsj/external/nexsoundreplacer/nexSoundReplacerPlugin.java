package hsj.external.nexsoundreplacer;

import com.google.inject.Provides;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import javax.inject.Inject;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(name = "Nex Sound Replacer")
public class nexSoundReplacerPlugin extends Plugin
{
	private static Clip teleportSound;

	@Inject
	private Client client;

	@Inject
	private nexSoundReplacerConfig config;

	@Override
	protected void startUp() throws Exception
	{
		teleportSound = generateSoundClip(config.soundFile(), config.soundVolume());
	}

	@Override
	protected void shutDown() throws Exception
	{
		teleportSound = null;
	}

	@Subscribe
	public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed areaSoundEffectPlayed)
	{
		int soundId = areaSoundEffectPlayed.getSoundId();
		if (soundId == 5291 && teleportSound != null)
		{
			areaSoundEffectPlayed.consume();
			teleportSound.setFramePosition(0);
			teleportSound.start();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("nexSoundReplacer"))
		{
			if (event.getKey().equals("teleportSoundFile"))
			{
				teleportSound = generateSoundClip(config.soundFile(), config.soundVolume());
			}

			if (event.getKey().equals("teleportSoundVolume"))
			{
				if (teleportSound != null)
				{
					FloatControl control = (FloatControl) teleportSound.getControl(FloatControl.Type.MASTER_GAIN);

					if (control != null)
					{
						control.setValue((float) (config.soundVolume() / 2 - 45));
					}

					teleportSound.setFramePosition(0);
					teleportSound.start();
				}
			}
		}
	}

	public Clip generateSoundClip(String soundFile, int volume)
	{
		Clip soundClip = null;
		AudioInputStream inputStream = null;
		try
		{
			URL url = Paths.get(soundFile).toUri().toURL();
			inputStream = AudioSystem.getAudioInputStream(url);
		}
		catch (UnsupportedAudioFileException | IOException e)
		{
			log.warn("Unable to create audio input stream: ", e);
		}

		if (inputStream == null)
		{
			return null;
		}

		try
		{
			soundClip = AudioSystem.getClip();
			soundClip.open(inputStream);
		}
		catch (LineUnavailableException | IOException e)
		{
			log.warn("Could not load sound file: ", e);
		}

		if (soundClip == null)
		{
			return null;
		}

		FloatControl control = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
		control.setValue((float) (volume / 2 - 45));

		return soundClip;
	}

	@Provides
	nexSoundReplacerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(nexSoundReplacerConfig.class);
	}
}

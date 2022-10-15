package com.example;

import hsj.external.nexsoundreplacer.nexSoundReplacerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(nexSoundReplacerPlugin.class);
		RuneLite.main(args);
	}
}
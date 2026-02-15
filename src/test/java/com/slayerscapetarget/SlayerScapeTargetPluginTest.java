package com.slayerscapetarget;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SlayerScapeTargetPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SlayerScapeTargetPlugin.class);
		RuneLite.main(args);
	}
}
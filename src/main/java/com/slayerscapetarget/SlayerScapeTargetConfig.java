package com.slayerscapetarget;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("slayerscapetarget")
public interface SlayerScapeTargetConfig extends Config
{
	@ConfigItem(
			keyName = "hidenotask",
			name = "Hide attack without task",
			description = "Keep attack option hidden even without an active slayer task"
	)
	default boolean hideNoTask()
	{
		return true;
	}
}

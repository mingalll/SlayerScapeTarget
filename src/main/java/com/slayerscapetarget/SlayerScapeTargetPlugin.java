package com.slayerscapetarget;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.slayer.*;

import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Slayer Scape Target"
)
@PluginDependency(SlayerPlugin.class)
public class SlayerScapeTargetPlugin extends Plugin
{
	private static final List<MenuAction> NPC_MENU_TYPES = ImmutableList.of(
			MenuAction.NPC_FIRST_OPTION,
			MenuAction.NPC_SECOND_OPTION,
			MenuAction.NPC_THIRD_OPTION,
			MenuAction.NPC_FOURTH_OPTION,
			MenuAction.NPC_FIFTH_OPTION
	);

	@Inject
	private SlayerPluginService slayerPluginService;

	private Boolean onSlayerTask = false;

	@Inject
	private Client client;

	@Inject
	private SlayerScapeTargetConfig config;

	@Override
	protected void startUp() throws Exception
	{
		if(client.getGameState() == GameState.LOGGED_IN)
		{
			int amount = client.getVarpValue(VarPlayerID.SLAYER_COUNT);
			if(amount == 0)
			{
				onSlayerTask = false;
				return;
			}

			onSlayerTask = true;
		}
	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{

	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		int varpId = varbitChanged.getVarpId();
		if (varpId == VarPlayerID.SLAYER_COUNT)
		{
			int amount = client.getVarpValue(varpId);

            onSlayerTask = amount > 0;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		if (!onSlayerTask && !config.hideNoTask())
		{
			return;
		}

		if (client.isMenuOpen())
		{
			return;
		}

		for (MenuEntry menuEntry : client.getMenuEntries())
		{
			MenuAction type = menuEntry.getType();

			if (NPC_MENU_TYPES.contains(type))
			{
				final NPC npc = menuEntry.getNpc();
				assert npc != null;
				final NPCComposition composition = npc.getTransformedComposition();
				assert composition != null;

				final String entryText = menuEntry.getOption();
				if(!entryText.equalsIgnoreCase("Attack"))
				{
					continue;
				}

				if(!slayerPluginService.getTargets().contains(npc))
				{
					menuEntry.setDeprioritized(true);
				}
			}
		}
	}

	@Provides
    SlayerScapeTargetConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SlayerScapeTargetConfig.class);
	}
}

package com.wuest.prefab;

import java.util.List;

import com.wuest.prefab.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;

/**
 * The starting point to load all of the blocks, items and other objects
 * associated with this mod.
 * 
 * @author WuestMan
 *
 */
@Mod(modid = Prefab.MODID, version = Prefab.VERSION, acceptedMinecraftVersions = "[1.11]", guiFactory = "com.wuest.prefab.Gui.ConfigGuiFactory")
public class Prefab
{
	public static final String MODID = "prefab";
	public static final String VERSION = "1.2.0.1";
	public static boolean isDebug = false;

	@Instance(value = Prefab.MODID)
	public static Prefab instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.wuest.prefab.Proxy.ClientProxy", serverSide = "com.wuest.prefab.Proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	public static Configuration config;

	static
	{
		Prefab.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Prefab.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Prefab.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		Prefab.proxy.postinit(event);
	}
	
	@EventHandler
	public void OnMissingMapping(FMLMissingMappingsEvent event) 
	{
		List<MissingMapping> missingMappings = event.get();
		
		for(MissingMapping mapping : missingMappings) 
		{
			if (mapping.type == Type.BLOCK)
			{
				Block mappedBlock = null;
				
				switch (mapping.resourceLocation.getResourcePath())
				{
					case "blockcompressedstone":
					{
						mappedBlock = ModRegistry.CompressedStoneBlock();
					}
				}
				
				if (mappedBlock != null)
				{
					mapping.remap(mappedBlock);
				}
			}
			else
			{
				Item mappedItem = null;
				
				switch (mapping.resourceLocation.getResourcePath())
				{
					case "blockcompressedstone":
					{
						mappedItem = ModRegistry.ModItems.stream().filter(item -> item.getRegistryName().getResourcePath().equals("block_compressed_stone")).findFirst().get();
						break;
					}
					
					case "itemproducefarm" :
					{
						mappedItem = ModRegistry.ProduceFarm();
						break;
					}
					
					case "itempileofbricks" :
					{
						mappedItem = ModRegistry.PileOfBricks();
						break;
					}
					
					case "itemhorsestable" :
					{
						mappedItem = ModRegistry.HorseStable();
						break;
					}
					
					case "itemnethergate" :
					{
						mappedItem = ModRegistry.NetherGate();
						break;
					}
					
					case "itemwarehouseupgrade" :
					{
						mappedItem = ModRegistry.WareHouseUpgrade();
						break;
					}
					
					case "itemchickencoop" :
					{
						mappedItem = ModRegistry.ChickenCoop();
						break;
					}
					
					case "itemtreefarm" :
					{
						mappedItem = ModRegistry.TreeFarm();
						break;
					}
					
					case "itemcompressedchest" :
					{
						mappedItem = ModRegistry.CompressedChestItem();
						break;
					}
					
					case "itembundleoftimber" :
					{
						mappedItem = ModRegistry.BundleOfTimber();
						break;
					}
					
					case "itemwarehouse" :
					{
						mappedItem = ModRegistry.WareHouse();
						break;
					}
					
					case "itempalletofbricks" :
					{
						mappedItem = ModRegistry.PalletOfBricks();
						break;
					}
					
					case "itemfishpond" :
					{
						mappedItem = ModRegistry.FishPond();
						break;
					}
					
					case "itemmonstermasher" :
					{
						mappedItem = ModRegistry.MonsterMasher();
						break;
					}
					
					case "itemstarthouse" :
					{
						mappedItem = ModRegistry.StartHouse();
						break;
					}
					
					case "itemadvancedwarehouse" :
					{
						mappedItem = ModRegistry.AdvancedWareHouse();
						break;
					}
				}
				
				if (mappedItem != null)
				{
					mapping.remap(mappedItem);
				}
			}
		}
	}
}

package com.ultramega.refinedtypes;

import com.ultramega.refinedtypes.autocrafting.EnergyPatternProviderExternalPatternSinkFactory;
import com.ultramega.refinedtypes.autocrafting.SoulPatternProviderExternalPatternSinkFactory;
import com.ultramega.refinedtypes.autocrafting.SourcePatternProviderExternalPatternSinkFactory;
import com.ultramega.refinedtypes.compat.emi.EmiEnergyResourceModIngredientConverter;
import com.ultramega.refinedtypes.compat.jei.JEIRecipeModIngredientConverter;
import com.ultramega.refinedtypes.exporter.EnergyExporterTransferStrategyFactory;
import com.ultramega.refinedtypes.exporter.SoulExporterTransferStrategyFactory;
import com.ultramega.refinedtypes.exporter.SourceExporterTransferStrategyFactory;
import com.ultramega.refinedtypes.externalstorage.EnergyPlatformExternalStorageProviderFactory;
import com.ultramega.refinedtypes.externalstorage.SoulPlatformExternalStorageProviderFactory;
import com.ultramega.refinedtypes.externalstorage.SourcePlatformExternalStorageProviderFactory;
import com.ultramega.refinedtypes.grid.TypeGridResourceFactory;
import com.ultramega.refinedtypes.grid.energy.EnergyGridExtractionStrategy;
import com.ultramega.refinedtypes.grid.energy.EnergyGridInsertionStrategy;
import com.ultramega.refinedtypes.importer.EnergyImporterTransferStrategyFactory;
import com.ultramega.refinedtypes.importer.SoulImporterTransferStrategyFactory;
import com.ultramega.refinedtypes.importer.SourceImporterTransferStrategyFactory;
import com.ultramega.refinedtypes.registry.BlockEntities;
import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageBlockBlockItem;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageBlockProvider;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageDiskItem;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageInterface;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageBlockBlockItem;
import com.ultramega.refinedtypes.storage.soul.SoulStorageBlockProvider;
import com.ultramega.refinedtypes.storage.soul.SoulStorageDiskItem;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageBlockBlockItem;
import com.ultramega.refinedtypes.storage.source.SourceStorageBlockProvider;
import com.ultramega.refinedtypes.storage.source.SourceStorageDiskItem;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;
import com.ultramega.refinedtypes.storagemonitor.EnergyStorageMonitorInsertionStrategy;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceContainerInsertStrategy;
import com.ultramega.refinedtypes.type.energy.EnergyResourceFactory;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.soul.SoulResourceFactory;
import com.ultramega.refinedtypes.type.soul.SoulResourceType;
import com.ultramega.refinedtypes.type.soul.SoulUtil;
import com.ultramega.refinedtypes.type.source.SourceResource;
import com.ultramega.refinedtypes.type.source.SourceResourceFactory;
import com.ultramega.refinedtypes.type.source.SourceResourceType;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.network.AbstractNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.content.BlockConstants;
import com.refinedmods.refinedstorage.common.content.ExtendedMenuTypeFactory;
import com.refinedmods.refinedstorage.common.storage.StorageContainerUpgradeRecipe;
import com.refinedmods.refinedstorage.common.storage.StorageContainerUpgradeRecipeSerializer;
import com.refinedmods.refinedstorage.common.support.SimpleItem;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;

import java.util.Set;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isArsNouveauLoaded;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isIndustrialForegoingSoulsLoaded;

@Mod(RefinedTypesUtil.MOD_ID)
public final class ModInitializer {
    private static final ResourceLocation ENERGY_ID = createRefinedTypesIdentifier("energy");
    private static final ResourceLocation SOURCE_ID = createRefinedTypesIdentifier("source");
    private static final ResourceLocation SOUL_ID = createRefinedTypesIdentifier("soul");

    private static final Config CONFIG = new Config();

    // TODO: add an energy converter/user block (Network Energizer?) which can use the saved energy on disk to power the controller
    public ModInitializer(final IEventBus eventBus, final ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CONFIG.getSpec());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(ClientModInitializer::onClientSetup);
            eventBus.addListener(ClientModInitializer::onRegisterMenuScreens);
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerRegistries);
        eventBus.addListener(this::registerCreativeModeTab);
        eventBus.addListener(this::registerCapabilities);

        Types.TYPES.register(eventBus);

        final DeferredRegister<Item> itemRegistry = DeferredRegister.create(
            BuiltInRegistries.ITEM,
            RefinedTypesUtil.MOD_ID
        );
        registerItems(itemRegistry);
        itemRegistry.register(eventBus);

        final DeferredRegister<Block> blockRegistry = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            RefinedTypesUtil.MOD_ID
        );
        registerBlocks(blockRegistry);
        blockRegistry.register(eventBus);

        final DeferredRegister<BlockEntityType<?>> blockEntityRegistry = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            RefinedTypesUtil.MOD_ID
        );
        registerBlockEntities(blockEntityRegistry);
        blockEntityRegistry.register(eventBus);

        final DeferredRegister<MenuType<?>> menuRegistry = DeferredRegister.create(
            BuiltInRegistries.MENU,
            RefinedTypesUtil.MOD_ID
        );
        final ExtendedMenuTypeFactory extendedMenuTypeFactory = new ExtendedMenuTypeFactory() {
            @Override
            public <T extends AbstractContainerMenu, D> MenuType<T> create(final MenuSupplier<T, D> supplier,
                                                                           final StreamCodec<RegistryFriendlyByteBuf, D>
                                                                               streamCodec) {
                return IMenuTypeExtension.create((syncId, inventory, buf) -> {
                    final D data = streamCodec.decode(buf);
                    return supplier.create(syncId, inventory, data);
                });
            }
        };
        registerMenus(menuRegistry, extendedMenuTypeFactory);
        menuRegistry.register(eventBus);

        final DeferredRegister<RecipeSerializer<?>> recipeSerializerRegistry = DeferredRegister.create(
            BuiltInRegistries.RECIPE_SERIALIZER,
            RefinedTypesUtil.MOD_ID
        );
        registerRecipeSerializers(recipeSerializerRegistry);
        recipeSerializerRegistry.register(eventBus);
    }

    private static void registerBlocks(final DeferredRegister<Block> registry) {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            Blocks.setEnergyStorageBlock(variant, registry.register(variant.getStorageBlockId().getPath(),
                () -> RefinedStorageApi.INSTANCE.createStorageBlock(BlockConstants.PROPERTIES,
                    new EnergyStorageBlockProvider(variant))));
        }
        if (isArsNouveauLoaded()) {
            for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                Blocks.setSourceStorageBlock(variant, registry.register(variant.getStorageBlockId().getPath(),
                    () -> RefinedStorageApi.INSTANCE.createStorageBlock(BlockConstants.PROPERTIES,
                        new SourceStorageBlockProvider(variant))));
            }
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                Blocks.setSoulStorageBlock(variant, registry.register(variant.getStorageBlockId().getPath(),
                    () -> RefinedStorageApi.INSTANCE.createStorageBlock(BlockConstants.PROPERTIES,
                        new SoulStorageBlockProvider(variant))));
            }
        }
    }

    private static void registerItems(final DeferredRegister<Item> registry) {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            Items.setEnergyStoragePart(variant, registry.register(variant.getStoragePartId().getPath(), SimpleItem::new));
            Items.setEnergyStorageDisk(variant, registry.register(variant.getStorageDiskId().getPath(), () -> new EnergyStorageDiskItem(
                RefinedStorageApi.INSTANCE.getStorageContainerItemHelper(),
                variant)));
            Items.setEnergyStorageBlock(variant, registry.register(variant.getStorageBlockId().getPath(), () -> new EnergyStorageBlockBlockItem(
                Blocks.getEnergyStorageBlock(variant),
                variant)));
        }
        if (isArsNouveauLoaded()) {
            for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                Items.setSourceStoragePart(variant, registry.register(variant.getStoragePartId().getPath(), SimpleItem::new));
                Items.setSourceStorageDisk(variant, registry.register(variant.getStorageDiskId().getPath(), () -> new SourceStorageDiskItem(
                    RefinedStorageApi.INSTANCE.getStorageContainerItemHelper(), variant)));
                Items.setSourceStorageBlock(variant, registry.register(variant.getStorageBlockId().getPath(), () -> new SourceStorageBlockBlockItem(
                    Blocks.getSourceStorageBlock(variant),
                    variant)));
            }
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                Items.setSoulStoragePart(variant, registry.register(variant.getStoragePartId().getPath(), SimpleItem::new));
                Items.setSoulStorageDisk(variant, registry.register(variant.getStorageDiskId().getPath(), () -> new SoulStorageDiskItem(
                    RefinedStorageApi.INSTANCE.getStorageContainerItemHelper(), variant)));
                Items.setSoulStorageBlock(variant, registry.register(variant.getStorageBlockId().getPath(), () -> new SoulStorageBlockBlockItem(
                    Blocks.getSoulStorageBlock(variant),
                    variant)));
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private static void registerBlockEntities(final DeferredRegister<BlockEntityType<?>> blockEntityRegistry) {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            BlockEntities.setEnergyStorageBlock(variant,
                blockEntityRegistry.register(variant.getStorageBlockId().getPath(), () -> new BlockEntityType<>(
                    (pos, state) -> RefinedStorageApi.INSTANCE.createStorageBlockEntity(pos, state,
                        new EnergyStorageBlockProvider(variant)),
                    Set.of(Blocks.getEnergyStorageBlock(variant)),
                    null
                ))
            );
        }
        if (isArsNouveauLoaded()) {
            for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                BlockEntities.setSourceStorageBlock(variant,
                    blockEntityRegistry.register(variant.getStorageBlockId().getPath(), () -> new BlockEntityType<>(
                        (pos, state) -> RefinedStorageApi.INSTANCE.createStorageBlockEntity(pos, state,
                            new SourceStorageBlockProvider(variant)),
                        Set.of(Blocks.getSourceStorageBlock(variant)),
                        null
                    ))
                );
            }
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                BlockEntities.setSoulStorageBlock(variant,
                    blockEntityRegistry.register(variant.getStorageBlockId().getPath(), () -> new BlockEntityType<>(
                        (pos, state) -> RefinedStorageApi.INSTANCE.createStorageBlockEntity(pos, state,
                            new SoulStorageBlockProvider(variant)),
                        Set.of(Blocks.getSoulStorageBlock(variant)),
                        null
                    ))
                );
            }
        }
    }

    private static void registerMenus(final DeferredRegister<MenuType<?>> menuRegistry,
                                      final ExtendedMenuTypeFactory extendedMenuTypeFactory) {
        Menus.setEnergyStorage(menuRegistry.register(
            "energy_storage_block",
            () -> extendedMenuTypeFactory.create((syncId, playerInventory, data) ->
                RefinedStorageApi.INSTANCE.createStorageBlockContainerMenu(syncId, playerInventory.player, data,
                    EnergyResourceFactory.INSTANCE, Menus.getEnergyStorage()), RefinedStorageApi.INSTANCE.getStorageBlockDataStreamCodec())));
        if (isArsNouveauLoaded()) {
            Menus.setSourceStorage(menuRegistry.register(
                "source_storage_block",
                () -> extendedMenuTypeFactory.create((syncId, playerInventory, data) ->
                    RefinedStorageApi.INSTANCE.createStorageBlockContainerMenu(syncId, playerInventory.player, data,
                        SourceResourceFactory.INSTANCE, Menus.getSourceStorage()), RefinedStorageApi.INSTANCE.getStorageBlockDataStreamCodec())));
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            Menus.setSoulStorage(menuRegistry.register(
                "soul_storage_block",
                () -> extendedMenuTypeFactory.create((syncId, playerInventory, data) ->
                    RefinedStorageApi.INSTANCE.createStorageBlockContainerMenu(syncId, playerInventory.player, data,
                        SoulResourceFactory.INSTANCE, Menus.getSoulStorage()), RefinedStorageApi.INSTANCE.getStorageBlockDataStreamCodec())));
        }
    }

    private static void registerRecipeSerializers(final DeferredRegister<RecipeSerializer<?>> registry) {
        registry.register(
            "energy_storage_disk_upgrade",
            () -> new StorageContainerUpgradeRecipeSerializer<>(
                EnergyStorageVariant.values(),
                to -> new StorageContainerUpgradeRecipe<>(
                    EnergyStorageVariant.values(), to, Items::getEnergyStorageDisk
                )
            )
        );
        registry.register(
            "energy_storage_block_upgrade",
            () -> new StorageContainerUpgradeRecipeSerializer<>(
                EnergyStorageVariant.values(),
                to -> new StorageContainerUpgradeRecipe<>(
                    EnergyStorageVariant.values(), to, Items::getEnergyStorageBlock
                )
            )
        );
        if (isArsNouveauLoaded()) {
            registry.register(
                "source_storage_disk_upgrade",
                () -> new StorageContainerUpgradeRecipeSerializer<>(
                    SourceStorageVariant.values(),
                    to -> new StorageContainerUpgradeRecipe<>(
                        SourceStorageVariant.values(), to, Items::getSourceStorageDisk
                    )
                )
            );
            registry.register(
                "source_storage_block_upgrade",
                () -> new StorageContainerUpgradeRecipeSerializer<>(
                    SourceStorageVariant.values(),
                    to -> new StorageContainerUpgradeRecipe<>(
                        SourceStorageVariant.values(), to, Items::getSourceStorageBlock
                    )
                )
            );
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            registry.register(
                "soul_storage_disk_upgrade",
                () -> new StorageContainerUpgradeRecipeSerializer<>(
                    SoulStorageVariant.values(),
                    to -> new StorageContainerUpgradeRecipe<>(
                        SoulStorageVariant.values(), to, Items::getSoulStorageDisk
                    )
                )
            );
            registry.register(
                "soul_storage_block_upgrade",
                () -> new StorageContainerUpgradeRecipeSerializer<>(
                    SoulStorageVariant.values(),
                    to -> new StorageContainerUpgradeRecipe<>(
                        SoulStorageVariant.values(), to, Items::getSoulStorageBlock
                    )
                )
            );
        }
    }

    private void setup(final FMLCommonSetupEvent e) {
        RefinedStorageApi.INSTANCE.getResourceTypeRegistry().register(ENERGY_ID, EnergyResourceType.INSTANCE);
        RefinedStorageApi.INSTANCE.getAlternativeResourceFactories().add(EnergyResourceFactory.INSTANCE);
        RefinedStorageApi.INSTANCE.getStorageTypeRegistry().register(ENERGY_ID, EnergyResourceType.STORAGE_TYPE);
        RefinedStorageApi.INSTANCE.addGridResourceRepositoryMapper(EnergyResource.class, new TypeGridResourceFactory());
        RefinedStorageApi.INSTANCE.addGridInsertionStrategyFactory(EnergyGridInsertionStrategy::new);
        RefinedStorageApi.INSTANCE.addGridExtractionStrategyFactory(EnergyGridExtractionStrategy::new);
        RefinedStorageApi.INSTANCE.addStorageMonitorInsertionStrategy(new EnergyStorageMonitorInsertionStrategy());
        RefinedStorageApi.INSTANCE.addResourceContainerInsertStrategy(new EnergyResourceContainerInsertStrategy());
        RefinedStorageApi.INSTANCE.getImporterTransferStrategyRegistry().register(ENERGY_ID, new EnergyImporterTransferStrategyFactory());
        RefinedStorageApi.INSTANCE.getExporterTransferStrategyRegistry().register(ENERGY_ID, new EnergyExporterTransferStrategyFactory());
        RefinedStorageApi.INSTANCE.addExternalStorageProviderFactory(new EnergyPlatformExternalStorageProviderFactory());
        RefinedStorageApi.INSTANCE.addPatternProviderExternalPatternSinkFactory(new EnergyPatternProviderExternalPatternSinkFactory());

        if (isArsNouveauLoaded()) {
            RefinedStorageApi.INSTANCE.getResourceTypeRegistry().register(SOURCE_ID, SourceResourceType.INSTANCE);
            RefinedStorageApi.INSTANCE.getAlternativeResourceFactories().add(SourceResourceFactory.INSTANCE);
            RefinedStorageApi.INSTANCE.getStorageTypeRegistry().register(SOURCE_ID, SourceResourceType.STORAGE_TYPE);
            RefinedStorageApi.INSTANCE.addGridResourceRepositoryMapper(SourceResource.class, new TypeGridResourceFactory());
            RefinedStorageApi.INSTANCE.getImporterTransferStrategyRegistry().register(SOURCE_ID, new SourceImporterTransferStrategyFactory());
            RefinedStorageApi.INSTANCE.getExporterTransferStrategyRegistry().register(SOURCE_ID, new SourceExporterTransferStrategyFactory());
            RefinedStorageApi.INSTANCE.addExternalStorageProviderFactory(new SourcePlatformExternalStorageProviderFactory());
            RefinedStorageApi.INSTANCE.addPatternProviderExternalPatternSinkFactory(new SourcePatternProviderExternalPatternSinkFactory());
        }

        if (isIndustrialForegoingSoulsLoaded()) {
            RefinedStorageApi.INSTANCE.getResourceTypeRegistry().register(SOUL_ID, SoulResourceType.INSTANCE);
            RefinedStorageApi.INSTANCE.getAlternativeResourceFactories().add(SoulResourceFactory.INSTANCE);
            RefinedStorageApi.INSTANCE.getStorageTypeRegistry().register(SOUL_ID, SoulResourceType.STORAGE_TYPE);
            RefinedStorageApi.INSTANCE.addGridResourceRepositoryMapper(SoulResource.class, new TypeGridResourceFactory());
            RefinedStorageApi.INSTANCE.getImporterTransferStrategyRegistry().register(SOUL_ID, new SoulImporterTransferStrategyFactory());
            RefinedStorageApi.INSTANCE.getExporterTransferStrategyRegistry().register(SOUL_ID, new SoulExporterTransferStrategyFactory());
            RefinedStorageApi.INSTANCE.addExternalStorageProviderFactory(new SoulPlatformExternalStorageProviderFactory());
            RefinedStorageApi.INSTANCE.addPatternProviderExternalPatternSinkFactory(new SoulPatternProviderExternalPatternSinkFactory());
        }

        if (ModList.get().isLoaded("emi")) {
            RefinedStorageApi.INSTANCE.addIngredientConverter(new EmiEnergyResourceModIngredientConverter());
        } else if (ModList.get().isLoaded("jei")) {
            RefinedStorageApi.INSTANCE.addIngredientConverter(new JEIRecipeModIngredientConverter());
        }
    }

    private void registerRegistries(final NewRegistryEvent event) {
        event.register(Types.TYPE_REGISTRY);
    }

    private void registerCreativeModeTab(final RegisterEvent e) {
        e.register(Registries.CREATIVE_MODE_TAB, helper -> helper.register(
            RefinedTypesUtil.CREATIVE_MODE_TAB,
            CreativeModeTab.builder()
                .title(RefinedTypesUtil.MOD)
                .icon(() -> Items.getEnergyStoragePart(EnergyStorageVariant.K_64).getDefaultInstance())
                .displayItems((params, output) -> {
                    for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
                        output.accept(Items.getEnergyStoragePart(variant));
                    }
                    for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
                        output.accept(Items.getEnergyStorageDisk(variant));
                    }
                    for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
                        output.accept(Items.getEnergyStorageBlock(variant));
                    }
                    if (isArsNouveauLoaded()) {
                        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                            output.accept(Items.getSourceStoragePart(variant));
                        }
                        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                            output.accept(Items.getSourceStorageDisk(variant));
                        }
                        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                            output.accept(Items.getSourceStorageBlock(variant));
                        }
                    }
                    if (isIndustrialForegoingSoulsLoaded()) {
                        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                            output.accept(Items.getSoulStoragePart(variant));
                        }
                        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                            output.accept(Items.getSoulStorageDisk(variant));
                        }
                        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                            output.accept(Items.getSoulStorageBlock(variant));
                        }
                    }
                })
                .build()
        ));
    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            com.refinedmods.refinedstorage.common.content.BlockEntities.INSTANCE.getInterface(),
            (be, side) -> new EnergyStorageInterface(be)
        );
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.registerNetworkNodeContainerProvider(event, BlockEntities.getEnergyStorageBlock(variant));
        }
        if (isArsNouveauLoaded()) {
            for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                this.registerNetworkNodeContainerProvider(event, BlockEntities.getSourceStorageBlock(variant));
            }
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            SoulUtil.registerCapability(event);
            for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                this.registerNetworkNodeContainerProvider(event, BlockEntities.getSoulStorageBlock(variant));
            }
        }
    }

    private void registerNetworkNodeContainerProvider(final RegisterCapabilitiesEvent event,
                                                      final BlockEntityType<? extends AbstractNetworkNodeContainerBlockEntity<?>> type) {
        event.registerBlockEntity(
            RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability(),
            type,
            (be, side) -> be.getContainerProvider()
        );
    }


    public static Config getConfig() {
        return CONFIG;
    }
}

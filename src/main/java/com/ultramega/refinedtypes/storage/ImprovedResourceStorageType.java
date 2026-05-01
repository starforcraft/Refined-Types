package com.ultramega.refinedtypes.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.AbstractProxyStorage;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.Storage;
import com.refinedmods.refinedstorage.api.storage.StorageImpl;
import com.refinedmods.refinedstorage.api.storage.limited.LimitedStorage;
import com.refinedmods.refinedstorage.api.storage.limited.LimitedStorageImpl;
import com.refinedmods.refinedstorage.api.storage.tracked.InMemoryTrackedStorageRepository;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedResource;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedStorage;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedStorageImpl;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedStorageRepository;
import com.refinedmods.refinedstorage.common.api.storage.PlayerActor;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageContents;
import com.refinedmods.refinedstorage.common.api.storage.StorageType;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.Nullable;

/**
 * {@link com.refinedmods.refinedstorage.common.storage.ResourceStorageType} but with creative (infinitely filled) support
 */
public class ImprovedResourceStorageType implements StorageType {
    private static final String DESERIALIZE_ERROR_MESSAGE = """
        Refined Storage could not load a resource in storage.
        This could be because the resource no longer exists after a mod update, or if the data format of the
        resource has changed. In any case, this is NOT caused by Refined Storage.
        Refined Storage will try to gracefully handle this problem and continue to load the storage data.
        The problematic resource might end up being removed from storage, or may no longer have any additional data
        associated with it.
        Error message:""";
    private static final Codec<StorageContents.Changed> CHANGED_CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("by").forGetter(StorageContents.Changed::by),
            Codec.LONG.fieldOf("at").forGetter(StorageContents.Changed::at)
        ).apply(instance, StorageContents.Changed::new));

    private final MapCodec<StorageContents> codec;
    private final Predicate<ResourceKey> valid;
    private final ResourceKey resource;
    private final long diskInterfaceTransferQuota;
    private final long diskInterfaceTransferQuotaWithStackUpgrade;

    public ImprovedResourceStorageType(final Codec<ResourceKey> resourceCodec,
                                       final Predicate<ResourceKey> valid,
                                       final ResourceKey resource,
                                       final long diskInterfaceTransferQuota,
                                       final long diskInterfaceTransferQuotaWithStackUpgrade) {
        this.valid = valid;
        this.resource = resource;
        this.diskInterfaceTransferQuota = diskInterfaceTransferQuota;
        this.diskInterfaceTransferQuotaWithStackUpgrade = diskInterfaceTransferQuotaWithStackUpgrade;

        final Codec<StorageContents.Stored> storedCodec = RecordCodecBuilder.create(instance -> instance.group(
            resourceCodec.fieldOf("resource").forGetter(StorageContents.Stored::resource),
            Codec.LONG.fieldOf("amount").forGetter(StorageContents.Stored::amount),
            Codec.optionalField("changed", CHANGED_CODEC, false).forGetter(StorageContents.Stored::changed)
        ).apply(instance, StorageContents.Stored::new));

        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.optionalField("capacity", Codec.LONG, false).forGetter(StorageContents::capacity),
            new com.refinedmods.refinedstorage.common.support.ErrorHandlingListCodec<>(storedCodec, DESERIALIZE_ERROR_MESSAGE)
                .fieldOf("resources").forGetter(StorageContents::stored)
        ).apply(instance, (capacity, stored) -> new StorageContents(this, capacity, stored)));
    }

    @Override
    public SerializableStorage create(@Nullable final Long capacity, final Runnable listener) {
        final boolean isCreative = capacity != null && capacity == -1;
        return this.createStorage(this.createEmptyStorageContents(capacity), isCreative, listener);
    }

    @Override
    public SerializableStorage create(final StorageContents contents, final Runnable listener) {
        final boolean isCreative = contents.capacity().isPresent() && contents.capacity().get() == -1;
        return this.createStorage(contents, isCreative, listener);
    }

    @Override
    public MapCodec<StorageContents> getCodec() {
        return this.codec;
    }

    @Override
    public boolean isAllowed(final ResourceKey resource) {
        return this.valid.test(resource);
    }

    @Override
    public long getDiskInterfaceTransferQuota(final boolean stackUpgrade) {
        if (stackUpgrade) {
            return this.diskInterfaceTransferQuotaWithStackUpgrade;
        }
        return this.diskInterfaceTransferQuota;
    }

    private SerializableStorage createStorage(final StorageContents contents, final boolean isCreative, final Runnable listener) {
        final TrackedStorageRepository trackingRepository = new InMemoryTrackedStorageRepository();
        final TrackedStorageImpl tracked = new TrackedStorageImpl(
            isCreative ? new CreativeStorageImpl(this.resource) : new StorageImpl(),
            trackingRepository,
            System::currentTimeMillis
        );
        final PlatformStorage storage = contents.capacity()
            .filter(capacity -> capacity != -1)
            .map(capacity -> {
                final LimitedStorageImpl limited = new LimitedStorageImpl(tracked, capacity);
                return (PlatformStorage) new LimitedPlatformStorage(limited, this, trackingRepository, listener);
            }).orElseGet(() -> new PlatformStorage(tracked, this, trackingRepository, listener));
        contents.stored().forEach(storage::load);
        return storage;
    }

    private StorageContents createEmptyStorageContents(@Nullable final Long capacity) {
        return new StorageContents(this, Optional.ofNullable(capacity), List.of());
    }

    /**
     * Exact copy of {@link com.refinedmods.refinedstorage.common.storage.PlatformStorage}
     */
    static class PlatformStorage extends AbstractProxyStorage implements SerializableStorage, TrackedStorage {
        private final StorageType type;
        private final TrackedStorageRepository trackingRepository;
        private final Runnable listener;

        PlatformStorage(final Storage delegate,
                        final StorageType type,
                        final TrackedStorageRepository trackingRepository,
                        final Runnable listener) {
            super(delegate);
            this.type = type;
            this.trackingRepository = trackingRepository;
            this.listener = listener;
        }

        void load(final StorageContents.Stored stored) {
            final ResourceKey resource = stored.resource();
            if (!this.type.isAllowed(resource)) {
                return;
            }
            super.insert(resource, stored.amount(), Action.EXECUTE, Actor.EMPTY);
            stored.changed().ifPresent(
                changed -> this.trackingRepository.update(resource, new PlayerActor(changed.by()), changed.at())
            );
        }

        @Override
        public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
            if (!this.type.isAllowed(resource)) {
                return 0;
            }
            final long extracted = super.extract(resource, amount, action, actor);
            if (extracted > 0 && action == Action.EXECUTE) {
                this.listener.run();
            }
            return extracted;
        }

        @Override
        public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
            if (!this.type.isAllowed(resource)) {
                return 0;
            }
            final long inserted = super.insert(resource, amount, action, actor);
            if (inserted > 0 && action == Action.EXECUTE) {
                this.listener.run();
            }
            return inserted;
        }

        @Override
        public Optional<TrackedResource> findTrackedResourceByActorType(final ResourceKey resource,
                                                                        final Class<? extends Actor> actorType) {
            return this.trackingRepository.findTrackedResourceByActorType(resource, actorType);
        }

        @Override
        public StorageType getType() {
            return this.type;
        }

        @Override
        public StorageContents toContents() {
            final Optional<Long> capacity = this instanceof LimitedStorage limitedStorage
                ? Optional.of(limitedStorage.getCapacity())
                : Optional.empty();
            final List<StorageContents.Stored> stored = this.getAll().stream()
                .map(storedResource -> new StorageContents.Stored(storedResource.resource(), storedResource.amount(),
                    this.toChanged(storedResource.resource())))
                .toList();
            return new StorageContents(this.type, capacity, stored);
        }

        private Optional<StorageContents.Changed> toChanged(final ResourceKey resourceAmount) {
            return this.findTrackedResourceByActorType(resourceAmount, PlayerActor.class)
                .map(tracked -> new StorageContents.Changed(tracked.getSourceName(), tracked.getTime()));
        }
    }

    /**
     * Exact copy of {@link com.refinedmods.refinedstorage.common.storage.LimitedPlatformStorage}
     */
    static class LimitedPlatformStorage extends PlatformStorage implements LimitedStorage {
        private final LimitedStorageImpl limitedStorage;

        LimitedPlatformStorage(final LimitedStorageImpl delegate,
                               final StorageType type,
                               final TrackedStorageRepository trackingRepository,
                               final Runnable listener) {
            super(delegate, type, trackingRepository, listener);
            this.limitedStorage = delegate;
        }

        @Override
        public long getCapacity() {
            return this.limitedStorage.getCapacity();
        }
    }
}

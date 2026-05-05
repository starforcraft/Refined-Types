package com.ultramega.refinedtypes.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
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
import com.refinedmods.refinedstorage.common.api.storage.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link com.refinedmods.refinedstorage.common.storage.SameTypeStorageType} but with creative (infinitely filled) support
 */
public class ImprovedSameTypeStorageType<T extends ResourceKey> implements StorageType {
    private final Codec<T> codec;
    private final Predicate<ResourceKey> valid;
    private final Function<ResourceKey, T> caster;
    private final ResourceKey resource;
    private final long diskInterfaceTransferQuota;
    private final long diskInterfaceTransferQuotaWithStackUpgrade;

    public ImprovedSameTypeStorageType(final Codec<T> codec,
                                       final Predicate<ResourceKey> valid,
                                       final Function<ResourceKey, T> caster,
                                       final ResourceKey resource,
                                       final long diskInterfaceTransferQuota,
                                       final long diskInterfaceTransferQuotaWithStackUpgrade) {
        this.codec = codec;
        this.valid = valid;
        this.caster = caster;
        this.resource = resource;
        this.diskInterfaceTransferQuota = diskInterfaceTransferQuota;
        this.diskInterfaceTransferQuotaWithStackUpgrade = diskInterfaceTransferQuotaWithStackUpgrade;
    }

    @Override
    public SerializableStorage create(@Nullable final Long capacity, final Runnable listener) {
        final boolean isCreative = capacity != null && capacity == -1;
        return this.createStorage(StorageCodecs.StorageData.empty(capacity), isCreative, listener);
    }

    @Override
    public MapCodec<SerializableStorage> getMapCodec(final Runnable listener) {
        return StorageCodecs.sameTypeStorageData(this.codec).xmap(
            storageData -> this.createStorage(storageData, storageData.capacity().isPresent() && storageData.capacity().get() == -1, listener),
            storage -> StorageCodecs.StorageData.ofSameTypeStorage(storage, this.valid, this.caster)
        );
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

    private SerializableStorage createStorage(final StorageCodecs.StorageData<T> data, final boolean isCreative, final Runnable listener) {
        final TrackedStorageRepository trackingRepository = new InMemoryTrackedStorageRepository();
        final TrackedStorageImpl tracked = new TrackedStorageImpl(
            isCreative ? new CreativeStorageImpl(this.resource) : new StorageImpl(),
            trackingRepository,
            System::currentTimeMillis
        );
        final PlatformStorage storage = data.capacity()
            .filter(capacity -> capacity != -1)
            .map(capacity -> {
                final LimitedStorageImpl limited = new LimitedStorageImpl(tracked, capacity);
                return (PlatformStorage) new LimitedPlatformStorage(limited, this, trackingRepository, listener);
            }).orElseGet(() -> new PlatformStorage(tracked, this, trackingRepository, listener));
        data.resources().forEach(storage::load);
        return storage;
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

        void load(final StorageCodecs.StorageResource<? extends ResourceKey> storageResource) {
            final ResourceKey resource = storageResource.resource();
            if (!this.type.isAllowed(resource)) {
                return;
            }
            super.insert(resource, storageResource.amount(), Action.EXECUTE, Actor.EMPTY);
            storageResource.changed().ifPresent(
                changed -> this.trackingRepository.update(resource, new PlayerActor(changed.changedBy()), changed.changedAt())
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
        public StorageType getType() {
            return this.type;
        }

        @Override
        public Optional<TrackedResource> findTrackedResourceByActorType(final ResourceKey resource,
                                                                        final Class<? extends Actor> actorType) {
            return this.trackingRepository.findTrackedResourceByActorType(resource, actorType);
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

    /**
     * Exact copy of {@link com.refinedmods.refinedstorage.common.storage.StorageCodecs} (except TRACKED_RESOURCE_STREAM_CODEC + TRACKED_RESOURCE_OPTIONAL_STREAM_CODEC)
     */
    static class StorageCodecs {
        private static final Codec<StorageChangedByAt> CHANGED_BY_AT_CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("changedBy").forGetter(StorageChangedByAt::changedBy),
                Codec.LONG.fieldOf("changedAt").forGetter(StorageChangedByAt::changedAt)
            ).apply(instance, StorageChangedByAt::new));

        private StorageCodecs() {
        }

        static <T extends ResourceKey> MapCodec<StorageData<T>> sameTypeStorageData(final Codec<T> resourceCodec) {
            final Codec<StorageResource<T>> storageResourceCodec = RecordCodecBuilder.create(instance -> instance.group(
                resourceCodec.fieldOf("resource").forGetter(StorageResource::resource),
                Codec.LONG.fieldOf("amount").forGetter(StorageResource::amount),
                Codec.optionalField("changed", CHANGED_BY_AT_CODEC, false).forGetter(StorageResource::changed)
            ).apply(instance, StorageResource::new));

            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.optionalField("capacity", Codec.LONG, false).forGetter(StorageData::capacity),
                new ErrorHandlingListCodec<>(storageResourceCodec).fieldOf("resources").forGetter(StorageData::resources)
            ).apply(instance, StorageData::new));
        }

        record StorageData<T extends ResourceKey>(Optional<Long> capacity, List<StorageResource<T>> resources) {
            static <T extends ResourceKey> StorageData<T> empty(@Nullable final Long capacity) {
                return new StorageData<>(Optional.ofNullable(capacity), List.of());
            }

            static <T extends ResourceKey> StorageData<T> ofSameTypeStorage(
                final Storage storage,
                final Predicate<ResourceKey> valid,
                final Function<ResourceKey, T> caster
            ) {
                final Optional<Long> capacity = storage instanceof LimitedStorage limitedStorage
                    ? Optional.of(limitedStorage.getCapacity())
                    : Optional.empty();
                final List<StorageResource<T>> resources = storage.getAll().stream()
                    .filter(resourceAmount -> valid.test(resourceAmount.resource()))
                    .map(resourceAmount -> getResource(storage, caster, resourceAmount))
                    .toList();
                return new StorageData<>(capacity, resources);
            }

            private static <T extends ResourceKey> StorageResource<T> getResource(
                final Storage storage,
                final Function<ResourceKey, T> caster,
                final ResourceAmount resourceAmount
            ) {
                return new StorageResource<>(
                    caster.apply(resourceAmount.resource()),
                    resourceAmount.amount(),
                    getChanged(storage, resourceAmount)
                );
            }

            private static Optional<StorageChangedByAt> getChanged(final Storage storage,
                                                                                 final ResourceAmount resourceAmount) {
                if (!(storage instanceof TrackedStorage trackedStorage)) {
                    return Optional.empty();
                }
                return trackedStorage.findTrackedResourceByActorType(resourceAmount.resource(), PlayerActor.class)
                    .map(StorageChangedByAt::ofTrackedResource);
            }
        }

        record StorageResource<T extends ResourceKey>(T resource, long amount, Optional<StorageChangedByAt> changed) {
        }

        record StorageChangedByAt(String changedBy, long changedAt) {
            private static StorageChangedByAt ofTrackedResource(final TrackedResource trackedResource) {
                return new StorageChangedByAt(trackedResource.getSourceName(), trackedResource.getTime());
            }
        }
    }

    /**
     * Exact copy of {@link com.refinedmods.refinedstorage.common.storage.ErrorHandlingListCodec}
     */
    static class ErrorHandlingListCodec<E> implements Codec<List<E>> {
        private static final String ERROR_MESSAGE = """
        Refined Storage could not load a resource in storage.
        This could be because the resource no longer exists after a mod update, or if the data format of the
        resource has changed. In any case, this is NOT caused by Refined Storage.
        Refined Storage will try to gracefully handle this problem and continue to load the storage data.
        The problematic resource might end up being removed from storage, or may no longer have any additional data
        associated with it.
        Error message:""";

        private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingListCodec.class);

        private final Codec<E> elementCodec;

        ErrorHandlingListCodec(final Codec<E> elementCodec) {
            this.elementCodec = elementCodec;
        }

        @Override
        public <T> DataResult<T> encode(final List<E> input, final DynamicOps<T> ops, final T prefix) {
            final ListBuilder<T> builder = ops.listBuilder();
            for (final E element : input) {
                builder.add(this.elementCodec.encodeStart(ops, element));
            }
            return builder.build(prefix);
        }

        @Override
        public <T> DataResult<Pair<List<E>, T>> decode(final DynamicOps<T> ops, final T input) {
            return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
                final DecoderState<T> decoder = new DecoderState<>(ops);
                stream.accept(decoder::accept);
                return decoder.build();
            });
        }

        @Override
        public String toString() {
            return "ErrorHandlingListCodec[" + this.elementCodec + ']';
        }

        private class DecoderState<T> {
            private final DynamicOps<T> ops;
            private final List<E> elements = new ArrayList<>();

            private DecoderState(final DynamicOps<T> ops) {
                this.ops = ops;
            }

            private void accept(final T value) {
                final DataResult<Pair<E, T>> elementResult = ErrorHandlingListCodec.this.elementCodec.decode(this.ops, value);
                elementResult.error().ifPresent(
                    error -> LOGGER.warn("{} {}", ERROR_MESSAGE, error.message())
                );
                elementResult.resultOrPartial().ifPresent(pair -> this.elements.add(pair.getFirst()));
            }

            private DataResult<Pair<List<E>, T>> build() {
                return DataResult.success(Pair.of(this.elements, this.ops.empty()));
            }
        }
    }
}

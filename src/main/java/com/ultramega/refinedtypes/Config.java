package com.ultramega.refinedtypes;

import net.neoforged.neoforge.common.ModConfigSpec;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslationKey;

public final class Config {
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    private final ModConfigSpec spec;

    private final EnergyStorageBlockEntry energyStorageBlock;
    private final SourceStorageBlockEntry sourceStorageBlock;
    private final SoulStorageBlockEntry soulStorageBlock;

    public Config() {
        this.energyStorageBlock = new EnergyStorageBlockEntry();
        this.sourceStorageBlock = new SourceStorageBlockEntry();
        this.soulStorageBlock = new SoulStorageBlockEntry();
        this.spec = this.builder.build();
    }

    private static String translationKey(final String value) {
        return createRefinedTypesTranslationKey("config", "option." + value);
    }

    public EnergyStorageBlockEntry getEnergyStorageBlock() {
        return this.energyStorageBlock;
    }

    public SourceStorageBlockEntry getSourceStorageBlock() {
        return this.sourceStorageBlock;
    }

    public SoulStorageBlockEntry getSoulStorageBlock() {
        return this.soulStorageBlock;
    }

    public ModConfigSpec getSpec() {
        return this.spec;
    }

    public class EnergyStorageBlockEntry {
        private final ModConfigSpec.LongValue k64EnergyUsage;
        private final ModConfigSpec.LongValue k256EnergyUsage;
        private final ModConfigSpec.LongValue k1024EnergyUsage;
        private final ModConfigSpec.LongValue k8192EnergyUsage;
        private final ModConfigSpec.LongValue k65536EnergyUsage;
        private final ModConfigSpec.LongValue k262144EnergyUsage;
        private final ModConfigSpec.LongValue k1048576EnergyUsage;
        private final ModConfigSpec.LongValue k8388608EnergyUsage;
        private final ModConfigSpec.LongValue infiniteEnergyUsage;

        EnergyStorageBlockEntry() {
            builder.translation(translationKey("energyStorageBlock")).push("energyStorageBlock");
            this.k64EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.64KEnergyUsage"))
                .defineInRange(
                    "64KEnergyUsage",
                    2,
                    0,
                    Long.MAX_VALUE
                );
            this.k256EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.256KEnergyUsage"))
                .defineInRange(
                    "256KEnergyUsage",
                    4,
                    0,
                    Long.MAX_VALUE
                );
            this.k1024EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.1024KEnergyUsage"))
                .defineInRange(
                    "1024KEnergyUsage",
                    6,
                    0,
                    Long.MAX_VALUE
                );
            this.k8192EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.8192KEnergyUsage"))
                .defineInRange(
                    "8192KEnergyUsage",
                    8,
                    0,
                    Long.MAX_VALUE
                );
            this.k65536EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.65536KEnergyUsage"))
                .defineInRange(
                    "65536KEnergyUsage",
                    10,
                    0,
                    Long.MAX_VALUE
                );
            this.k262144EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.262144KEnergyUsage"))
                .defineInRange(
                    "262144KEnergyUsage",
                    12,
                    0,
                    Long.MAX_VALUE
                );
            this.k1048576EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.1048576KEnergyUsage"))
                .defineInRange(
                    "1048576KEnergyUsage",
                    14,
                    0,
                    Long.MAX_VALUE
                );
            this.k8388608EnergyUsage = builder
                .translation(translationKey("energyStorageBlock.8388608KEnergyUsage"))
                .defineInRange(
                    "8388608KEnergyUsage",
                    16,
                    0,
                    Long.MAX_VALUE
                );
            this.infiniteEnergyUsage = builder
                .translation(translationKey("energyStorageBlock.infiniteEnergyUsage"))
                .defineInRange(
                    "infiniteEnergyUsage",
                    18,
                    0,
                    Long.MAX_VALUE
                );
            builder.pop();
        }

        public long get64KEnergyUsage() {
            return this.k64EnergyUsage.get();
        }

        public long get256KEnergyUsage() {
            return this.k256EnergyUsage.get();
        }

        public long get1024KEnergyUsage() {
            return this.k1024EnergyUsage.get();
        }

        public long get8192KEnergyUsage() {
            return this.k8192EnergyUsage.get();
        }

        public long get65536KEnergyUsage() {
            return this.k65536EnergyUsage.get();
        }

        public long get262144KEnergyUsage() {
            return this.k262144EnergyUsage.get();
        }

        public long get1048576KEnergyUsage() {
            return this.k1048576EnergyUsage.get();
        }

        public long get8388608KEnergyUsage() {
            return this.k8388608EnergyUsage.get();
        }

        public long getInfiniteEnergyUsage() {
            return this.infiniteEnergyUsage.get();
        }
    }

    public class SourceStorageBlockEntry {
        private final ModConfigSpec.LongValue b64SourceUsage;
        private final ModConfigSpec.LongValue b256SourceUsage;
        private final ModConfigSpec.LongValue b1024SourceUsage;
        private final ModConfigSpec.LongValue b8192SourceUsage;
        private final ModConfigSpec.LongValue b65536SourceUsage;
        private final ModConfigSpec.LongValue b262144SourceUsage;
        private final ModConfigSpec.LongValue b1048576SourceUsage;
        private final ModConfigSpec.LongValue b8388608SourceUsage;
        private final ModConfigSpec.LongValue infiniteSourceUsage;

        SourceStorageBlockEntry() {
            builder.translation(translationKey("sourceStorageBlock")).push("sourceStorageBlock");
            this.b64SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.64BSourceUsage"))
                .defineInRange(
                    "64BSourceUsage",
                    2,
                    0,
                    Long.MAX_VALUE
                );
            this.b256SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.256BSourceUsage"))
                .defineInRange(
                    "256BSourceUsage",
                    4,
                    0,
                    Long.MAX_VALUE
                );
            this.b1024SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.1024BSourceUsage"))
                .defineInRange(
                    "1024BSourceUsage",
                    6,
                    0,
                    Long.MAX_VALUE
                );
            this.b8192SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.8192BSourceUsage"))
                .defineInRange(
                    "8192BSourceUsage",
                    8,
                    0,
                    Long.MAX_VALUE
                );
            this.b65536SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.65536BSourceUsage"))
                .defineInRange(
                    "65536BSourceUsage",
                    10,
                    0,
                    Long.MAX_VALUE
                );
            this.b262144SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.262144BSourceUsage"))
                .defineInRange(
                    "262144BSourceUsage",
                    12,
                    0,
                    Long.MAX_VALUE
                );
            this.b1048576SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.1048576BSourceUsage"))
                .defineInRange(
                    "1048576BSourceUsage",
                    14,
                    0,
                    Long.MAX_VALUE
                );
            this.b8388608SourceUsage = builder
                .translation(translationKey("sourceStorageBlock.8388608BSourceUsage"))
                .defineInRange(
                    "8388608BSourceUsage",
                    16,
                    0,
                    Long.MAX_VALUE
                );
            this.infiniteSourceUsage = builder
                .translation(translationKey("sourceStorageBlock.infiniteSourceUsage"))
                .defineInRange(
                    "infiniteSourceUsage",
                    18,
                    0,
                    Long.MAX_VALUE
                );
            builder.pop();
        }

        public long get64BSourceUsage() {
            return this.b64SourceUsage.get();
        }

        public long get256BSourceUsage() {
            return this.b256SourceUsage.get();
        }

        public long get1024BSourceUsage() {
            return this.b1024SourceUsage.get();
        }

        public long get8192BSourceUsage() {
            return this.b8192SourceUsage.get();
        }

        public long get65536BSourceUsage() {
            return this.b65536SourceUsage.get();
        }

        public long get262144BSourceUsage() {
            return this.b262144SourceUsage.get();
        }

        public long get1048576BSourceUsage() {
            return this.b1048576SourceUsage.get();
        }

        public long get8388608BSourceUsage() {
            return this.b8388608SourceUsage.get();
        }

        public long getInfiniteSourceUsage() {
            return this.infiniteSourceUsage.get();
        }
    }

    public class SoulStorageBlockEntry {
        private final ModConfigSpec.LongValue k1EnergyUsage;
        private final ModConfigSpec.LongValue k8EnergyUsage;
        private final ModConfigSpec.LongValue k64EnergyUsage;
        private final ModConfigSpec.LongValue k512EnergyUsage;
        private final ModConfigSpec.LongValue k4096EnergyUsage;
        private final ModConfigSpec.LongValue k32768EnergyUsage;
        private final ModConfigSpec.LongValue k262144EnergyUsage;
        private final ModConfigSpec.LongValue k2097152EnergyUsage;
        private final ModConfigSpec.LongValue infiniteEnergyUsage;

        SoulStorageBlockEntry() {
            builder.translation(translationKey("soulStorageBlock")).push("soulStorageBlock");
            this.k1EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.1KEnergyUsage"))
                .defineInRange(
                    "1KEnergyUsage",
                    2,
                    0,
                    Long.MAX_VALUE
                );
            this.k8EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.8KEnergyUsage"))
                .defineInRange(
                    "8KEnergyUsage",
                    4,
                    0,
                    Long.MAX_VALUE
                );
            this.k64EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.64KEnergyUsage"))
                .defineInRange(
                    "64KEnergyUsage",
                    6,
                    0,
                    Long.MAX_VALUE
                );
            this.k512EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.512KEnergyUsage"))
                .defineInRange(
                    "512KEnergyUsage",
                    8,
                    0,
                    Long.MAX_VALUE
                );
            this.k4096EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.4096KEnergyUsage"))
                .defineInRange(
                    "4096KEnergyUsage",
                    10,
                    0,
                    Long.MAX_VALUE
                );
            this.k32768EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.32768KEnergyUsage"))
                .defineInRange(
                    "32768KEnergyUsage",
                    12,
                    0,
                    Long.MAX_VALUE
                );
            this.k262144EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.262144KEnergyUsage"))
                .defineInRange(
                    "262144KEnergyUsage",
                    14,
                    0,
                    Long.MAX_VALUE
                );
            this.k2097152EnergyUsage = builder
                .translation(translationKey("soulStorageBlock.2097152KEnergyUsage"))
                .defineInRange(
                    "2097152KEnergyUsage",
                    16,
                    0,
                    Long.MAX_VALUE
                );
            this.infiniteEnergyUsage = builder
                .translation(translationKey("soulStorageBlock.infiniteEnergyUsage"))
                .defineInRange(
                    "infiniteEnergyUsage",
                    18,
                    0,
                    Long.MAX_VALUE
                );
            builder.pop();
        }

        public long get1KEnergyUsage() {
            return this.k1EnergyUsage.get();
        }

        public long get8KEnergyUsage() {
            return this.k8EnergyUsage.get();
        }

        public long get64KEnergyUsage() {
            return this.k64EnergyUsage.get();
        }

        public long get512KEnergyUsage() {
            return this.k512EnergyUsage.get();
        }

        public long get4096KEnergyUsage() {
            return this.k4096EnergyUsage.get();
        }

        public long get32768KEnergyUsage() {
            return this.k32768EnergyUsage.get();
        }

        public long get262144KEnergyUsage() {
            return this.k262144EnergyUsage.get();
        }

        public long get2097152KEnergyUsage() {
            return this.k2097152EnergyUsage.get();
        }

        public long getInfiniteEnergyUsage() {
            return this.infiniteEnergyUsage.get();
        }
    }
}

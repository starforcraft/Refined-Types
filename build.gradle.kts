import org.gradle.jvm.tasks.Jar

plugins {
    id("refinedarchitect.root")
    id("refinedarchitect.neoforge")
    id("me.modmuss50.mod-publish-plugin") version "1.0.0"
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/refinedmods/refinedstorage2")
        credentials {
            username = "anything"
            password = "\u0067hp_oGjcDFCn8jeTzIj4Ke9pLoEVtpnZMP4VQgaX"
        }
    }
    maven {
        url = uri("https://modmaven.dev/")
        content {
            includeGroup("dev.technici4n")
        }
    }
    maven {
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "EMI"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        url = uri("https://maven.theillusivec4.top/")
        content {
            includeGroup("top.theillusivec4.curios")
        }
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroup("software.bernie.geckolib")
        }
    }
    maven {
        name = "TerraBlender"
        url = uri("https://maven.minecraftforge.net")
    }
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://cursemaven.com")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }
}

val modVersion: String by project

refinedarchitect {
    modId = "refinedtypes"
    version = modVersion
    neoForge()
    dataGeneration(project(":"))
}

group = "com.ultramega.refinedtypes"

base {
    archivesName.set("refined-types")
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude(".cache/")
        }
    }
}

val refinedstorageVersion: String by project
val grandpowerVersion: String by project
val arsNouveauVersion: String by project
val industrialForegoingVersion: String by project
val titaniumVersion: String by project
val refinedstorageJeiIntegrationVersion: String by project
val refinedstorageEmiIntegrationVersion: String by project
val minecraftVersion: String by project
val jeiVersion: String by project
val emiVersion: String by project

dependencies {
    api("com.refinedmods.refinedstorage:refinedstorage-neoforge:${refinedstorageVersion}")
    api("dev.technici4n:GrandPower:${grandpowerVersion}")
    jarJar("dev.technici4n:GrandPower:${grandpowerVersion}")

    implementation("com.hollingsworth.ars_nouveau:ars_nouveau-${minecraftVersion}:${arsNouveauVersion}")
    implementation("com.buuz135:industrialforegoing:1.21-${industrialForegoingVersion}")
    implementation("curse.maven:industrial-foregoing-souls-904394:6235883")
    implementation("com.hrznstudio:titanium:1.21-${titaniumVersion}")

    runtimeOnly("com.refinedmods.refinedstorage:refinedstorage-jei-integration-neoforge:${refinedstorageJeiIntegrationVersion}")
    // runtimeOnly("com.refinedmods.refinedstorage:refinedstorage-emi-integration-neoforge:${refinedstorageEmiIntegrationVersion}")

    runtimeOnly("mezz.jei:jei-${minecraftVersion}-neoforge:${jeiVersion}")
    compileOnlyApi("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}")
    testCompileOnly("mezz.jei:jei-${minecraftVersion}-common:${jeiVersion}")
    compileOnlyApi("mezz.jei:jei-${minecraftVersion}-neoforge-api:${jeiVersion}")
    // runtimeOnly("dev.emi:emi-neoforge:${emiVersion}")
    compileOnlyApi("dev.emi:emi-neoforge:${emiVersion}")
}

val currentChangelog: String by project

publishMods {
    changelog = currentChangelog
    type = STABLE
    modLoaders.add("neoforge")
    file.set(tasks.named<Jar>("jar").flatMap { it.archiveFile })

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        projectId = "1327983"
        minecraftVersions.add(minecraftVersion)
        changelogType = "html"
        displayName = file.map { it.asFile.name }
        requires("refined-storage")
        optional("ars-nouveau", "industrial-foregoing-souls")
    }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = "WvQIise1"
        minecraftVersions.add(minecraftVersion)
        requires("refined-storage")
        optional("ars-nouveau", "industrial-foregoing-souls")
    }
}


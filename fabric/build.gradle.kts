plugins {
    `multiloader-loader`
    id("fabric-loom") version "1.13-SNAPSHOT"
    kotlin("jvm") version "2.2.20"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

stonecutter {

}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${commonMod.mc}:$parchmentVersion@zip")
        }
    })

    modImplementation("net.fabricmc:fabric-loader:${commonMod.dep("fabric_loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.dep("fabric_api")}+${commonMod.mc}")

    // Dependencies
    modImplementation("com.terraformersmc:modmenu:${commonMod.dep("modmenu")}")
}

loom {
    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            runDir = "run/client"
            ideConfigGenerated(true)
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            runDir = "run/server"
            ideConfigGenerated(true)
        }
    }
}
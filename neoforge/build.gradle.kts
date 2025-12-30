plugins {
    `multiloader-loader`
    id("net.neoforged.moddev") version "2.0.107"
    kotlin("jvm") version "2.2.20"
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22"
}

neoForge {
    enable {
        version = commonMod.dep("neoforge")
    }
}

dependencies {
}

neoForge {
    runs {
        register("client") {
            client()
            gameDirectory = project.file("run/client")
            ideName = "NeoForge Client (${project.path})"
        }
        register("server") {
            server()
            gameDirectory = project.file("run/server")
            ideName = "NeoForge Server (${project.path})"
        }
    }

    parchment {
        commonMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = commonMod.mc
        }
    }

    mods {
        register(commonMod.id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}
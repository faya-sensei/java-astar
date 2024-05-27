plugins {
    application
}

group = "org.faya.sensei"
version = "1.0-SNAPSHOT"

application {
    mainClass = "org.faya.sensei.Main"
}

val lwjglNatives = Pair(System.getProperty("os.name")!!, System.getProperty("os.arch")!!).let { (name, arch) ->
    when {
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } -> {
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"
        }
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } -> {
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        }
        arrayOf("Windows").any { name.startsWith(it) } -> {
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else
                "natives-windows-x86"
        }
        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:3.3.+"))
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-opengl")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)

    testImplementation(platform("org.junit:junit-bom:5.10.+"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.reflections:reflections:0.10.+")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
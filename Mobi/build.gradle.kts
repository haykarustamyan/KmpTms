import dev.icerock.gradle.MRVisibility
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework


plugins {// Mobi module
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.mokoResources)
}


kotlin {
    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlin.ExperimentalUnsignedTypes,kotlin.RequiresOptIn",
                        "-Xexpect-actual-classes"
                    )
                }
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    applyDefaultHierarchyTemplate()

    val iosArm64 = iosArm64()
    val iosX64 = iosX64()
    val iosSimulatorArm64 = iosSimulatorArm64()

    val xcframeworkName = "Shared"
    val xcFramework = XCFramework(xcframeworkName)
    configure(listOf(iosArm64, iosX64, iosSimulatorArm64)) {
        binaries {
            framework {
                isStatic = true
                baseName = xcframeworkName
                binaryOption("bundleId", "com.mobiclocks.kiosk.shared")
                xcFramework.add(this)
                export(libs.mokoLibResources)
                export(libs.mokoGraphics)

            }
        }
    }



//    val xcframeworkName = "Shared"
//    val xcf = XCFramework(xcframeworkName)
//
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = xcframeworkName
//            binaryOption("bundleId", "com.mobiclocks.kiosk.shared")
//            xcf.add(this)
//            isStatic = true
//
//            export(libs.mokoLibResources)
//            export(libs.mokoGraphics)
//        }
//    }
//
//    tasks.register("buildIosResources") {
////        dependsOn(":Mobi:assembleSharedXCFramework", ":Mobi:iosSimulatorArm64AggregateResources", ":Mobi:iosArm64AggregateResources")
//        dependsOn(":Mobi:assembleSharedXCFramework")
////        finalizedBy("prepareIosResources")
//    }


    tasks.register("copyBundleResources") {
        dependsOn("assembleSharedReleaseXCFramework")

        doLast {
            val xcFrameworkBundlePath = "$buildDir/XCFrameworks/Release/${xcframeworkName}.xcframework"

            // Iterate over all iOS targets and get their framework paths
            val frameworks = listOf(iosArm64, iosX64, iosSimulatorArm64).map { target ->
                kotlin.targets.getByName<KotlinNativeTarget>(target.name)
                    .binaries
                    .withType<Framework>()
                    .first { it.buildType == NativeBuildType.RELEASE }
                    .outputDirectory
            }

            // Copy .bundle resources
            frameworks.forEach { frameworkDir ->
                frameworkDir.listFiles()
                    ?.filter { it.name.endsWith(".bundle") }
                    ?.forEach { bundleFile ->
                        logger.lifecycle("📦 Copying bundle: ${bundleFile.name} to $xcFrameworkBundlePath")
                        project.copy {
                            from(bundleFile)
                            into("$xcFrameworkBundlePath/${bundleFile.name}")
                        }
                    }
            }
        }
    }

    tasks.named("assembleSharedReleaseXCFramework").configure {
        finalizedBy("copyBundleResources")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.websockets)

            implementation("io.insert-koin:koin-compose:1.1.2")

            api(libs.mokoLibResources)
            api(libs.mokoCompose)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

//        val iosX64Main by getting {
//            dependsOn(iosMain.get())
//        }
//        val iosArm64Main by getting {
//            dependsOn(iosMain.get())
//        }
//        val iosSimulatorArm64Main by getting {
//            dependsOn(iosMain.get())
//        }
    }
}

android {
    namespace = "com.mobiclocks.kiosk"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

//compose.resources {
//    publicResClass = true
//    packageOfResClass = "com.mobiclocks.kiosk.resources"
//    generateResClass = always
//}

dependencies {
    debugImplementation(compose.uiTooling)
    commonMainApi(libs.mokoCompose)
}


multiplatformResources {
    resourcesPackage.set("kiosk.sharedresources")
    resourcesClassName.set("SharedRes")
    resourcesVisibility.set(MRVisibility.Public)
    iosBaseLocalizationRegion.set("en")
    iosMinimalDeploymentTarget.set("15.0")

    configureCopyXCFrameworkResources("Shared")
}
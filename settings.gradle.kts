pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("mapbox")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1Ijoic2ZhY2kiLCJhIjoiY20yd2FoZHZzMDRsNjJsc2dvM2l6OWlkbyJ9.2IBPu4kuHGAjMUJONaj38Q"
            }
        }
    }
}

rootProject.name = "Memazo"
include(":app")
 
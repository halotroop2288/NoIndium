# No Indium?

<img src="src/main/resources/assets/noindium/icon.png" align="right" width="125" alt="logo"/>

[![Fabric API](https://images2.imgbox.com/8e/38/bfInI5qv_o.png)](https://modrinth.com/mod/fabric-api)

No Indium? is a small, jij-able mod attempting to minimize the usage of Sodium without Indium on Fabric.

## Features

- Warns users using Sodium without Indium
- Warns users using Optifabric
- Additional warnings can be added by other mods
- Configurable (disable warnings, disable "Proceed Anyway" button).

## Adding to your project

You may check the mod's current version on [Github][latest].
[![](https://img.shields.io/github/downloads/halotroop2288/NoIndium/latest/total?label=Latest%20Version)][latest]

```kts
repositories {
	ivy("https://github.com/halotroop2288") {
		name = "NoIndium"
		artifactPattern("[module]/releases/download/[revision]/[module]-[revision]+MC${libs.versions.minecraft.get()}(-[classifier])(.jar)")
		metadataSources { artifact() }
	}
}

dependencies {
    modImplementation("me.luligabi:NoIndium:[VERSION]")
    include("me.luligabi:NoIndium:[VERSION]")
}
```

[latest]:https://github.com/halotroop2288/NoIndium/releases/latest

# LayeredStructures ![Static Badge](https://img.shields.io/badge/Plugin-green) ![Static Badge](https://img.shields.io/badge/Library-blue) ![Static Badge](https://img.shields.io/badge/Easy%20to%20use-red)

Layered Structures is a plugin and library for minecraft servers that will allow you to create, save and generate layered block structures. Using the API you will be able to allow the automation of generated structures relative to given positions.

The main idea is, not the generation of structures, but the generation of structures in a fragmented way over time. Which has great potential with regards to creating construction animations.


<p align="center">
  <img src="https://github.com/RedRiotTank/LayeredStructures/assets/64831934/1d8fd7e5-f551-4aed-b008-ac0f9e0f1b17" alt="drake">
</p>

## What does Layered block structure means? 

With this plugin you can define a structure of blocks as in the image, selecting which blocks you want to be built at the moment, then you can call the generate command or the API if you are a developer, to invoke the structure choosing the delay that you want there to be between each layer.

In this example you can see how

- First you build the redstone lamps

- Second redstone blocks

- Third place lower leaves

- Fourth place Intermediate leaves.

- Fifth place upper leaves

<p align="center">
  <img src="https://github.com/RedRiotTank/LayeredStructures/assets/64831934/6925a427-9402-4345-9e0c-0d002bd8a38c" alt="drake">
</p>

## Plugin Installing

To install LayeredStructures on your Minecraft server you just have to copy the "LayeredStructures-version.jar" file to your "/plugins" directory. You do not have to do anything else. Once you run the server with the plugin inside for the first time, it will generate a LayeredStructures/ folder where you can find all the exported LayeredStructures.

## How to use the plugin (example)

### Start a new layered procedure

To initialize the procedure and create the permanent save file (.json)
```
/ls startSave initFile direction
```
If direction is not relevant in your structure, just take any of them.

### Define the cuboid
You will have to define a cube which will establish the dimensions that the structure will occupy.

Stand in the bottom corner and execute the command.
```
/ls startSave c1
```
Go to the opposite top corner and cupboard the second corner to define the cuboid.
```
/ls startSave c2
```
**Note:** remember that the position you store is the one of your feet, so be careful with the limits.

### Build and save layers
Now you can start building. Iteratively build the layer and when you're done, run the command to save it. Blocks will be removed as you build for optimization reasons. You will always have the previous layer to be able to guide you correctly with what you did before.

```
/ls saveLayer
```

### Export

Finally you export the process to store it in permanent memory
```
/ls exportProcedure name
```

### Generate the structures
Now, you can generate the structure. You have to specify the direction, the delay (how many ticks you want the command to wait until start) and tickCounter (delay between each layer)
```
/ls generate filename direction delay tickCounter
```
**Note:** Remember a tick is 0.05 seconds. 

### How to use the pluginn video example


https://github.com/RedRiotTank/LayeredStructures/assets/64831934/bc554d6d-3f6e-4b0a-8d02-74a67ff47bc4

## About permissions and commands

Although this plugin is not geared towards the generic server user and is intended for administrators, you still have a permission to add if you want that let you use the whole plugin:

```
layeredstructures.use
```

The command list is the following one: 

- /ls startSave initFile direction: Initializes the process and creates the save file specifying which direction the structure is initially looking at
- /ls startSave c1: Select the location of the player's feet as the first corner of the cube
-/ls startSave c2: Select the location of the player's feet as the second corner of the cube
- /ls saveLayer: Saves a new layer of blocks.
- /ls exportProcedure filena: Permanently stores the structure with the name you specify.
- /ls generate filename direction delay tickCounter: Builds the structure, facing the specified direction, waiting for "delay" ticks to start building and with a difference of "tickCounter" ticks between layers

## Library

You can use LayeredStructures as a library in your own plugin, which will allow you to create amazing animations. To do this you will simply have to install the .jar file in your local maven repository.
```bash
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
```

Then add the dependency to your pom.xml
```xml
<dependency>
    <groupId>htt</groupId>
    <artifactId>LayeredStructures</artifactId>
    <version>1.0</version>
</dependency>
```

It's important to initialize the dependency on your onEnable()

```java
public void onEnable(){  
  LayeredStructuresAPI.initialize(Bukkit.getPluginManager().getPlugin("LayeredStructures"));
}
```

Now you are ready to generate buildings, with the API, here you have an example:
```java
public void generate(){  
  LayeredStructuresAPI.generateLayeredStructure("iceDrake", player.getLocation(),"north",0,3);
}
```

## Warranty and License ![Static Badge](https://img.shields.io/badge/License-GNU%20v3.0-green)

This project uses the GNU General Public License v3.0, if you have any questions about it you can consult the LICENSE file, I remember that this software runs without any type of guarantee and could contain bad practices or even errors.


bukkit Plugins
==============

This set contains some *(useful?)* single-purpose bukkit plugins.

Book Publisher (`bookpublish`)
--------------

Writes `Material.WRITTEN_BOOK` to Markdown-formatted files.

Chunk Generators (`generators`)
----------------

This generator plugin will add the following chunk generators to your bukkit server, each with different purposes.

*   `void`

    Generates empty chunks. Only exception is a single bedrock block that is located at 0,0 at sea level.

*   `skygrid`

    Generates chunks with random blocks placed in a 4&times;4&times;4 grid. Makes a good challenge.

Maze Generator (`mazegen`)
--------------

The *maze generator* will provide functionality that generates random mazes with multiple floors (optional) when a player enters a defined area.

.PNG Map Renderer (`pngmaps`)
-----------------

The *.png map renderer* will look for .png files in a specific folder and render that to the map ID that it was given. You can create empty template files with commands.

Replacing the files will almost instantly show the changes to the user, so you can easily write plugins that generate dynamic images instead of drawing them manually.

We also have some plugins that draw dynamic images:

### Player of the week (`pngmaps_potw`)

Draws a map that shows the total uptime and avatar of the player that played the most in the current calendar week.

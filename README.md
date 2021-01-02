# SuperNick

Super nick is an easy to use nick plugin for spigot version 1.8.9. Here you will find a short introduction to the use of the API.
<br>
<br>
Conntribute to the project here. <br>
Report issues here. <br>
Read the License here. <br>

## Setting the plugin up
> Remember: Since this plugin is using version specific packets, it can only run on Spigot version 1.8.8 and 1.8.9 

To use the plugin you need a `MySQL` database in which players and their data can be stored. Have your access data ready to enter them in the `mysql.yml` afterwards.

Put the SuperNick.jar file into your `plugins` folder. Now you just have to start your server.

After the first start of the plugin, a file named `mysql.yml` is created in the folder `plugins/SuperNick/`. Here you have to enter your MySQL access data and then restart the server.

Even if a few names have already been entered, you should definitely enter more nicknames in config.yml. The more, the better. The plugin is now ready and can be used.

## Permissions
/nick - `nick.use` <br>
/autonick - `nick.use` <br>
/checknick - `nick.check` <br>

## API
### Adding the library

To get started, the plugin must be added to the project like any other library. In intellij this can be done under
`File > Project Structure > Libraries > New Project Library > Java`.
Now you just want to select the `.jar` file and add it as a library.

### Using the API

The prefix can be accessed via the main class `SuperNick`. All other operations are handled via the [NickManager](https://github.com/chris23lngr/SuperNick/blob/main/src/main/java/de/z1up/supernick/nick/NickManager.java). Operations related to the MySQL database are performed in the [NickWrapper](https://github.com/chris23lngr/SuperNick/blob/main/src/main/java/de/z1up/supernick/nick/NickWrapper.java), which can also only be accessed via the manager.

The various methods can be accessed via the static instance `instance` in the NickManager.
<br>
```java
@EventHandler(priority = EventPriority.HIGH)
    public void onCall(final PlayerJoinEvent event) {

        Player player = event.getPlayer();
        NickPlayer nickPlayer = NickManager.instance.getNickWrapper().getNickPlayer(player.getUniqueId());
        
    }
```


## Setting the plugin up
#### Here is an explanation of how to set up the plugin

<br>

> Remember: Since this plugin is using version specific packets, it can only run on Spigot version 1.8.8 and 1.8.9 

<br>

To use the plugin you need a MySQL database in which players and their data can be stored. Have your access data ready to enter them in the `mysql.yml` afterwards.

<br>

1. Put the SuperNick.jar file into your `plugins` folder. 
2. Start your server.
3. Enter your MySQL access data in `plugins/SuperNick/mysql.yml`
4. Restart your server

<br>

The nicknames are stored in config.yml. Even if a few names are already specified here, you should definitely enter more. This is important so that the other players cannot remember which name belongs to a nicked player.
> Note that these names must really exist. If they do not, errors will occur with the plugin.

<br>

###### Permissions

<br>

`nick.use` - Use The command /nick, /unnick and /renick <br>
`nick.autonick` - Use command /autonick <br>
`nick.check` - Use command /checknick

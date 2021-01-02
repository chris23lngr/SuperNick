## API
#### Here you will find a short introduction to the use of the SuperNick API.

<br>

###### Adding the library

To get started, the plugin must be added to the project like any other library. In intellij this can be done under
`File > Project Structure > Libraries > New Project Library > Java`.
Now you just want to select the `.jar` file and add it as a library.

<br>

###### API Structure

The prefix of the plugin can be accessed via the main class `SuperNick`. To do this, the instance that is initialised when the plugin is enabeld must be accessed.
``SuperNick.getInstance().getPrefix();``
<br>
<br>
The [NickManager](https://github.com/chris23lngr/SuperNick/blob/main/src/main/java/de/z1up/supernick/nick/NickManager.java) takes care of the nicknames and nicking players. This class can also be used to access the [NickWrapper](https://github.com/chris23lngr/SuperNick/blob/main/src/main/java/de/z1up/supernick/nick/NickWrapper.java).
<br>
<br>
The NickWrapper performs all operations that rely on the database and the mysql connection. The access succeeds via `NickManager.instance.getNickWrapper()#...`.
<br>
<br>
The API also contains a `Reflects` and a `UUIDFetcher` class. 

<br>

###### Usage Examples

<br>

> Check if a User is registered to the database
```java
if(NickManager.instance.getNickWrapper().isRegistered(uuid)) {
    //...
}
```

<br>

> Get a NickPlayer from the database
```java
NickPlayer nickPlayer = NickManager.instance.getNickWrapper().getNickPlayer(uuid);
```

<br>

> Update the autonick settings of a player
```java
nickPlayer.setAutoNick(true);
nickPlayer.update();
```

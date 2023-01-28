# GUIs

This document describes how to create a GUI in general in this project.

## Window

### Definition

Each frame of a gui is called a ``window``. 
To define a window, you must define a class that extends ``BaseGui`` and 
implement the ``init()`` and ``build()`` methods as the following:

```java
public class ExampleClass extends BaseGui {

    @Override
    public void init(Language lang) {
        super.instantiate(lang, Message.Gui.EXAMPLE_TITLE, 3);
    }

    @Override
    public void build() {
    }
}
```

In the ``init()`` method, you must call the ``instantiate()`` method of the
super class. This method takes three parameters: the language, the title of the
window and the number of rows of the window. The message must be an 
Enum that the ``BaseMessageType``.

The ``build()`` method is used to build the window. It is called on every update
and when the window is opened.

### Caller

The shown GUI can be opened by calling it from anywhere:

```java
GuiManager.entryGui(ExampleWindow.class, player);
```

The ``build()`` method defines every component seen in the window. 

### Window methods

The 
following methods are available to modify the window:

```java
public <T extends GuiItem> void addItem(
        Class<T> guiItem, 
        int row, 
        int column, 
        Object... args
)
```

This method adds a (normal) button to the window. The first parameter is the
class of the button wich needs to extend ``GuiItem``. The second and 
third parameter are the row and column of the button. After that, you can pass
any number of arguments to the button class, which can be used there.

```java
public <T extends AbstractGuiItem> void addAbstractItem(
        Class<T> guiItem, 
        int row, 
        int column, 
        Serializable identifier, 
        Object... args
)
```

This method adds an abstract button (described later) to the window. In
addition to the normal button, you need to pass an identifier to the button.

```java
public KVStore getKvStore()
```

This method returns the ``KVStore`` of the window. The ``KVStore`` is used
to store data throughout the GUI for each player.

### Events

To listen to events, you need to override the 
``onClose(Player player, InventoryCloseEvent event)`` or 
``onClick(Player player, InventoryClickEvent event, GuiItem handledItem)`` method.

The ``onClose()`` method is called when the window is closed. The ``onClick()``
method is invoked when a button is clicked.

## Buttons

There are two types of buttons: normal buttons and abstract buttons. A normal
button is instantiated once internally. The arguments passed to a normal
button are always the same for the same class and player. An abstract button
will be instantiated for each unique identifier passed in the 
``addAbstractItem()``.

### Definition

A normal button can be defined as the following:

```java
public class ExampleButton extends GuiItem {
    @Override
    public void onClick(Player p) {

    }

    @Override
    public void build() {

    }
}
```

An abstract button needs to be defined as following:

```java
public class AbstractExampleButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {}

    @Override
    public void build() {
    }
}
```

The ``onClick()`` method is called when the button is clicked. The ``build()``
method is called on every update and when the parent window gets opened.

### Methods of button

Right at the beginning of the ``build()`` method, you need to call one of the
method that defines the button's item type. The following
methods are available:

```java
public void setItemMaterial(Material mat)
```

This method sets the button material.

```java
setItemPlayerHead(String playerName)
```

Sets the item to a player head. The parameter is the name of the player. The
method currently only works on players, that have been on the server since
it uses ``OfflinePlayer``.

```java
setItemHead(String headId)
```

Sets the item to a head. The parameter is the head id from the
[Head Database](https://minecraft-heads.com/).
Obviously, HeadDatabase must be installed on the server.

There are a few other methods, that can be used on items:

```java
public void setDisplayName(Component displayName)
public void setDisplayName(String displayName)
```

Sets the display name of the item.

```java
public void addLore(Component lore)
public void addLore(String lore)
public void addLore(BaseMessageType loreTemplate, String... args)
```

Adds a line to the lore of the item.

```java
public void prompt(Object identifier, String line1, String line2, String line3)
public void prompt(PromptType type, Object identifier)
```

Prompts the player with an input field. The identifier is used to associate the
prompt to a player. You can specify the three lines of the prompt or use a
``PromptType``, for example ``PromptType.INTEGER``

```java
public @Nullable String getPrompt(Object identifier)
```

Get the prompt for the identifier. Returns ``null`` if the prompt does not exist.

```java
public KVStore getKvStore()
```

This method returns the ``KVStore`` of the window. The ``KVStore`` is used
to store data throughout the GUI for each player.

## General actions

### Navigation

There is a stack-based navigation system. You start a new session by calling
``GuiManager.entryGui()``. The new GUI gets pushed to the stack. When you close
the GUI, the last GUI will be pushed off the stack.

To push a new GUI on the stack, you can call: ``GuiManager.navigate()``

```java
public static <T extends BaseGui> void navigate(
        Class<T> gui, 
        Player p, 
        Object... args
)
```

The first parameter is the GUI class. The second parameter is the player
to open the GUI for. The third parameter is an optional array of arguments,
that can be used in the GUI.

To pop the last GUI from the stack, you can call: ``GuiManager.back(Player p)``

You can reload/rebuild the current GUI by calling: ``GuiManager.rebuild(Player p)``


## Examples

Just take a look at the existing GUIs in the project: 
[GUI Package](../src/main/java/de/paradubsch/paradubschmanager/gui)

- Simple normal button: [BackButton.java](../src/main/java/de/paradubsch/paradubschmanager/gui/items/BackButton.java)
- Simple abstract button: [BazaarItemButton.java](../src/main/java/de/paradubsch/paradubschmanager/gui/items/BazaarItemButton.java)
- Simple GUI: [RtpGui.java](../src/main/java/de/paradubsch/paradubschmanager/gui/window/RtpGui.java)
- Use of ``onClick()``, ``onClose()``: [BackpackGui.java](../src/main/java/de/paradubsch/paradubschmanager/gui/window/BackpackGui.java)
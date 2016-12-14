# **Chrone**
> also known as Chronological Clone

![Chrone](https://raw.githubusercontent.com/blead/chrone/master/menu_screen.png)

_Chrone_ is a proof-of-concept minimalistic platformer game aimed to be highly extensible by design.
![Menu Screen][menu_screen.png] menu screen image here

____

# **Overview**
_Chrone_ is a puzzle platformer with some key mechanics:

## Keyboard Controls
All functions in the game are controlled through unique keyboard inputs. This includes both user interface control and character control during gameplay.

### controls
These controls can be shown in-game as well by pressing `H`.
  | Key | Action |
  | :-: | :----: |
  | _Arrow Keys_ | move the playable character |
  | `A` | create an _Anchor_ |
  | `S` | create a _Static Chrone_ |
  | `S` | create a _Dynamic Chrone_ |
  | `R` | restart a _level_ |
  | `O` | open a new _level |
  | `H` | toggle in-game help message |
  | `Esc` | return to main menu |

## Customizable Tile-based Level
Each object in a _level_ is represented as a single character in an array of strings. This array is called a _tilemap_.
_Levels_ are loaded externally from JSON-encoded files. Anyone can create a new _level_ with customized _tilemap_, gravity values, background image, and music to play and share with others.

### tiles
There are many tile types available. Furthermore, these tiles are easily extensible, allowing future developments of the project.

- ### Player
  The playable character
- ### Block
  A simple solid tile. Can be used to create a wall or a platform.
- ### Switch Block
  A _Block_ marked with a small colored circle. _Switch Blocks_ can be triggered from any direction by contact. Once triggered, _Door Blocks_ with the same color will open.
- ### Door Block
  A _Block_ marked with a colored diamond shape. _Door blocks_ will open only if a _Switch Block_ with the same color is triggered. Once open, it can be passed through.
- ### Info Block
  A _Block_ marked with a small `i` character. While the player is in contact with an _Info Block_, a message associated with the block will be displayed on the screen.
- ### Goal Block
  The objective of a level. When the player is in contact with a _Goal Block_, a congratulation message will be displayed on the screen, and an audio clip indicating a victory will play as well.

### level file structure
A _level_ file is a JSON-encoded text file with the following fields:

- ### `data`
  An array of strings representing the _tilemap_. Tile types are represented by the following set of characters:
    | Tile | Character |
    | :--: | :-------: |
    | _none_ | `0` |
    | Block | `1` |
    | Player | `2` |
    | Info Block | `3` |
    | Goal Block | `4` |
    | Switch Block | `a` - `z`* |
   | Door Block | `A` - `Z`* |
  _(*) Switch Blocks and Door Blocks with matching character have the same associated color, therefore, there can be 26 different sets of Switch Blocks/Door Blocks._
- ### `messages`
  An array of strings consisting of messages displayed by _Info Blocks_. If there are `n` _Info Blocks_ in the _tilemap_, there must be at least `n` messages in this array. If there are more messages than the number of _Info Blocks_, the remaining messages are ignored. Messages are associated with _Info Blocks_ by their positioning order in the _tilemap_, from left to right and top to bottom.
- ### `width` (optional)
  A floating point value indicating the width of the _level_. Each tile is 60 units wide. If provided, the game will try to render the _level_ according to this value. If the actual width from `data` is larger, the overflowing part of the _tilemap_ will be ignored.
- ### `height` (optional)
  A floating point value indicating the height of the _level_. Each tile is 60 units high. If provided, the game will try to render the _level_ according to this value. If the actual width from `data` is larger, the overflowing part of the _tilemap_ will be ignored.
- ### `gravityX` (optional)
  A floating point value indicating the gravity of the _level_ in the horizontal axis. If not provided, defaults to `0`.
- ### `gravityY` (optional)
  A floating point value indicating the gravity of the _level_ in the vertical axis. If not provided, defaults to `0`.
- ### `background` (optional)
  A string representing the path to the background image of the _level_. If not provided or the path given is incorrect, the game will use the default background instead.
- ### `music` (optional)
  A string indicating the path to the background music of the _level_. If not provided or the path given is incorrect, the game will use the default background music instead.

### example level file structure
Note that the following file will create a _level_ with a single empty tile which is pretty much unplayable.
```
{"data":["01234","aabAB"],"messages":["Hello world!"],"width":60.0,"height":60.0,"gravityX":1.0,"gravityY":2.0}
```

## Chrones
_Chrones_ are copies of the playable character. They can trigger Switch Blocks, Info Blocks, and Goal Blocks like the player. Chrones will not collide with the player. There are currently 2 types of Chrones:

- ### Static Chrone
  _Static Chrones_ are red-colored Chrones. When created, they will stay in-place for an indefinite amount of time. They are unaffected by gravity as well.

- ### Dynamic Chrone
  _Dynamic Chrones_ are green-colored Chrones with the ability to move. After being created, they will move according to the recorded inputs of the player for up to 3 seconds and then vanish. Unlike _Static Chrones_, _Dynamic Chrones_ are affected by gravity.

### Anchor
An _Anchor_ is a gray-colored diamond shape. To create _Chrones_ an _Anchor_ must be created first. Once created, an _Anchor_ will record inputs of the player for up to 3 seconds. If the player chooses to create a _Chrone_, the _Chrone_ will be created at the position of the _Anchor_. If the _Chrone_ being created is a _Dynamic Chrone_, it will move according to the recorded inputs as mentioned above.

____

# **Implementation Details**
_Chrone_ implements _Entity-component-system_ architectural pattern.
(For more information, refer to https://en.wikipedia.org/wiki/Entity%E2%80%93component%E2%80%93system)

## Class Structure
Each object in the game is represented as an `Entity`. An `Entity` is a container of `Components`. `Components` are used to store properties of the `Entity` which contains them. Each time the game updates, the game loops through all active `Systems`. `Systems` manage the game state by manipulating data in each `Entity`'s `Components` and eventually render them to the user interface. Note that in _Chrone_, `Systems` are called `EntitySystems` instead to avoid confusion with `java.lang.System`.

### `package components`
This package contains all `Components` used in the game.
 
 - ### `public abstract class Component`
   Parent class for all `Components`

 - ### `public class CameraComponent extends Component`
   A `Component` indicating that the containing `Entity` should be focused by the camera (should be shown on the screen) with an accompanying `priority`.
    - `private int priority` : indicates the priority of the `Entity`. `Entities` with lower value of `priority` will be focused by the camera first.
    - `public CameraComponent()`: constructor, sets `priority` to `0`.
    - `public CameraComponent(int priority)`: constructor, sets `priority` according to passed arguments.
    - `public int getPriority()`: returns `priority`.
    - `public void setPriority(int priority)`: sets `priority` according to passed arguments.

  - ### `public abstract class CodeComponent extends Component`
    A `Component` used to store `code` of _Switch Blocks_/_Door Blocks_. Extended by `SwitchComponent` and `DoorComponent`.
    - `private char code`: stores `code` as explained above.
    - `public CodeComponent(char code)` : constructor, sets `code` according to passed arguments.
    - `public char getCode()`: returns `code`.
    - `public void setCode(char code)`: sets `code` according to passed arguments.

  - ### `public class CollisionComponent extends Component`
    A `Component` indicating that the containing `Entity` can participate in collisions.
    - `private Shape collisionShape`: stores the shape used to determine collision.
    - `public CollisionComponent(Shape collisionShape)`: constructor, sets `collisionShape` according to passed arguments.
    - `public Shape getCollisionShape()`: returns `collisionShape`.
    - `public void setCollisionShape(Shape collisionShape)`: sets `collisionShape` according to passed arguments.

  - ### `public class ContactComponent extends Component`
    A `Component` used to store contacts against a certain selection of `Entities`.
    - `private Set<Direction> contacts`: stores active contact directions.
    - `private Class<? extends Component> targetComponent;`: stores `class` of target `Component`. Only `Entities` containing the target `class` will be considered in contact detection.
    - `public ContactComponent(Class<? extends Component> targetComponent)`: constructor, sets `targetComponent` according to passed arguments.
    - `public boolean isContact()`: returns `true` if `contacts` contains at least 1 element, otherwise, returns `false`.
    - `public boolean isContact(Direction direction)`: returns `true` if `contacts` contains `direction`, otherwise, returns `false`. 
    - `public void setContact(Direction direction)`: adds `direction` to `contacts`.
    - `public void setContact(Direction direction, boolean isContact)`: if `isContact` is `true`, adds `direction` to `contacts`, otherwise, remove `direction` from `contacts`.
    - `public void clearContacts()`: remove all elements from `contacts`
    - `public Class<? extends Component> getTargetComponent()`: returns `targetComponent`

  - ### `public class InputComponent extends Component`
    A `Component` used to store reactive `Intents` to user inputs of the containing `Entitiy`.
    - `private HashMap<KeyCode, Intent> pressedIntents`: stores `Intents` corresponding to `KeyCodes`.
    - `private HashMap<KeyCode, Intent> triggeredIntents`: stores `Intents` corresponding to `KeyCodes`.
    - `public InputComponent()`: constructor.
    - `public Intent getPressedIntent(KeyCode keyCode)`: returns stored `Intent` in `pressedIntents` associated with `keyCode`. If no associated `Intent` can be found, returns `null` instead.
    - `public void setPressedIntent(KeyCode keyCode, Intent intent)`: stores `intent` in `pressedIntents`, associating it with `keyCode`.
    - `public Intent getTriggeredIntent(KeyCode keyCode)`: returns stored `Intent` in `triggeredIntents` associated with `keyCode`. If no associated `Intent` can be found, returns `null` instead.
    - `public void setTriggeredIntent(KeyCode keyCode, Intent intent)`: stores `intent` in `triggeredIntents`, associating it with `keyCode`.

  - ### `public class DelayedInputComponent extends InputComponent`
    Behaves exactly the same as `InputComponent`. Stores delayed inputs used by _Dynamic Chrones_.

  - ### `public class DoorComponent extends CodeComponent`
    A `Component` used for mechanisms of _Door Blocks_.
    - `private Shape collisionShape`: stores the shape used to determine collision when the _Door Block_ is closed.
    - `private boolean isOpen`: stores the state of the _Door Block_.
    - `public DoorComponent(Shape collisionShape, char code)`: constructor, sets `collisionShape` and `code` according to passed arguments. Initializes `isOpen` to `false`.
    - `public Shape getCollisionShape()`: returns `collisionShape`.
    - `public void setCollisionShape(Shape collisionShape)`: sets `collisionShape` and `code` according to passed arguments. Initialize `isOpen` to `false`.
    - `public boolean isOpen()`: returns `isOpen`.
    - `public void setOpen(boolean isOpen)`: returns `isOpen`.

  - ### `public class ExpirationComponent extends Component`
    A `Component` indicating that the containing `Entity` should be destroyed after a certain period of time has passed.
    - `private double expirationTime`: stores the remaining time until the `Entity` gets destroyed.
    - `public ExpirationComponent(double expirationTime)`: constructor, sets `expirationTime` according to passed arguments.
    - `public double getExpirationTime()`: returns `expirationTime`.
    - `public void decreaseExpirationTime(double amount)`: decreases `expirationTime` by `amount`.

  - ### `public class GoalComponent extends Component`:
    A `Component` indicating that the containing `Entity` is a _Goal Block_.

  - ### `public class GravityComponent extends Component`
    A `Component` indicating that the containing `Entity` is affected by gravity.

  - ### `public class InputRecorderComponent extends Component`
    A `Component` used to store inputs within a period of `duration`.
    - `private Queue<Set<KeyCode>> pressedRecord`: stores all pressed inputs of each frame.
    - `private Queue<Set<KeyCode>> triggeredRecord`: stores all triggered inputs of each frame.
    - `private int duration`: stores the duration for which inputs are recorded.
    - `public InputRecorderComponent(int duration)`: constructor, sets `duration` according to passed arguments.
    - `public Queue<Set<KeyCode>> getPressedRecord()`: returns `pressedRecord`.
    - `public void addPressedRecord(Set<KeyCode> pressed)`: stores pressed inputs of the current frame to `pressedRecord`.
    - `public Queue<Set<KeyCode>> getTriggeredRecord()`: returns `triggeredRecord`.
    - `public void addTriggeredRecord(Set<KeyCode> triggered)`: stores triggered inputs of the current frame to `pressedRecord`.
    - `public double getDuration()`: returns the remaining `duration`.
    - `public void decreaseDuration()`: decreases `duration` by `1`.

  - ### `public class InputRecordComponent extends Component`
    A `Component` used to store recorded inputs from `InputRecorderComponent`.
    - `private Queue<Set<KeyCode>> pressedRecord`: stores all pressed inputs of each frame.
    - `private Queue<Set<KeyCode>> triggeredRecord`: stores all triggered inputs of each frame.
    - `private int duration`: stores the duration for which inputs were recorded.
    - `public InputRecordComponent(Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord, int duration)`: constructor, sets `pressedRecord`, `triggeredRecord`, `duration` according to passed arguments.
    - `public boolean isAvailable()`: returns `true` if `duration` is positive and there are elements in `pressedRecord` and `triggeredRecord`.
    - `public Set<KeyCode> getPressed()`: returns all pressed inputs of the earliest frame available and remove it from `pressedRecord`.
    - `public Set<KeyCode> getTriggered()`: returns all triggered inputs of the earliest frame available and remove it from `pressedRecord`.
    - `public void decreaseDuration()`: decreases `duration` by `1`.

  - ### `public class JumpableSurfaceComponent extends Component`
    A `Component` used to indicate that the containing `Entity` can be jumped on by the player.

  - ### `public class MessageComponent extends Component`
    A `Component` used to store messages associated with _Info Blocks_/_Goal Blocks_.
    - `private String message`: stores the associated message.
    - `private boolean isSingle`: if `true`, `message` will be shown only once.
    - `private boolean isActive`: indicates the state of `message`. Set to `true` while `message` is being shown on-screen.
    - `public MessageComponent(String message, boolean isSingle)`: constructor, sets `message` and `isSingle` according to passed arguments. Initializes `isActive` to `false`.
    - `public String getMessage()`: returns `message`.
    - `public void setMessage(String message)`: sets `message` according to passed arguments.
    - `public boolean isSingle()`: returns `isSingle`.
    - `public void setSingle(boolean isSingle)`: sets `isSingle` according to passed arguments.
    - `public boolean isActive()`: returns `isActive`.
    - `public void setActive(boolean isActive)`: sets `isActive` according to passed arguments.

  - ### `public class PositionComponent extends Component`
    A `Component` used to store the position of the containing `Entity` inside the game world. Top-left corner is `(x = 0, y = 0)`.
    - `private Point2D position`: stores 2 dimensional position coordinates.
    - `public PositionComponent()`: constructor, sets `position` to `(0, 0)`.
    - `public PositionComponent(Point2D position)`: constructor, sets `position` according to passed arguments.
    - `public PositionComponent(double x, double y)`: constructor, sets `position` according to passed arguments.
    - `public Point2D getPosition()`: returns `position`.
    - `public void setPosition(Point2D position)`: sets `position` according to passed arguments.

  - ### `public class RenderComponent extends Component`
    A `Component` used to store the visual aspects used when rendering the containing `Entity` on the screen.
    - `private Renderable shape`: stores a `Renderable` shape.
    - `private Paint color`: stores rendering color.
    - `private double alpha`: stores alpha values.
    - `private Point2D offset`: stores offset. Used when the visual shape does not have the same position as the containing `Entity`'s position stored inside its `PositionComponent`.
    - `public RenderComponent(Renderable shape, Paint color)`: constructor, sets `shape`, `color` according to passed arguments. Sets `alpha` to `1` and `offset` to `(0, 0)`.
    - `public RenderComponent(Renderable shape, Paint color, Point2D offset)`: constructor, sets `shape`, `color`, `offset` according to passed arguments. Sets `alpha` to `1`.
    - `public RenderComponent(Renderable shape, Paint color, double alpha)`: constructor, sets `shape`, `color`, `alpha` according to passed arguments. Sets `offset` to `(0, 0)`.
    - `public RenderComponent(Renderable shape, Paint color, double offsetX, double offsetY)`: constructor, sets `shape`, `color`, `offset` according to passed arguments. Sets `alpha` to `1`.
    - `public RenderComponent(Renderable shape, Paint color, double offsetX, double offsetY, double alpha)`: constructor, sets `shape`, `color`, `alpha`, `offset` according to passed arguments.
    - `public Renderable getShape()`: returns `shape`.
    - `public void setShape(Renderable shape)`: sets `shape` according to passed arguments.
    - `public Paint getColor()`: returns `color`.
    - `public void setColor(Paint color)`: sets `color` according to passed arguments.
    - `public double getAlpha()`: returns `alpha`.
    - `public void setAlpha(double alpha)`: sets `alpha` according to passed arguments.
    - `public Point2D getOffset()`: returns `offset`.
    - `public void setOffset(Point2D offset)`: sets `offset` according to passed arguments.

  - ### `public class SwitchComponent extends CodeComponent`: 
    A `Component` used for mechanisms of _Switch Blocks_.
    - `public SwitchComponent(char code)`: constructor, sets `code` according to passed arguments.

  - ### `public class VelocityComponent extends Component`: 
    A `Component` used to store the velocity of the containing `Entity`.
    - `private Point2D velocity`: stores the velocity of the containing `Entity`.
    - `public VelocityComponent()`: constructor, sets `velocity` to `(0, 0)`.
    - `public VelocityComponent(Point2D velocity)`: constructor, sets `velocity` according to passed arguments.
    - `public VelocityComponent(double x, double y)`: constructor, sets `velocity` according to passed arguments.
    - `public Point2D getVelocity()`: returns `velocity`.
    - `public void setVelocity(Point2D velocity)`: sets `velocity` according to passed arguments.

### `package core`
This package contains core logic unit utilized in the game.

  - ### `public class AudioManager`
    A _singleton_ used to perform all audio related tasks.
    - `public static final double VOLUME`: stores system-wide volume used when playing audio.
    - `public static final Media LOST_SIGNAL`: stores preloaded media `lost_signal.mp3`. Used as background music.
    - `public static final Media DEAR_DIARY`: stores preloaded media `dear_diary.mp3`. Used as background music.
    - `public static final AudioClip DOOR_OPEN`: stores preloaded media `043.wav`. Used as a sound effect when _Door Blocks_ open.
    - `public static final AudioClip DOOR_CLOSE` : stores preloaded media `019.wav`. Used as a sound effect when _Door Blocks_ close.
    - `public static final AudioClip ANCHOR`: stores preloaded media `046.wav`. Used as a sound effect when _Anchors_ are created.
    - `public static final AudioClip CHRONE_CREATE`: stores preloaded media `052.wav`. Used as a sound effect when _Chrones_ are created.
    - `public static final AudioClip CHRONE_REMOVE`: stores preloaded media `053.wav`. Used as a sound effect when _Chrones_ are removed.
    - `public static final AudioClip INFO_BLOCK`: stores preloaded media `m_cancel.wav`. Used as a sound effect when _Info Blocks_ are triggered.
    - `public static final AudioClip GOAL_BLOCK`: stores preloaded media `m_pass.wav`. Used as a sound effect when _Goal Blocks_ are triggered.
    - `private static AudioManager instance`: stores the _singleton_ instance.
    - `private Set<AudioClip> audioClips`: stores all active audio clips.
    - `private MediaPlayer bgm`: stores currently playing background music.
    - `private AudioManager()`: constructor.
    - `public static AudioManager getInstance()`: returns `instance`.
    - `public void uniquePlay(AudioClip audioClip)`: plays the `audioClip` only if `audioClips` does not contain it.
    - `public void uniquePlay(AudioClip audioClip, double volume)`: plays the `audioClip` with custom `volume` only if `audioClips` does not contain it.
    - `public boolean contains(AudioClip audioClip)`: returns `true` if `audioClips` contains `audioClip`.
    - `public void add(AudioClip audioClip)`: adds `audioClip` to `audioClips`.
    - `public void remove(AudioClip audioClip)`: removes `audioClip` from `audioClips`.
    - `public void playBgm(Media media)`: stops any currently playing `bgm`, sets `media` as the new `bgm`, then plays it.
    - `public void stopBgm()`: stops any currently playing `bgm`.

  - ### `public class ChroneManager`
    A _singleton_ used to perform all _Chrone_ related tasks.
    - `public static final int MAX_ANCHOR`: stores maximum number of _Anchors_ allowed on the screen.
    - `public static final int MAX_STATIC_CHRONE`: stores maximum number of _Static Chrones_ allowed on the screen.
    - `public static final int MAX_DYNAMIC_CHRONE`: stores maximum number of _Dynamic Chrones_ allowed on the screen.
    - `public static final int STATIC_CHRONE_DURATION`: stores duration of recorded inputs of _Static Chrones_.
    - `public static final int DYNAMIC_CHRONE_DURATION`: stores duration of recorded inputs of _Dynamic Chrones_.
    - `private static ChroneManager instance`: stores the _singleton_ instance.
    - `private Queue<Anchor> anchors`: stores all active _Anchors_.
	- `private Queue<StaticChrone> staticChrones`: stores all active _Static Chrones_.
	- `private Queue<DynamicChrone> dynamicChrones`: stores all active _Dynamic Chrones_.
	- `private ChroneManager()`: constructor.
	- `public static ChroneManager getInstance()`: returns `instance`.
	- `public static int getMaxDuration()`: returns `max(STATIC_CHRONE_DURATION, DYNAMIC_CHRONE_DURATION)`.
	- `public void createAnchor(Point2D position)`: creates a new _Anchor_ at `position`, remove the oldest one if the number of _Anchors_ exceeds `MAX_ANCHOR`, then adds the newly created _Anchor_ to `anchors`.
	- `public void createStaticChrone(Point2D position)`: creates a new _Static Chrone_ at `position`, remove the oldest one if the number of _Static Chrones_ exceeds `MAX_STATIC_CHRONE`, then adds the newly created _Static Chrone_ to `staticChrones`.
	- `public void createDynamicChrone(Point2D position, Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord)` : creates a new _Dynamic Chrone_ at `position` with `pressedRecord` and `triggeredRecord` stored in its `InputRecordComponent`, remove the oldest one if the number of _Dynamic Chrones_ exceeds `MAX_DYNAMIC_CHRONE`, then adds the newly created _Dynamic Chrone_ to `dynamicChrones`.
	- `private void addAnchor(Anchor anchor)`: add `anchor` to `anchors`.
	- `private void removeAnchor()`: remove the oldest _Anchor_ from `anchors`.
	- `private void addStaticChrone(StaticChrone staticChrone)`: add `staticChrone` to `staticChrones`.
	- `private void removeStaticChrone()`: remove the oldest _Static Chrone_ from `staticChrones`.
	- `private void addDynamicChrone(DynamicChrone dynamicChrone)`: add `dynamicChrone` to `dynamicChrones`.
	- `private void removeDynamicChrone()`: remove the oldest _Dynamic Chrone_ from `dynamicChrones`.

  - ### `public class EntityManager`
    A _singleton_ used to perform all `Entity` related tasks.
    - `private static EntityManager instance`: stores the _singleton_ instance.
    - `private List<Entity> entities`: stores all `Entities`.
	- `private EntityManager()`: constructor.
	- `public static EntityManager getInstance()`: returns `instance`.
	- `public List<Entity> getEntities()`: returns a copy of `entities`.
	- `public void add(Entity... entities)`: add all members of `entities` to `this.entities`.
	- `public void addAll(List<Entity> entities)`: add all members of `entities` to `this.entities`.
	- `public void remove(Entity... entities)`: remove all members of `entities` from `this.entities`.
	- `public void clear()`: remove all members of `entities`.
	- `public void sort()`: sort `entities`.

  - ### `public class EntitySystemManager`
    A _singleton_ used to perform all `EntitySystem` related tasks.
    - `private static EntitySystemManager instance`: stores the _singleton_ instance.
    - `private SortedSet<EntitySystem> systems`: stores all `EntitySystems`.
	- `private EntitySystemManager()`: constructor.
	- `public static EntitySystemManager getInstance()`: returns `instance`.
	- `public void add(EntitySystem... systems)`: add all members of `systems` to `this.systems`.
	- `public void remove(EntitySystem... systems)`: remove all members of `systems` from `this.systems`.
	- `public void clear()`: remove all members of  `systems`.
	- `public void update(double deltaTime)`: loops to all stored `EntitySystem` in `systems` and call `update(deltaTime)` of each `EntitySystem`.


  - ### `public class InputManager`
    A _singleton_ used to perform all input related tasks.
    - `private static InputManager instance`: stores the _singleton_ instance.
    - `private Set<KeyCode> pressed`: stores all pressed inputs.
    - `private Set<KeyCode> triggered`: stores all triggered inputs.
    - `private Map<KeyCode, Intent> pressedIntents`: stores global `Intents` corresponding to pressed `KeyCodes`.
    - `private Map<KeyCode, Intent> triggeredIntents`: stores global `Intents` corresponding to triggered `KeyCodes`.
	- `private InputManager()`: constructor.
	- `public static InputManager getInstance()`: returns `instance`.
	- `public Set<KeyCode> getPressed()`: returns a copy of `pressed`.
	- `public boolean isPressed(KeyCode keyCode)`: returns `true` if `pressed` contains `keyCode`.
	- `public void setPressed(KeyCode keyCode, boolean isPressed)`: if `isPressed` is `true`, adds `keyCode` to `pressed` and `triggered` if `pressed` does not already contains it. Otherwise, removes `keyCode` from `pressed`.
	- `public Set<KeyCode> getTriggered()`: returns a copy of `triggered`.
	- `public boolean isTriggered(KeyCode keyCode)`: returns `true` if `triggered` contains `keyCode`.
	- `public void clearTriggered()`: remove all members of `triggered`.
    - `public Intent getPressedIntent(KeyCode keyCode)`: returns stored `Intent` in `pressedIntents` associated with `keyCode`. If no associated `Intent` can be found, returns `null` instead.
    - `public void setPressedIntent(KeyCode keyCode, Intent intent)`: stores `intent` in `pressedIntents`, associating it with `keyCode`.
    - `public Intent getTriggeredIntent(KeyCode keyCode)`: returns stored `Intent` in `triggeredIntents` associated with `keyCode`. If no associated `Intent` can be found, returns `null` instead.
    - `public void setTriggeredIntent(KeyCode keyCode, Intent intent)`: stores `intent` in `triggeredIntents`, associating it with `keyCode`.

  - ### `public class LevelManager`
    A _singleton_ used to perform all _level_ related tasks.
    - `private static LevelManager instance`: stores the _singleton_ instance.
    - `private Level level`: stores current _level_.
    - `private Image background`: stores current background image.
    - `private Media music`: stores current background music.
	- `private LevelManager()`: constructor, sets `level` to `null`.
	- `public static LevelManager getInstance()`: returns `instance`.
	- `public void load()`: reads data from `level` and initializes the game state. Throws `JsonSyntaxException` if the data do not comply to the format.
	- `private void checkLevelData()`: checks and verifies data of `level`. Throws `JsonSyntaxException` if the data do not comply to the format.
	- `private void loadLevelResources()`: loads background image and music from `level`. Sets to defaults if loading failed.
	- `private void resetLevel()`: reinitializes game state.
	- `private void parseLevelData(String[] data, String[] messages, long width, long height)`: creates and adds `Entities` to `EntityManager.entities` based on `data`, `messages`, `width`, and `height`.
	- `public Level getLevel()`: returns `level`.
	- `public void setLevel(Level level)`: sets `this.level` to `level`.
	- `public Image getBackground()`: returns `background`.
	- `public void setBackground(Image background)`: sets `this.background` to `background`.

  - ### `public class ToastManager`
    A _singleton_ used to show/hide messages on the screen. The text box used to show messages is called a _Toast_.
    - `public static final Paint COLOR`: stores the default background color of the _Toast_.
	- `public static final Paint TEXT_COLOR`: stores the default text color.
	- `public static final double ALPHA`: stores the default alpha value of the _Toast_.
	- `public static final double MARGIN`: stores the default margin size of the _Toast_.
    - `private static ToastManager instance`: stores the _singleton_ instance.
    - `private String message`: stores currently shown message.
	- `private ToastManager()`: constructor.
	- `public static ToastManager getInstance()`: returns `instance`.
	- `public void show(String message)`: shows the _Toast_ along with `message` in the center of the screen.
	- `public void hide()`: hides the _Toast_.
	- `public void hide(String message)`: hides the _Toast_ only if `this.message` is equal to `message`.

### `package entities`
This package contains all `Entities` used in the game.

  - ### `public class Entity implements Comparable<Entity>`: 
    Parent class for all `Entities`.
    - `private Map<Class<? extends Component>, Component> components`: stores the `Entity`'s `Components`.
    - `private int z`: stores the value used to sort `Entities` in `EntityManager.entities`, affecting the iteration order when processed by `EntitySystems`.
    - `public Entity()`: constructor, sets `z` to `0`.
    - `public Entity(int z)`: constructor, sets `z` according to passed arguments.
    - `public void add(Component... components)`: adds all members of `components` to `this.components`.
    - `public void remove(Class<? extends Component> componentClass)`: removes a `Component` with the `class` of `componentClass` from `components`.
    - `public void remove(Component... components)`: removes all members of `components` to `this.components`.
    - `public void clear()`: removes all members of `components`.
    - `public boolean contains(Class<? extends Component> componentClass)`: returns `true` if `components` contains a `Component` with the `class` of `componentClass`.
    - `public Component getComponent(Class<? extends Component> componentClass) throws ComponentNotFoundException`: returns the `Component` with the `class` of `componentClass` stored inside `components`. If there is no such `Component`, throws `ComponentNotFoundException`.
    - `public int getZ()`: returns `z`.
    - `public void setZ(int z)`: sets `z` according to passed arguments and calls `EntityManager.sort()`.
    - `public int compareTo(Entity other)`: overriding method of `Comparable<Entity>`, compares `z` with `other.z`.

  - ### `public class Anchor extends Entity`: 
    An `Entity` representation of _Anchors_.
    - `public static final double WIDTH`: stores width of _Anchors_.
	- `public static final double HEIGHT`: stores height of _Anchors_.
	- `public static final Paint COLOR`: stores color of _Anchors_.
	- `public static final double ALPHA`: stores alpha value of _Anchors_.
	- `public Anchor()`: constructor, sets its position to `(0, 0)`.
	- `public Anchor(Point2D position)`: constructor, sets its position according to passed arguments.
	- `public Anchor(double positionX, double positionY)`: constructor, sets its position according to passed arguments.

  - ### `public class Block extends Entity`: 
    An `Entity` representation of _Blocks_.
    - `public static final double WIDTH`: stores width of _Blocks_.
	- `public static final double HEIGHT`: stores height of _Blocks_.
	- `public static final Paint COLOR`: stores color of _Blocks_.
	- `public Block()`: constructor, sets its position to `(0, 0)`.
	- `public Block(Point2D position)`: constructor, sets its position according to passed arguments.
	- `public Block(double positionX, double positionY)`: constructor, sets its position according to passed arguments.

  - ### `public abstract class Chrone extends Entity`: 
    An `Entity` representation of _Chrones_. Extended by `StaticChrone` and `DynamicChrone`.
    - `public static final double ALPHA`: stores alpha value of _Chrones_.
	- `public Chrone()`: constructor, sets its position and velocity to `(0, 0)`.
	- `public Chrone(Point2D position, Point2D velocity)`: constructor, sets its position and velocity according to passed arguments.
	- `public Chrone(double positionX, double positionY, double velocityX, double velocityY)`: constructor, sets its position and velocity according to passed arguments.

  - ### `public class DoorBlock extends Entity`: 
    An `Entity` representation of _Door Blocks_.
    - `public static final double ALPHA`: stores alpha value of _Door Blocks_.
	- `public DoorBlock(Paint codeColor, char code)`: constructor, sets its code color and character according to passed arguments, then sets its position to `(0, 0)`.
	- `public DoorBlock(double positionX, double positionY, Paint codeColor, char code)`: constructor, sets its position, code color, code character according to passed arguments.

  - ### `public class DynamicChrone extends Chrone`: 
    An `Entity` representation of _Dynamic Chrones_.
	- `public static final Paint COLOR`: stores color of _Dynamic Chrones_.
	- `public DynamicChrone(Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord)`: constructor, sets its input records according to passed arguments and sets its position to `(0, 0)`.
	- `public DynamicChrone(Point2D position, Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord)`: constructor, sets its position and input records according to passed arguments.
	- `public DynamicChrone(double positionX, double positionY, Queue<Set<KeyCode>> pressedRecord, Queue<Set<KeyCode>> triggeredRecord)`: constructor, sets its position and input records according to passed arguments.

  - ### `public class GoalBlock extends Entity`: 
    An `Entity` representation of _Goal Blocks_.
    - `public static final Paint COLOR`: stores color of _Goal Blocks_.
	- `public static final Paint ACTIVE_COLOR` : stores color of _Goal Blocks_ when triggered.
	- `public GoalBlock(Paint goalColor)`: constructor, sets its secondary color according to passed arguments and sets its position to `(0, 0)`.
	- `public GoalBlock(double positionX, double positionY, Paint goalColor)`: constructor, sets its position and secondary color according to passed arguments.

  - ### `public class InfoBlock extends Entity`: 
    An `Entity` representation of _Info Blocks_.
    - `public static final Paint COLOR`: stores color of _Info Blocks_.
	- `public static final Paint COLOR_DISABLED` : stores color of _Info Blocks_ while disabled.
	- `public InfoBlock(String message, Paint secondaryColor)`: constructor, sets its message and secondary color according to passed arguments, then sets its position to `(0, 0)`.
	- `public InfoBlock(double positionX, double positionY, String message, Paint secondaryColor)`: constructor, sets its position, message, secondary color according to passed arguments.

  - ### `public class Player extends Entity`: 
    An `Entity` representation of the _Player_.
    - `public static final double WIDTH`: stores width of the _Player_.
	- `public static final double HEIGHT`: stores height of the _Player_.
	- `public static final Paint COLOR`: stores color of the _Player_.
	- `public static final double ACCELERATION_X`: stores movement speed in the horizontal axis of the _Player_.
	- `public static final double ACCELERATION_Y`: stores movement speed in the vertical axis of the _Player_.
	- `public static final Point2D MAX_VELOCITY`: stores maximum velocity of the _Player_.
	- `public Player()`: constructor, sets its position and velocity to `(0, 0)`.
	- `public Player(Point2D position, Point2D velocity)`: constructor, sets its position and velocity according to passed arguments.
	- `public Player(double positionX, double positionY, double velocityX, double velocityY)`: constructor, sets its position and velocity according to passed arguments.

  - ### `public class StaticChrone extends Chrone`: 
    An `Entity` representation of _Static Chrones_.
	- `public static final Paint COLOR`: stores color of _Static Chrones_.
	- `public StaticChrone()`: constructor, sets its position to `(0, 0)`.
	- `public StaticChrone(Point2D position)`: constructor, sets its position according to passed arguments.
	- `public StaticChrone(double positionX, double positionY)`: constructor, sets its position according to passed arguments.
 
  - ### `public class SwitchBlock extends Chrone`: 
    An `Entity` representation of _Switch Blocks_.
	- `public SwitchBlock(Paint codeColor, char code)`: constructor, sets its code color and character accorrding to passed arguments, then sets its position to `(0, 0)`.
	- `public SwitchBlock(double positionX, double positionY, Paint codeColor, char code)`: constructor, sets its position, code color, code character accorrding to passed arguments.

  - ### `public class TextUserInterface extends Chrone`: 
    An `Entity` used to represent user interface elements in the main menu.
    - `public static final Paint COLOR`: stores the text color used when rendering the element.
	- `public TextUserInterface(String text, double positionX, double positionY)`: constructor, sets its text and position according to passed arguments, then sets its font to the system's default.
	- `public TextUserInterface(String text, Font font, double positionX, double positionY)`: constructor, sets its text, font, position accorrding to passed arguments.
 
### `package intents`
This package contains all `Intents` used in the game.

  - ### `public interface Intent`:
    Parent class for all `Intents`. `Intents` are actions invoked by inputs.
    - `public abstract void handle(Entity entity)`: performs the predefined actions. Called by `InputSystem` when a matching input stored by `InputManager` is asserted.

  - ### `public class CreateAnchorIntent implements Intent`:
    An `Intent` used to create _Anchors_.
    - `public void handle(Entity entity)`: creates an _Anchor_ at the position of `entity`.

  - ### `public class CreateDynamicChroneIntent implements Intent`:
    An `Intent` used to create _Dynamic Chrones_.
    - `public void handle(Entity entity)`: creates a _Dynamic Chrone_ at the position of `entity` with recorded inputs from `entity`'s `InputRecorderComponent`.

  - ### `public class CreateStaticChroneIntent implements Intent`:
    An `Intent` used to create _Static Chrones_.
    - `public void handle(Entity entity)`: creates a  _Static Chrone_ at the position of `entity`.

  - ### `public class MoveIntent implements Intent`:
    An `Intent` used to move the player.
    - `protected Point2D acceleration`: stores acceleration of the player.
    - `protected Point2D maxVelocity`: stores maximum velocity of the player.
    - `public MoveIntent(Point2D acceleration, Point2D maxVelocity)`: constructor, sets `acceleration` and `maxVelocity` according to passed arguments.
    - `public void handle(Entity entity)`: increases the velocity of `entity` by `acceleration`, capped by `maxVelocity`.
    - `private Point2D getVelocity(Point2D velocity, Point2D acceleration, Point2D maxVelocity)`: returns `max(velocity + acceleration, maxVelocity)`.

  - ### `public class JumpIntent extends MoveIntent`:
    An `Intent` used by the player to jump.
    - `public JumpIntent(Point2D acceleration, Point2D maxVelocity)`: constructor, sets `acceleration` and `maxVelocity` according to passed arguments.
    - `public void handle(Entity entity)`: if `entity` has a contact in `Direction.DOWN`, increases the velocity in the horizontal axis of `entity` by `acceleration`, but sets the velocity in the vertical axis to `acceleration`. The velocity in both axes are capped by `maxVelocity`. If there is no such contact, do nothing.
    - `private Point2D getVelocity(Point2D velocity, Point2D acceleration, Point2D maxVelocity)`: returns `( max(velocity.x + acceleration.x, maxVelocity), max(acceleration.y, maxVelocity) )`.

  - ### `public class OpenLevelIntent implements Intent`:
    An `Intent` used to open a new _level_.
    - `private static final FileChooser fileChooser`: stores the file chooser instance used to open a new _level_.
    - `public void handle(Entity entity)`: opens the file chooser dialog and load the selected _level_ file. If any error arises, an alert dialog box with corresponding error message will be shown and the _level_ file discarded.
    - `private String read(File file) throws NullPointerException, IOException`: reads `file` to `String`. Throws `NullPointerException` if `file` is `null`. Throws `IOException` if `file` cannot be read.

  - ### `public class RestartIntent implements Intent`:
    An `Intent` used to restart the _level_.
    - `public void handle(Entity entity)`: clears the _Toast_ and restarts the _level_ only if the current screen is not the main menu.

  - ### `public class ReturnToMenuIntent implements Intent`:
    An `Intent` used to navigate back to the main menu.
    - `public void handle(Entity entity)`: loads the main menu only if the current screen is not the main menu.

  - ### `public class ToggleHelpIntent implements Intent`:
    An `Intent` used to toggle display of help messages on the _Toast_.
    - `public static final String HELP`: stores the help messages.
	- `private static boolean isActive`: stores the status of help messages. Set to `true` when the help messages are shown on-screen.
    - `public void handle(Entity entity)`: shows the help messages and toggles `isActive`.
    - `private void toggleActive()`: toggles `isActive`.

### `package main`
This package contains the `Main` class used to run the game.

  - ### `public class Main extends Application`:
    A _singleton_. Starts and runs the game.
    - `public static final String TITLE`: stores the title of the game window.
	- `public static final boolean IS_RESIZABLE`: if `true`, the game window can be resized.
	- `public static final int WIDTH`: stores the width of the game window.
	- `public static final int HEIGHT`: stores the height of the game window.
	- `public static final int FPS`: stores the rate at which the game updates.
	- `public static final long TIME_PER_FRAME`: stores the minumum amount of time the game has to wait before continuing the game loop.
	- `public static final double MAXIMUM_DELTA_TIME`: stores the maximum amount of time the game has to wait before continuing the game loop.
	- `public static Main instance`: stores the _singleton_ instance.
	- `private Stage primaryStage`: stores the _primary stage_.
	- `private Pane applicationRoot`: stores the _root node_ of the _application_.
	- `private Canvas background`: stores the _canvas_ used to render background images.
	- `private Canvas game`: stores the _canvas_ used to render the game.
	- `private Canvas toast`: stores the _canvas_ used to render the _Toast_.
	- `private Thread gameLoop`: the main _thread_ which runs the game loop.
	- `private boolean isRunning`: if true, the game runs. Otherwise, the game stops.
	- `public Main()`: constructor.
	- `public static Main getInstance()`: returns `instance`.
	- `public static void main(String[] args)`: launches the _application_.
	- `public Stage getPrimaryStage()`: returns `primaryStage`.
	- `public Pane getApplicationRoot()`: returns `applicationRoot`.
	- `public Canvas getBackground()`: returns `background`.
	- `public Canvas getGame()`: returns `game`.
	- `public Canvas getToast()`: returns `toast`.
	- `public boolean isRunning()`: returns `isRunning`.
	- `public void setRunning(boolean isRunning)`: sets `this.isRunning` to `isRunning`.
	- `public void start(Stage primaryStage)`: initializes and starts the game.
	- `private void initializeStage()`: initializes graphical interface.
	- `private void initializeGame()`: initializes game state.
	- `private void setInputHandlers()`: sets input handlers `OnKeyPressed` and `OnKeyReleased`.
	- `private Thread getGameLoop()`: returns the main _thread_ which runs the game loop.
	- `public void stop() throws Exception`: sets `isRunning` to `false` and stops all thread executions. Throws an `Exception` if it is unable to do so.

### `package renderables`
This package contains all `Renderable` classes used to represent visual shape of `Entities` in the game. _Overriden methods_ are omitted in this section because they all perform the same functions, albeit with different shapes.

  - ### `public interface Renderable`:
    All `Renderable` classes implement `Renderable`. Each `Renderable` has an associated shape which it can `render`.
    - `public abstract void render(GraphicsContext gc, double x, double y)`: renders the shape with `gc` at the position `(x, y)` as the top-left corner of the shape.
    - `public abstract double getWidth()`: returns absolute width of the shape.
    - `public abstract double getHeight()`: returns absolute height of the shape.

  - ### `public class RenderableCircle implements Renderable`:
    A `Renderable` representing a circle.
    - `private double radius`: stores the radius.
    - `public RenderableCircle(double radius)`: constructor, sets `radius` according to passed arguments.
    - `public void setRadius(double radius)`: sets `radius` according to passed arguments.

  - ### `public class RenderableDiamond implements Renderable`:
    A `Renderable` representing a diamond.
    - `private double width`: stores the width.
    - `private double height`: stores the height.
    - `public RenderableDiamond(double width, double height)`: constructor, sets `width` and `height` according to passed arguments.
    - `public void setWidth(double width)`: sets `width` according to passed arguments.
    - `public void setHeight(double height)`: sets `height` according to passed arguments.

  - ### `public class RenderableRectangle implements Renderable`:
    A `Renderable` representing a rectangle.
    - `private double width`: stores the width.
    - `private double height`: stores the height.
    - `public RenderableRectangle(double width, double height)`: constructor, sets `width` and `height` according to passed arguments.
    - `public void setWidth(double width)`: sets `width` according to passed arguments.
    - `public void setHeight(double height)`: sets `height` according to passed arguments.

  - ### `public abstract class RenderableBlock extends RenderableRectangle`:
    A compound `Renderable` representing the shape of a _Block_.
    - `private RenderableRectangle block`: stores the shape of a _Block_.
    - `private Paint secondaryColor`: stores the secondary color.
    - `public RenderableBlock(double width, double height, Paint secondaryColor)`: constructor, sets `width` and `height`, `secondaryColor` according to passed arguments.
    - `public Paint getSecondaryColor()`: returns `secondaryColor`
    - `public void setSecondaryColor(Paint secondaryColor)`: sets `secondaryColor` according to passed arguments.

  - ### `public abstract class RenderableDoorBlock extends RenderableBlock`:
    A compound `Renderable` representing the shape of a _Door Block_.
    - `private RenderableDiamond core`: stores the shape of the diamond in the center of a _Door Block_.
    - `private boolean isOpen`: stores the state the door.
    - `public RenderableDoorBlock(double width, double height, Paint secondaryColor)`: constructor, sets `width` and `height`, `secondaryColor` according to passed arguments, then sets `isOpen` to `false`.
    - `public boolean isOpen()`: returns `isOpen`.
    - `public void setOpen(boolean isOpen)`: sets `isOpen` according to passed arguments.

  - ### `public abstract class RenderableSwitchBlock extends RenderableBlock`:
    A compound `Renderable` representing the shape of a _Switch Block_.
    - `private RenderableRectangle edgeX`: stores the shape of the vertical line shown at the edge of a _Switch Block_ when triggered.
    - `private RenderableRectangle edgeY`: stores the shape of the horizontal line shown at the edge of a _Switch Block_ when triggered.
    - `private RenderableCircle core`: stores the shape of the circle in the center of a _Switch Block_.
    - `private Set<Direction> directions`: stores the directions at which the _Switch Block_ is triggered from.
    - `private boolean isActive`: stores the state of the switch.
    - `public RenderableSwitchBlock(double width, double height, Paint secondaryColor)`: constructor, sets `width` and `height`, `secondaryColor` according to passed arguments, then sets `isActive` to `false`.
    - `public boolean isDirectionActive(Direction direction)`: returns `true` if `directions` contains `direction`.
    - `public void setDirection(Direction direction)`: adds `direction` to `directions`.
    - `public void setDirection(Direction direction, boolean isActive)`: if `isActive` is `true`, adds `direction` to `directions`, otherwise, removes `direction` from `directions`.
    - `public boolean isActive()`: returns `isActive`.
    - `public void setActive(boolean isActive)`: sets `isActive` according to passed arguments.

  - ### `public abstract class RenderableGoalBlock extends RenderableBlock`:
    A compound `Renderable` representing the shape of a _Goal Block_.
    - `private RenderableStar core`: stores the shape of the star in the center of a _Goal Block_.
    - `public RenderableGoalBlock(double width, double height, Paint secondaryColor)`: constructor, sets `width` and `height`, `secondaryColor` according to passed arguments.

  - ### `public abstract class RenderableInfoBlock extends RenderableBlock`:
    A compound `Renderable` representing the shape of an _Info Block_.
    - `private RenderableRectangle stem`: stores the shape of the _stem_ of `i` in the center of an _Info Block_.
	- `private RenderableCircle tittle`: stores the shape of the _tittle_ of `i` in the center of an _Info Block_.
	- `private double totalHeight`: stores the total height of `i` in the center of an _Info Block_.
    - `public RenderableInfoBlock(double width, double height, Paint secondaryColor)`: constructor, sets `width` and `height`, `secondaryColor` according to passed arguments.

  - ### `public class RenderableStar implements Renderable`:
    A `Renderable` representing a star. (Also known as a _pentagram_.)
	- `public static final double GOLDEN_RATIO`: stores the golden ratio which is `(sqrt(5) + 1) / 2`.
    - `private double width`: stores the width.
    - `private double height`: stores the height.
    - `private double outerEdge`: stores the length of the outer edge used in calculation of rendering coordinates.
    - `private double innerEdge`: stores the length of the innter edge used in calculation of rendering coordinates.
    - `public RenderableStar(double size)`: constructor, sets `width` and `height` to `size`, then calculate values of `outerEdge` and `innerEdge`.
    - `public void setWidth(double width)`: sets `width` according to passed arguments.
    - `public void setHeight(double height)`: sets `height` according to passed arguments.

  - ### `public class RenderableText implements Renderable`:
    A `Renderable` representing a block of text.
    - `private String text`: stores the text.
	- `private Font font`: stores the font.
    - `private double width`: stores the width.
    - `private double height`: stores the height.
    - `public RenderableText(String text, Font font)`: constructor, sets `text` and `font` according to passed arguments, then calculate the size and sets them to `width` and `height`.
    - `private Point2D getTextSize(String string, Font font)`: returns the text size of `string` when rendered with `font`.

### `package systems`
This package contains all `EntitySystems` used in the game loop. Some systems have quite complex implementations, so  most details are omitted in this section to preserve readability.

  - ### `public abstract class EntitySystem implements Comparable<EntitySystem>`:
    Parent class of all `EntitySystems`. Each `EntitySystem` has an associated `priority`. An `EntitySystem` with lower `priority` value will be executed before others with higher `priority` value. `EntitySystems` can be turned on and off by setting the value of `isActive`, which is `true` by default.

  - ### `public class CameraSystem extends EntitySystem`:
    An `EntitySystem` used to determine and simulate camera movement. It greedily tries to get as many `Entities` containing `CameraComponents` in the viewport as possible while not moving beyond the borders of the _level_. Camera movements are simulated by _translating_ the game _canvas_ to the opposite direction of movement. The `background` _canvas_ is also _translated_ by a fraction of the distance to create a parallax effect.

  - ### `public class CollisionSystem extends EntitySystem`:
    An `EntitySystem` used to detect and respond to collisions between `Entities` containing `CollisionComponents`. This implementation uses the swept axis-aligned bounding box collision detection algorithm.

  - ### `public class ContactSystem extends EntitySystem`:
    An `EntitySystem` used to detect contacts between `Entities` containing `ContactComponents` with other `Entities` containing `CollisionComponents`. Again, it uses axis-aligned bounding box to determine contacts and sets the data accordingly.

  - ### `public class DelayedInputSystem extends EntitySystem`:
    An `EntitySystem` used to provide delayed input from `InputRecordComponents`. This system is mainly created for usage with _Dynamic Chrones_.

  - ### `public class DoorSwitchSystem extends EntitySystem`:
    An `EntitySystem` used to process the mechanisms of _Door Blocks_ and _Switch Blocks_. It loops through all _Switch Blocks_ and stores any active `code` in `codes`. It then loops through all _Door Blocks_ and sets their states accordingly.

  - ### `public class ExpirationSystem extends EntitySystem`:
    An `EntitySystem` used to remove `Entities` containing `ExpirationComponent` from the game when its duration runs out. It simply loops through all `Entities` and removes any `Entity` with `ExpirationComponent.getExpirationTime()` <= `0`.

  - ### `public class GarbageCollectionSystem extends EntitySystem`:
    An `EntitySystem` used to remove `Entities` which falls outside of the _level_. It checks the position from `PositionComponent` of each `Entity` and removes the `Entity` if `position.y` is more than `level.height` multiplied by  `HEIGHT_LIMIT`. Additionally, if the `Entity` removed was a `Player`, it displays a _Toast_ message: _"Press R to restart level"_.

  - ### `public class GravitySystem extends EntitySystem`:
    An `EntitySystem` used to exert gravity force on each `Entity` containing a `GravityComponent`. It simply adds the velocity of each `Entity` with the value returned from `level.getGravity()`.

  - ### `public class InputRecorderSystem extends EntitySystem`:
    An `EntitySystem` used to record inputs into `InputRecorderComponents`. Each update, It checks if `InputRecorderComponents.getDuration()` is more than `0` and proceeds to copy inputs from `InputManager` to `InputRecorderComponents`.

  - ### `public class InputSystem extends EntitySystem`:
    An `EntitySystem` used to handle inputs. It loops through all registered `Intents` in `InputManager` and `InputComponents`, then calls `Intent.handle()` if the associated inputs are asserted.

  - ### `public class MessageSystem extends EntitySystem`:
    An `EntitySystem` used to process the mechanisms of _Info Blocks_ and _Goal Blocks_. It checks if any of these blocks have contact with the player/_Chrones_ and proceeds to show associated messages/play audio clips depending on the type of the _Block_.

  - ### `public class MovementSystem extends EntitySystem`:
    An `EntitySystem` used to apply each `Entity`'s velocity to its position. It adds velocity to position and stores the result in `PositionComponents`. It then zeros out the velocity on horizontal axis to give the player more control over the character. (Vertical velocity remains unchanged to simulate realistic gravity force.)

  - ### `public class RenderSystem extends EntitySystem`:
    An `EntitySystem` used to render all `Entities` containing `RenderComponents`. On each update, it rerenders the background image, clear all previously rendered `Entities`, and rerender all `Entities` with respect to their positions.

### `package utils`
This package contains all utility classes used in the implementation.

  - ### `public class Code`:
    This class contains code character mapping to predefined color. Used by _Door Blocks_/_Switch Blocks_. (which were randomized and selected for usage.)
    - `public static Map<Character, Color> codeColor`: lookup table of color mapped to code character.
    - `public static Color getCodeColor(char code)`: returns the associated color of `code`.

  - ### `public class Collision`:
    This class is used to represent a collision in collision detection and response.
    - `public static final Collision NONE`: a constant value indicating that there is no collision.
    - `public static final Collision ALL`: a constant value indicating that there is a collision on all directions.
	- `public static final Collision CORNER`: a constant value indicating that there is a diagonal collision.
	- `private Direction direction`: stores the direction of the collision.
	- `private double time`: stores the time until the collision happens. Used in calculation.
	- `public Collision(Direction direction, double time)`: constructor, sets `direction` and `time` according to passed arguments.
	- `public static Collision min(Collision a, Collision b)`: generally returns the collision with lower `time` with a few exceptions to make the collision detection work correctly.
	- `public Direction getDirection()`: returns `direction`.
	- `public void setDirection(Direction direction)`: sets `direction` according to passed arguments.
	- `public double getTime()`: returns `time`.
	- `public void setTime(double time)`: sets `time` according to passed arguments.

  - ### `public class CollisionBox extends Rectangle`:
    This class is used to represent an axis-aligned bounding box in collision detection and response.
    - `public CollisionBox(Point2D position, Shape collisionShape)`: constructor, sets position and size according to passed arguments.
    - `public CollisionBox(double x, double y, double width, double height)`: constructor, sets position and size according to passed arguments.
    - `public boolean intersects(Rectangle other)`: returns `true` if `this` intersects with `other`.
    - `public boolean intersectsX(Rectangle other)`: returns `true` if `this` intersects with `other` in the horizontal axis.
    - `public boolean intersectsY(Rectangle other)`: returns `true` if `this` intersects with `other` in the vertical axis.
    - `public CollisionBox getBroadPhaseArea(Point2D velocity)`: returns the _broad phase area_, a rectangular area used to quickly determine likeliness of a collision in collision detection.
    - `public Direction getContactDirectionX(Rectangle other)`: returns the contact direction if `this` is in contact with `other` in the horizontal axis. Otherwise, returns `Direction.NONE`. 
    - `public Direction getContactDirectionY(Rectangle other)`: returns the contact direction if `this` is in contact with `other` in the vertical axis. Otherwise, returns `Direction.NONE`.

  - ### `public class ComponentNotFoundException extends Exception`:
    A custom `Exception` used in `Components.getComponent()`.
    - `private static final long serialVersionUID`: an automatically generated serial UID, to make the class `Serializable`.

  - ### `public enum Direction`:
    An enumuration representing directions, used in many systems.
    > `UP(0, 0, -1), RIGHT(1, 1, 0), DOWN(2, 0, 1), LEFT(3, -1, 0), NONE(-1, 0, 0), ALL(-2, 1, 1), DIAGONAL(-3, 1, 1)`
    - `private final int rotate90, rotate180, rotate270`: stores value of `enum` index of the rotated direction.
    - `private final boolean isHorizontal`: set to `true` if the direction is horizontal.
    - `private final boolean isVertical`: set to `true` if the direction is vertical.
    - `private Direction(int value, int dx, int dy)`: constructor, `value` is the index (and also an identifier) of the direction, `dx` and `dy` are displacement values in a unit vector with the associated direction.
    - `public static Direction fromPoint2D(Point2D direction)`: returns a `Direction` with the same direction as `direction` vector.
    - `public Direction rotate90()`: returns the current direction, rotated by 90 degrees clockwise.
    - `public Direction rotate180()`: returns the current direction, rotated by 180 degrees clockwise.
    - `public Direction rotate270()`: returns the current direction, rotated by 270 degrees clockwise.
    - `public boolean isHorizontal()`: returns `isHorizontal`.
    - `public boolean isVertical()`: returns `isVertical`.
    - `public int getDx()`: returns `dx`.
    - `public int getDy()`: returns `dy`.
    - `public boolean isPerpendicularTo(Direction direction)`: returns `true` if `this` is perpendicular to `direction`.

  - ### `public class Level`:
    This class is used to represent a _level_ in the game.
    - `public static final double TILE_SIZE`: stores the size of each tile in the _tilemap_.
	- `public static final Point2D DEFAULT_GRAVITY`: stores the default gravity values.
	- `public static final Image MENU_BACKGROUND`: stores the default background image of the main menu.
	- `public static final Image DEFAULT_BACKGROUND`: stores the default background image used in _levels_.
	- `public static final Level MENU`: stores the main menu.
	- `private final String[] data`: stores _tilemap_ data.
	- `private final String[] messages`: stores messages.
	- `private double width`: stores width of the _level_.
	- `private double height`: stores height of the _level_.
	- `private double gravityX, gravityY`: stores gravity values.
	- `private String background`: stores background image location.
	- `private String music`: stores background music location.
	- `public Level(String[] data)`: constructor, sets `data` according to passed arguments, then sets `messages` to an empty array of `Strings`, `gravity` to `DEFAULT_GRAVITY`, `background` and `music` to `null`.
	- `public Level(String[] data, String[] messages, Point2D gravity, String background, String music)`: constructor, sets `data`, `messages`, `gravity`, `background`, `music` according to passed arguments.
	- `public static Level fromJson(String json) throws JsonSyntaxException`: returns a `Level` with data loaded from JSON-encoded `String` `json`, throws `JsonSyntaxException` if `json` is badly formed.
	- `public String toJson()`: returns a JSON-encoded `String` representation of the _level_.
	- `public String[] getData()`: returns `data`.
	- `public String[] getMessages()`: returns `messages`.
	- `public double getWidth()`: returns `width`.
	- `public void setWidth(double width)`: sets `width` according to passed arguments.
	- `public double getHeight()`: returns `height`.
	- `public void setHeight(double height)`: sets `height` according to passed arguments.
	- `public Point2D getGravity()`: returns `gravity`.
    - `public void setGravity(Point2D gravity)`: sets `gravity` according to passed arguments.
	- `public String getBackground()`: returns `background`.
	- `public void setBackground(String background)`: sets `background` according to passed arguments.
	- `public String getMusic()`: returns `music`.
	- `public void setMusic(String music)`: sets `music` according to passed arguments.

  - ### `public class PrioritizedPoint2D implements Comparable<PrioritizedPoint2D>`:
    This class is used to create a `Point2D` wrapper which can be sorted by certain `priority` values. It is used in `CameraSystem` to determine the highest priority target for the camera.
    - `private Point2D point2D`: stores the `Point2D` value.
	- `private int priority`: stores the priority.
	- `public PrioritizedPoint2D(Point2D point2D, int priority)`: constructor, sets `point2D` and `priority` according to passed arguments.
	- `public PrioritizedPoint2D(double x, double y, int priority)`: constructor, sets `point2D` and `priority` according to passed arguments.
	- `public Point2D getPoint2D()`: returns `point2D`.
	- `public void setPoint2D()`: sets `point2D` and `priority` according to passed arguments.
	- `public Integer getPriority()`: returns `priority`.
	- `public void setPriority(int priority)`: sets `priority` according to passed arguments.
	- `public int compareTo(PrioritizedPoint2D other)`: compares the value of `this.priority` with `other.priority`.

____

# UML Class Diagrams
In addition to the main UML class diagram, class diagrams of each package are provided here as well.

## all packages
![all](https://raw.githubusercontent.com/blead/chrone/master/uml_all.png)

## `components`
![components](https://raw.githubusercontent.com/blead/chrone/master/uml_components.png)

## `core`
![core](https://raw.githubusercontent.com/blead/chrone/master/uml_core.png)

## `entities`
![entities](https://raw.githubusercontent.com/blead/chrone/master/uml_entities.png)

## `intents`
![intents](https://raw.githubusercontent.com/blead/chrone/master/uml_intents.png)

## `intents`
![intents](https://raw.githubusercontent.com/blead/chrone/master/uml_intents.png)

## `main`
![main](https://raw.githubusercontent.com/blead/chrone/master/uml_main.png)

## `renderables`
![renderables](https://raw.githubusercontent.com/blead/chrone/master/uml_renderables.png)

## `systems`
![systems](https://raw.githubusercontent.com/blead/chrone/master/uml_systems.png)

## `utils`
![utils](https://raw.githubusercontent.com/blead/chrone/master/uml_utils.png)










package rogue;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * A room within the dungeon - contains monsters, treasure,
 * doors out, etc.
 */
public class Room  {

    private int roomWidth;
    private int roomHeight;
    private int roomId;
    private int nDoor = -1;
    private int sDoor = -1;
    private int eDoor = -1;
    private int wDoor = -1;
    private ArrayList<Item> roomItems = new ArrayList<Item>();
    private Player roomPlayer = null;

/**
 * Default constructor for room that intializes room to default values.
 */
    public Room() {
      setWidth(2);
      setHeight(2);
      setId(0);
    }
/**
 * Default constructor for room that intializes room to default values.
 *@param jsonRoom jsonObject that contains all of the values needed for a room
 */
    public Room(JSONObject jsonRoom) {
      Integer integerId = Integer.decode(jsonRoom.get("id").toString());
      Boolean isPlayer = Boolean.parseBoolean(jsonRoom.get("start").toString());
      Integer integerHeight = Integer.decode(jsonRoom.get("height").toString());
      Integer integerWidth = Integer.decode(jsonRoom.get("width").toString());

      if (isPlayer.booleanValue()) {
        roomPlayer = new Player();
      }

      setId(integerId);
      setHeight(integerHeight);
      setWidth(integerWidth);

      for (Object door : (JSONArray) jsonRoom.get("doors")) {
        JSONObject jsonDoor = (JSONObject) door;
        setDoor(jsonDoor.get("dir").toString(), Integer.decode(jsonDoor.get("id").toString()));
      }

      for (Object item : (JSONArray) jsonRoom.get("loot")) {
        roomItems.add((new Item((JSONObject) item)));
      }
    }

   // Required getter and setters below
/**
 * getter that gets width of a room.
 *@return width of room
 */
    public int getWidth() {
      return (roomWidth);
    }

/**
 * setter that sets width of a room.
 *@param newWidth new width of room
 */
    public void setWidth(int newWidth) {
      roomWidth = newWidth;
    }
/**
 * getter that gets height of a room.
 *@return height of room
 */
    public int getHeight() {
      return (roomHeight);
    }
/**
 * setter that sets height of a room.
 *@param newHeight new height of room
 */
    public void setHeight(int newHeight) {
      roomHeight = newHeight;
    }
/**
 * getter that gets id of a room.
 *@return id of room
 */
    public int getId() {
      return (roomId);
    }
/**
 * setter that sets id of a room.
 *@param newId new id of room
 */
    public void setId(int newId) {
      roomId = newId;
    }

    public ArrayList<Item> getRoomItems() {
      return (roomItems);
    }

    public void setRoomItems(ArrayList<Item> newRoomItems) {
      roomItems = newRoomItems;
    }

    public Player getPlayer() {
      return (roomPlayer);
    }

    public void setPlayer(Player newPlayer) {
      roomPlayer = newPlayer;
    }

    public int getDoor(String direction) {
      if (direction.equals("N")) {
        return (nDoor);
      } else if (direction.equals("S")) {
        return (sDoor);
      } else if (direction.equals("E")) {
        return (eDoor);
      } else {
        return (wDoor);
      }
    }

/*
direction is one of NSEW
location is a number between 0 and the length of the wall
*/

    public void setDoor(String direction, int location) {
      if (direction.equals("N")) {
        nDoor = location;
      } else if (direction.equals("S")) {
        sDoor = location;
      } else if (direction.equals("E")) {
        eDoor = location;
      } else {
        wDoor = location;
      }
    }

    public boolean isPlayerInRoom() {
      return !(roomPlayer == null);
    }

   /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents.
    * @return (String) String representation of how the room looks.
    */

    public String displayRoom() {
      String roomDisplayString = "";
      String[][] roomDisplayArray = new String[roomHeight][roomWidth];

      roomDisplayArray = initalizeRoomDisplayArray(roomDisplayArray);
      roomDisplayArray = addDoorsToRoomDisplayArray(roomDisplayArray);
      roomDisplayArray = addContentsToRoomDisplayArray(roomDisplayArray);
      roomDisplayString = convertDisplayArrayToString(roomDisplayArray, roomDisplayString);

      return (roomDisplayString);
    }

    public String[][] initalizeRoomDisplayArray(String[][] roomDisplayArray) {
      for (int x = 0; x < roomWidth; x++) {
        roomDisplayArray[0][x] = "NS_WALL";
      }

      for (int y = 1; y < roomHeight - 1; y++) {
        int x = 0;
        roomDisplayArray[y][x] = "EW_WALL";
        for (x = 1; x < roomWidth - 1; x++) {
          roomDisplayArray[y][x] = "FLOOR";
        }
        roomDisplayArray[y][x] = "EW_WALL";
      }

      for (int x = 0; x < roomWidth; x++) {
        roomDisplayArray[roomHeight - 1][x] = "NS_WALL";
      }

      return (roomDisplayArray);
    }

    public String[][] addDoorsToRoomDisplayArray(String[][] roomDisplayArray) {
      if (nDoor != -1) {
        roomDisplayArray[0][nDoor] = "DOOR";
      }
      if (eDoor != -1) {
        roomDisplayArray[eDoor][0] = "DOOR";
      }
      if (sDoor != -1) {
        roomDisplayArray[roomHeight - 1][sDoor] = "DOOR";
      }
      if (wDoor != -1) {
        roomDisplayArray[wDoor][roomWidth - 1] = "DOOR";
      }

      return (roomDisplayArray);
    }

    public String[][] addContentsToRoomDisplayArray(String[][] roomDisplayArray) {
      if (isPlayerInRoom()) {
        roomDisplayArray[(int) roomPlayer.getXyLocation().getY()][(int) roomPlayer.getXyLocation().getX()] = "PLAYER";
      }

      for (int i = 0; i < roomItems.size(); i++) {
        roomDisplayArray[(int) roomItems.get(i).getXyLocation().getY()]
        [(int) roomItems.get(i).getXyLocation().getX()] = "ITEM";
      }

      return (roomDisplayArray);
    }

    public String convertDisplayArrayToString(String[][]roomDisplayArray, String roomDisplayString) {
      for (int y = 0; y < roomHeight; y++) {
        for (int x = 0; x < roomWidth; x++) {
          roomDisplayString += roomDisplayArray[y][x];
        }
        roomDisplayString += '\n';
      }

      roomDisplayString += '\n';
      roomDisplayString += '\n';

      return (roomDisplayString);
    }
}

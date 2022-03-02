package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Direction;

public class FaceUVsItem {
    @SerializedName("down")
    private FaceItem down;
    @SerializedName("east")
    private FaceItem east;
    @SerializedName("north")
    private FaceItem north;
    @SerializedName("south")
    private FaceItem south;
    @SerializedName("up")
    private FaceItem up;
    @SerializedName("west")
    private FaceItem west;

    public static FaceUVsItem singleSouthFace() {
        FaceUVsItem faces = new FaceUVsItem();
        faces.north = FaceItem.empty();
        faces.east = FaceItem.empty();
        faces.west = FaceItem.empty();
        faces.south = FaceItem.single16X();
        faces.up = FaceItem.empty();
        faces.down = FaceItem.empty();
        return faces;
    }

    public FaceItem getFace(Direction direction) {
        switch (direction) {
            case EAST:
                return west;
            case WEST:
                return east;
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case UP:
                return down;
            case DOWN:
            default:
                return up;
        }
    }
}

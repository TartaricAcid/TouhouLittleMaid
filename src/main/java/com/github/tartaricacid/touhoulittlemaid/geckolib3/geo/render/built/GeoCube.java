package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.VectorUtils;
import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class GeoCube {
    public GeoQuad[] quads = new GeoQuad[6];
    public Vector3f pivot;
    public Vector3f rotation;
    public Vector3f size = new Vector3f();
    public double inflate;
    public Boolean mirror;

    private GeoCube(double[] size) {
        if (size.length >= 3) {
            this.size.set((float) size[0], (float) size[1], (float) size[2]);
        }
    }

    public static GeoCube createFromPojoCube(Cube cubeIn, ModelProperties properties, Double boneInflate, Boolean mirror) {
        GeoCube cube = new GeoCube(cubeIn.getSize());

        UvUnion uvUnion = cubeIn.getUv();
        UvFaces faces = uvUnion.faceUV;
        boolean isBoxUV = uvUnion.isBoxUV;
        cube.mirror = cubeIn.getMirror();
        cube.inflate = cubeIn.getInflate() == null ? (boneInflate == null ? 0 : boneInflate) : cubeIn.getInflate() / 16;

        float textureHeight = properties.getTextureHeight().floatValue();
        float textureWidth = properties.getTextureWidth().floatValue();

        Vec3 size = VectorUtils.fromArray(cubeIn.getSize());
        Vec3 origin = VectorUtils.fromArray(cubeIn.getOrigin());
        origin = new Vec3(-(origin.x + size.x) / 16, origin.y / 16, origin.z / 16);

        size = size.multiply(0.0625f, 0.0625, 0.0625f);

        Vector3f rotation = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(cubeIn.getRotation()));
        rotation.mul(-1, -1, 1);

        rotation.setX((float) Math.toRadians(rotation.x()));
        rotation.setY((float) Math.toRadians(rotation.y()));
        rotation.setZ((float) Math.toRadians(rotation.z()));

        Vector3f pivot = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(cubeIn.getPivot()));
        pivot.mul(-1, 1, 1);

        cube.pivot = pivot;
        cube.rotation = rotation;

        GeoVertex P1 = new GeoVertex(origin.x - cube.inflate, origin.y - cube.inflate, origin.z - cube.inflate);
        GeoVertex P2 = new GeoVertex(origin.x - cube.inflate, origin.y - cube.inflate,
                origin.z + size.z + cube.inflate);
        GeoVertex P3 = new GeoVertex(origin.x - cube.inflate, origin.y + size.y + cube.inflate,
                origin.z - cube.inflate);
        GeoVertex P4 = new GeoVertex(origin.x - cube.inflate, origin.y + size.y + cube.inflate,
                origin.z + size.z + cube.inflate);
        GeoVertex P5 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y - cube.inflate,
                origin.z - cube.inflate);
        GeoVertex P6 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y - cube.inflate,
                origin.z + size.z + cube.inflate);
        GeoVertex P7 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y + size.y + cube.inflate,
                origin.z - cube.inflate);
        GeoVertex P8 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y + size.y + cube.inflate,
                origin.z + size.z + cube.inflate);

        GeoQuad quadWest;
        GeoQuad quadEast;
        GeoQuad quadNorth;
        GeoQuad quadSouth;
        GeoQuad quadUp;
        GeoQuad quadDown;

        if (!isBoxUV) {
            FaceUv west = faces.getWest();
            FaceUv east = faces.getEast();
            FaceUv north = faces.getNorth();
            FaceUv south = faces.getSouth();
            FaceUv up = faces.getUp();
            FaceUv down = faces.getDown();

            quadWest = west == null ? null
                    : new GeoQuad(new GeoVertex[]{P4, P3, P1, P2}, west.getUv(), west.getUvSize(), textureWidth,
                    textureHeight, cubeIn.getMirror(), Direction.WEST);
            quadEast = east == null ? null
                    : new GeoQuad(new GeoVertex[]{P7, P8, P6, P5}, east.getUv(), east.getUvSize(), textureWidth,
                    textureHeight, cubeIn.getMirror(), Direction.EAST);
            quadNorth = north == null ? null
                    : new GeoQuad(new GeoVertex[]{P3, P7, P5, P1}, north.getUv(), north.getUvSize(), textureWidth,
                    textureHeight, cubeIn.getMirror(), Direction.NORTH);
            quadSouth = south == null ? null
                    : new GeoQuad(new GeoVertex[]{P8, P4, P2, P6}, south.getUv(), south.getUvSize(), textureWidth,
                    textureHeight, cubeIn.getMirror(), Direction.SOUTH);
            quadUp = up == null ? null
                    : new GeoQuad(new GeoVertex[]{P4, P8, P7, P3}, up.getUv(), up.getUvSize(), textureWidth,
                    textureHeight, cubeIn.getMirror(), Direction.UP);
            quadDown = down == null ? null
                    : new GeoQuad(new GeoVertex[]{P1, P5, P6, P2}, down.getUv(), down.getUvSize(), textureWidth,
                    textureHeight, cubeIn.getMirror(), Direction.DOWN);

            if (Boolean.TRUE.equals(cubeIn.getMirror()) || Boolean.TRUE.equals(mirror)) {
                quadWest = west == null ? null
                        : new GeoQuad(new GeoVertex[]{P7, P8, P6, P5}, west.getUv(), west.getUvSize(), textureWidth,
                        textureHeight, cubeIn.getMirror(), Direction.WEST);
                quadEast = east == null ? null
                        : new GeoQuad(new GeoVertex[]{P4, P3, P1, P2}, east.getUv(), east.getUvSize(), textureWidth,
                        textureHeight, cubeIn.getMirror(), Direction.EAST);
                quadNorth = north == null ? null
                        : new GeoQuad(new GeoVertex[]{P3, P7, P5, P1}, north.getUv(), north.getUvSize(), textureWidth,
                        textureHeight, cubeIn.getMirror(), Direction.NORTH);
                quadSouth = south == null ? null
                        : new GeoQuad(new GeoVertex[]{P8, P4, P2, P6}, south.getUv(), south.getUvSize(), textureWidth,
                        textureHeight, cubeIn.getMirror(), Direction.SOUTH);
                quadUp = up == null ? null
                        : new GeoQuad(new GeoVertex[]{P1, P5, P6, P2}, up.getUv(), up.getUvSize(), textureWidth,
                        textureHeight, cubeIn.getMirror(), Direction.UP);
                quadDown = down == null ? null
                        : new GeoQuad(new GeoVertex[]{P4, P8, P7, P3}, down.getUv(), down.getUvSize(), textureWidth,
                        textureHeight, cubeIn.getMirror(), Direction.DOWN);
            }
        } else {
            double[] uv = cubeIn.getUv().boxUVCoords;
            Vec3 uvSize = VectorUtils.fromArray(cubeIn.getSize());
            uvSize = new Vec3(Math.floor(uvSize.x), Math.floor(uvSize.y), Math.floor(uvSize.z));

            quadWest = new GeoQuad(new GeoVertex[]{P4, P3, P1, P2},
                    new double[]{uv[0] + uvSize.z + uvSize.x, uv[1] + uvSize.z}, new double[]{uvSize.z, uvSize.y},
                    textureWidth, textureHeight, cubeIn.getMirror(), Direction.WEST);
            quadEast = new GeoQuad(new GeoVertex[]{P7, P8, P6, P5}, new double[]{uv[0], uv[1] + uvSize.z},
                    new double[]{uvSize.z, uvSize.y}, textureWidth, textureHeight, cubeIn.getMirror(),
                    Direction.EAST);
            quadNorth = new GeoQuad(new GeoVertex[]{P3, P7, P5, P1},
                    new double[]{uv[0] + uvSize.z, uv[1] + uvSize.z}, new double[]{uvSize.x, uvSize.y},
                    textureWidth, textureHeight, cubeIn.getMirror(), Direction.NORTH);
            quadSouth = new GeoQuad(new GeoVertex[]{P8, P4, P2, P6},
                    new double[]{uv[0] + uvSize.z + uvSize.x + uvSize.z, uv[1] + uvSize.z},
                    new double[]{uvSize.x, uvSize.y}, textureWidth, textureHeight, cubeIn.getMirror(),
                    Direction.SOUTH);
            quadUp = new GeoQuad(new GeoVertex[]{P4, P8, P7, P3}, new double[]{uv[0] + uvSize.z, uv[1]},
                    new double[]{uvSize.x, uvSize.z}, textureWidth, textureHeight, cubeIn.getMirror(), Direction.UP);
            quadDown = new GeoQuad(new GeoVertex[]{P1, P5, P6, P2},
                    new double[]{uv[0] + uvSize.z + uvSize.x, uv[1] + uvSize.z},
                    new double[]{uvSize.x, -uvSize.z}, textureWidth, textureHeight, cubeIn.getMirror(),
                    Direction.DOWN);

            if (Boolean.TRUE.equals(cubeIn.getMirror()) || Boolean.TRUE.equals(mirror)) {
                quadWest = new GeoQuad(new GeoVertex[]{P7, P8, P6, P5},
                        new double[]{uv[0] + uvSize.z + uvSize.x, uv[1] + uvSize.z},
                        new double[]{uvSize.z, uvSize.y}, textureWidth, textureHeight, cubeIn.getMirror(),
                        Direction.WEST);
                quadEast = new GeoQuad(new GeoVertex[]{P4, P3, P1, P2}, new double[]{uv[0], uv[1] + uvSize.z},
                        new double[]{uvSize.z, uvSize.y}, textureWidth, textureHeight, cubeIn.getMirror(),
                        Direction.EAST);
                quadNorth = new GeoQuad(new GeoVertex[]{P3, P7, P5, P1},
                        new double[]{uv[0] + uvSize.z, uv[1] + uvSize.z}, new double[]{uvSize.x, uvSize.y},
                        textureWidth, textureHeight, cubeIn.getMirror(), Direction.NORTH);
                quadSouth = new GeoQuad(new GeoVertex[]{P8, P4, P2, P6},
                        new double[]{uv[0] + uvSize.z + uvSize.x + uvSize.z, uv[1] + uvSize.z},
                        new double[]{uvSize.x, uvSize.y}, textureWidth, textureHeight, cubeIn.getMirror(),
                        Direction.SOUTH);
                quadUp = new GeoQuad(new GeoVertex[]{P4, P8, P7, P3}, new double[]{uv[0] + uvSize.z, uv[1]},
                        new double[]{uvSize.x, uvSize.z}, textureWidth, textureHeight, cubeIn.getMirror(), Direction.UP);
                quadDown = new GeoQuad(new GeoVertex[]{P1, P5, P6, P2},
                        new double[]{uv[0] + uvSize.z + uvSize.x, uv[1] + uvSize.z},
                        new double[]{uvSize.x, -uvSize.z}, textureWidth, textureHeight, cubeIn.getMirror(),
                        Direction.DOWN);
            }
        }

        cube.quads[0] = quadWest;
        cube.quads[1] = quadEast;
        cube.quads[2] = quadNorth;
        cube.quads[3] = quadSouth;
        cube.quads[4] = quadUp;
        cube.quads[5] = quadDown;
        return cube;
    }
}

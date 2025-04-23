package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.VectorUtils;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GeoMesh {
    /**
     * Down, Up, North, South, West, East
     */
    public static final int FACE_COUNT = 6;

    private final int cubeCount;
    private final int[] faces;
    private final Vector3f[] position;
    private final Vector3f[] dx;
    private final Vector3f[] dy;
    private final Vector3f[] dz;
    private final float[] u0;
    private final float[] v0;
    private final float[] u1;
    private final float[] v1;


    public GeoMesh(int cubeCount, int[] faces, Vector3f[] position, Vector3f[] dx, Vector3f[] dy, Vector3f[] dz, float[] u0, float[] v0, float[] u1, float[] v1) {
        this.cubeCount = cubeCount;
        this.faces = faces;
        this.position = position;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
    }

    static public class GeoMeshBuilder {
        private static final float DEGREES_TO_RADIANS = 0.017453292519943295f;

        private final Matrix4f poseMatrix = new Matrix4f();
        private final int cubeCount;

        private final int[] FACES;
        private final Vector3f[] POSITION;
        private final Vector3f[] DX;
        private final Vector3f[] DY;
        private final Vector3f[] DZ;
        private final float[] U0;
        private final float[] V0;
        private final float[] U1;
        private final float[] V1;

        private int index = 0;

        public GeoMeshBuilder(int cubeCount) {
            this.cubeCount = cubeCount;
            this.FACES = new int[cubeCount];
            this.POSITION = new Vector3f[cubeCount];
            this.DX = new Vector3f[cubeCount];
            this.DY = new Vector3f[cubeCount];
            this.DZ = new Vector3f[cubeCount];
            this.U0 = new float[cubeCount * FACE_COUNT];
            this.V0 = new float[cubeCount * FACE_COUNT];
            this.U1 = new float[cubeCount * FACE_COUNT];
            this.V1 = new float[cubeCount * FACE_COUNT];

            for (int i = 0; i < cubeCount; i++) {
                POSITION[i] = new Vector3f();
                DX[i] = new Vector3f();
                DY[i] = new Vector3f();
                DZ[i] = new Vector3f();
            }
        }

        public void addCube(Cube cubeIn, ModelProperties properties, Double boneInflate, Boolean mirror) {
            float textureHeight = properties.getTextureHeight().floatValue();
            float textureWidth = properties.getTextureWidth().floatValue();

            float inflate = cubeIn.getInflate() == null ? boneInflate.floatValue() : cubeIn.getInflate().floatValue();
            inflate /= 16f;

            Vector3f size = VectorUtils.fromArray(cubeIn.getSize()).toVector3f().mul(0.0625f);
            Vector3f origin = VectorUtils.fromArray(cubeIn.getOrigin()).toVector3f().mul(0.0625f);
            float diff = Math.max(0.001f, size.x + inflate * 2);

            Vector3f P1 = new Vector3f(-(origin.x + size.x) - inflate, origin.y - inflate, origin.z - inflate);
            if (Boolean.TRUE.equals(mirror)) {
                P1.x += diff;
                diff = -diff;
            }

            Vector3f rotation = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(cubeIn.getRotation()));

            rotation.mul(-DEGREES_TO_RADIANS, -DEGREES_TO_RADIANS, DEGREES_TO_RADIANS);

            Vector3f pivot = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(cubeIn.getPivot()));
            pivot.mul(-0.0625f, 0.0625f, 0.0625f);

            poseMatrix.identity();
            Quaternionf quat = new Quaternionf().rotateZYX(rotation.z(), rotation.y(), rotation.x());
            poseMatrix.rotateAround(quat, pivot.x(), pivot.y(), pivot.z());
            P1.mulPosition(poseMatrix);

            POSITION[index].set(P1);

            DX[index].set(poseMatrix.m00() * diff, poseMatrix.m01() * diff, poseMatrix.m02() * diff);

            diff = Math.max(0.001f, size.y + inflate * 2);
            DY[index].set(poseMatrix.m10() * diff, poseMatrix.m11() * diff, poseMatrix.m12() * diff);

            diff = Math.max(0.001f, size.z + inflate * 2);
            DZ[index].set(poseMatrix.m20() * diff, poseMatrix.m21() * diff, poseMatrix.m22() * diff);

            UvUnion uvUnion = cubeIn.getUv();
            boolean isBoxUV = uvUnion.isBoxUV;

            int faces = Boolean.TRUE.equals(mirror) ? 0b1000000 : 0;
            int faceIndex = index * FACE_COUNT;
            if (!isBoxUV) {
                UvFaces faceUV = uvUnion.faceUV;
                FaceUv west = faceUV.getWest();
                FaceUv east = faceUV.getEast();
                FaceUv north = faceUV.getNorth();
                FaceUv south = faceUV.getSouth();
                FaceUv up = faceUV.getUp();
                FaceUv down = faceUV.getDown();

                if (down != null) {
                    faces |= 0b000001;
                    double[] uv = down.getUv();
                    double[] uvSize = down.getUvSize();

                    U0[faceIndex] = (float) uv[0] / textureWidth;
                    V0[faceIndex] = (float) uv[1] / textureHeight;
                    U1[faceIndex] = ((float) uv[0] + (float) uvSize[0]) / textureWidth;
                    V1[faceIndex] = ((float) uv[1] + (float) uvSize[1]) / textureHeight;
                }
                if (up != null) {
                    faces |= 0b000010;
                    double[] uv = up.getUv();
                    double[] uvSize = up.getUvSize();

                    U0[faceIndex + 1] = (float) uv[0] / textureWidth;
                    V0[faceIndex + 1] = (float) uv[1] / textureHeight;
                    U1[faceIndex + 1] = ((float) uv[0] + (float) uvSize[0]) / textureWidth;
                    V1[faceIndex + 1] = ((float) uv[1] + (float) uvSize[1]) / textureHeight;
                }
                if (north != null) {
                    faces |= 0b000100;
                    double[] uv = north.getUv();
                    double[] uvSize = north.getUvSize();

                    U0[faceIndex + 2] = (float) uv[0] / textureWidth;
                    V0[faceIndex + 2] = (float) uv[1] / textureHeight;
                    U1[faceIndex + 2] = ((float) uv[0] + (float) uvSize[0]) / textureWidth;
                    V1[faceIndex + 2] = ((float) uv[1] + (float) uvSize[1]) / textureHeight;
                }
                if (south != null) {
                    faces |= 0b001000;
                    double[] uv = south.getUv();
                    double[] uvSize = south.getUvSize();

                    U0[faceIndex + 3] = (float) uv[0] / textureWidth;
                    V0[faceIndex + 3] = (float) uv[1] / textureHeight;
                    U1[faceIndex + 3] = ((float) uv[0] + (float) uvSize[0]) / textureWidth;
                    V1[faceIndex + 3] = ((float) uv[1] + (float) uvSize[1]) / textureHeight;
                }
                if (west != null) {
                    faces |= 0b010000;
                    double[] uv = west.getUv();
                    double[] uvSize = west.getUvSize();

                    U0[faceIndex + 4] = (float) uv[0] / textureWidth;
                    V0[faceIndex + 4] = (float) uv[1] / textureHeight;
                    U1[faceIndex + 4] = ((float) uv[0] + (float) uvSize[0]) / textureWidth;
                    V1[faceIndex + 4] = ((float) uv[1] + (float) uvSize[1]) / textureHeight;
                }
                if (east != null) {
                    faces |= 0b100000;
                    double[] uv = east.getUv();
                    double[] uvSize = east.getUvSize();

                    U0[faceIndex + 5] = (float) uv[0] / textureWidth;
                    V0[faceIndex + 5] = (float) uv[1] / textureHeight;
                    U1[faceIndex + 5] = ((float) uv[0] + (float) uvSize[0]) / textureWidth;
                    V1[faceIndex + 5] = ((float) uv[1] + (float) uvSize[1]) / textureHeight;
                }
            } else {
                faces = 0b111111;
                double[] uv = cubeIn.getUv().boxUVCoords;
                Vec3 uvSize = VectorUtils.fromArray(cubeIn.getSize());
                uvSize = new Vec3(Math.floor(uvSize.x), Math.floor(uvSize.y), Math.floor(uvSize.z));

                float u0 = (float) (uv[0] + uvSize.z + uvSize.x);
                float v0 = (float) (uv[1] + uvSize.z);
                U0[faceIndex] = u0 / textureWidth;
                V0[faceIndex] = v0 / textureHeight;
                U1[faceIndex] = (u0 + (float) uvSize.x) / textureWidth;
                V1[faceIndex] = (v0 - (float) uvSize.z) / textureHeight;

                u0 = (float) (uv[0] + uvSize.z);
                v0 = (float) (uv[1]);
                U0[faceIndex + 1] = u0 / textureWidth;
                V0[faceIndex + 1] = v0 / textureHeight;
                U1[faceIndex + 1] = (u0 + (float) uvSize.x) / textureWidth;
                V1[faceIndex + 1] = (v0 + (float) uvSize.z) / textureHeight;

                u0 = (float) (uv[0] + uvSize.z);
                v0 = (float) (uv[1] + uvSize.z);
                U0[faceIndex + 2] = u0 / textureWidth;
                V0[faceIndex + 2] = v0 / textureHeight;
                U1[faceIndex + 2] = (u0 + (float) uvSize.x) / textureWidth;
                V1[faceIndex + 2] = (v0 + (float) uvSize.y) / textureHeight;

                u0 = (float) (uv[0] + uvSize.z + uvSize.x + uvSize.z);
                v0 = (float) (uv[1] + uvSize.z);
                U0[faceIndex + 3] = u0 / textureWidth;
                V0[faceIndex + 3] = v0 / textureHeight;
                U1[faceIndex + 3] = (u0 + (float) uvSize.x) / textureWidth;
                V1[faceIndex + 3] = (v0 + (float) uvSize.y) / textureHeight;

                u0 = (float) (uv[0] + uvSize.z + uvSize.x);
                v0 = (float) (uv[1] + uvSize.z);
                U0[faceIndex + 4] = u0 / textureWidth;
                V0[faceIndex + 4] = v0 / textureHeight;
                U1[faceIndex + 4] = (u0 + (float) uvSize.z) / textureWidth;
                V1[faceIndex + 4] = (v0 + (float) uvSize.y) / textureHeight;

                u0 = (float) (uv[0]);
                v0 = (float) (uv[1] + uvSize.z);
                U0[faceIndex + 5] = u0 / textureWidth;
                V0[faceIndex + 5] = v0 / textureHeight;
                U1[faceIndex + 5] = (u0 + (float) uvSize.z) / textureWidth;
                V1[faceIndex + 5] = (v0 + (float) uvSize.y) / textureHeight;
            }
            FACES[index] = faces;

            index++;
        }

        public GeoMesh build() {
            return new GeoMesh(cubeCount, FACES, POSITION, DX, DY, DZ, U0, V0, U1, V1);
        }
    }

    public int faces(int index) {
        return faces[index];
    }

    public Vector3f position(int index) {
        return position[index];
    }

    public Vector3f dx(int index) {
        return dx[index];
    }

    public Vector3f dy(int index) {
        return dy[index];
    }

    public Vector3f dz(int index) {
        return dz[index];
    }

    public float u0(int index, int face) {
        return u0[index * FACE_COUNT + face];
    }

    public float v0(int index, int face) {
        return v0[index * FACE_COUNT + face];
    }

    public float u1(int index, int face) {
        return u1[index * FACE_COUNT + face];
    }

    public float v1(int index, int face) {
        return v1[index * FACE_COUNT + face];
    }

    public float downU0(int index) {
        return u0(index, 0);
    }

    public float downV0(int index) {
        return v0(index, 0);
    }

    public float downU1(int index) {
        return u1(index, 0);
    }

    public float downV1(int index) {
        return v1(index, 0);
    }

    public float upU0(int index) {
        return u0(index, 1);
    }

    public float upV0(int index) {
        return v0(index, 1);
    }

    public float upU1(int index) {
        return u1(index, 1);
    }

    public float upV1(int index) {
        return v1(index, 1);
    }

    public float northU0(int index) {
        return u0(index, 2);
    }

    public float northV0(int index) {
        return v0(index, 2);
    }

    public float northU1(int index) {
        return u1(index, 2);
    }

    public float northV1(int index) {
        return v1(index, 2);
    }

    public float southU0(int index) {
        return u0(index, 3);
    }

    public float southV0(int index) {
        return v0(index, 3);
    }

    public float southU1(int index) {
        return u1(index, 3);
    }

    public float southV1(int index) {
        return v1(index, 3);
    }

    public float westU0(int index) {
        return u0(index, 4);
    }

    public float westV0(int index) {
        return v0(index, 4);
    }

    public float westU1(int index) {
        return u1(index, 4);
    }

    public float westV1(int index) {
        return v1(index, 4);
    }

    public float eastU0(int index) {
        return u0(index, 5);
    }

    public float eastV0(int index) {
        return v0(index, 5);
    }

    public float eastU1(int index) {
        return u1(index, 5);
    }

    public float eastV1(int index) {
        return v1(index, 5);
    }

    public int getCubeCount() {
        return cubeCount;
    }
}
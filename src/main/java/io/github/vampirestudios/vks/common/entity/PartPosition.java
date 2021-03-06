package io.github.vampirestudios.vks.common.entity;

import net.minecraft.util.math.Vec3d;

/**
 * Author: MrCrayfish
 */
public class PartPosition
{
    public static final PartPosition DEFAULT = new PartPosition(1.0);

    private Vec3d translate = Vec3d.ZERO;
    private Vec3d rotation = Vec3d.ZERO;
    private double scale;

    public PartPosition(double scale)
    {
        this.scale = scale;
    }

    public PartPosition(double x, double y, double z, double scale)
    {
        this.translate = new Vec3d(x, y, z);
        this.scale = scale;
    }

    public PartPosition(double x, double y, double z, double rotX, double rotY, double rotZ, double scale)
    {
        this.translate = new Vec3d(x, y, z);
        this.rotation = new Vec3d(rotX, rotY, rotZ);
        this.scale = scale;
    }

    public Vec3d getTranslate()
    {
        return translate;
    }

    public Vec3d getRotation()
    {
        return rotation;
    }

    public double getX()
    {
        return translate.x;
    }

    public double getY()
    {
        return translate.y;
    }

    public double getZ()
    {
        return translate.z;
    }

    public double getRotX()
    {
        return rotation.x;
    }

    public double getRotY()
    {
        return rotation.y;
    }

    public double getRotZ()
    {
        return rotation.z;
    }

    public double getScale()
    {
        return scale;
    }

    public void update(double x, double y, double z, double rotX, double rotY, double rotZ, double scale)
    {
        this.translate = new Vec3d(x, y, z);
        this.rotation = new Vec3d(rotX, rotY, rotZ);
        this.scale = scale;
    }
}
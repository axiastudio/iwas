package com.axiastudio.iwas;

/**
 * User: tiziano
 * Date: 11/10/14
 * Time: 10:23
 */
public class Stamp {

    private String code;
    private StampType type = StampType.TEXT;
    private DatamatrixSize datamatrixSize = DatamatrixSize._20x20;
    private Float datamatrixScale = 1f;
    private Integer fontSize = 24;
    private Float x = 0f;
    private Float y = 0f;
    private Float rotation = 0f;

    public Stamp(String code) {
        this.code = code;
    }

    public Stamp(String code, StampType type) {
        this.code = code;
        this.type = type;
    }

    public void setDatamatrixSize(DatamatrixSize datamatrixSize) {
        this.datamatrixSize = datamatrixSize;
    }

    public void setScale(Float scale) {
        this.datamatrixScale = scale;
    }

    public void setFontiSize(Integer size) {
        fontSize = size;
    }

    public void setPosition(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public void setRotation(Float rotation) {
        this.rotation = rotation;
    }

    public String getCode() {
        return code;
    }

    public StampType getType() {
        return type;
    }

    public DatamatrixSize getDatamatrixSize() {
        return datamatrixSize;
    }

    public int getDatamatrixWidth() {
        return getDatamatrixDimension(0);
    }
    public int getDatamatrixHeight() {
        return getDatamatrixDimension(1);
    }
    private int getDatamatrixDimension(Integer i) {
        String[] split = datamatrixSize.name().substring(1).split("x");
        return Integer.parseInt(split[i]);
    }
    public Float getDatamatrixScale() {
        return datamatrixScale;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public Float getX() {
        return x;
    }
    public Float getY() {
        return y;
    }

    public Float getRotation() {
        return rotation;
    }
}

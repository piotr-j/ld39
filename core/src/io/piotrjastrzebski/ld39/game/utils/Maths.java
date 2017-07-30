package io.piotrjastrzebski.ld39.game.utils;

public class Maths {
    public static float map(float value, float srcMin, float srcMax, float dstMin, float dstMax) {
        return (value-srcMin)/(srcMax - srcMin) * (dstMax-dstMin) + dstMin;
    }
}

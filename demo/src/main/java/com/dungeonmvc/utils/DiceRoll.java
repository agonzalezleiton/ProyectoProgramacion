package com.dungeonmvc.utils;

import java.util.Random;

public class DiceRoll {
    public enum Dice{
        d4(4),d6(6),d8(8),d10(10),d12(12),d20(20);
        private int faces;

        private Dice(int faces){
            this.faces=faces;
        }

        public int value(){
            return faces;
        }
    }

    public static int roll(Dice dice){
        return new Random().nextInt(dice.value())+1;
    }
}
package com.room227.anotherworld;

public class Dice {
    int dice_number;
    int dice_result;

    int D20_double(){
        int result1 = (int)(Math.random() * 20 + 1);
        int result2 = (int)(Math.random() * 20 + 1);
        if(result1 >= result2)
            return result1;
        else
            return result2;
    }

    int D10_Num(int number_of_dices)
    {
        int result = 0;
        for(int i = 0; i < number_of_dices; i = i + 1)
        {
            result = result + (int)(Math.random() * 10 + 1);
        }
        return result;
    } //保下限的Xd10骰子，且投出中间值概率最高

    int D20_Num(int number_of_dices)
    {
        int result = 0;
        for(int i = 0; i < number_of_dices; i = i + 1)
        {
            result = result + (int)(Math.random() * 20 + 1);
        }
        return result;
    } //保下限的Xd20骰子，且投出中间值概率最高

    int DiceX(int number_of_faces)
    {
        return (int)(Math.random() * number_of_faces + 1);
    } //X面骰子，点数范围[0, X]

    int Dice_for_0_99()
    {
        int ones_place = (int)(Math.random() * 10);
        int tens_place = (int)(Math.random() * 10);
        return 10 * tens_place + ones_place;
    } //两位数骰子，点数范围[0, 99]


    int dice_before_battle(int d1_result)
    {
        if(d1_result <= 5){
            return -5;
        }
        else if(d1_result >= 16){
            return 5;
        }
        else
            return 0;
    } //战斗前的d20，
}

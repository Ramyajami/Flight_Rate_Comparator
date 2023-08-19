package org.example;

import static java.time.Year.isLeap;

public class Main {

    static boolean isValidDate(int d,int m, int y){
        int MAX_VALID_YR = 2050;
        int MIN_VALID_YR = 2023;
        if (y > MAX_VALID_YR || y < MIN_VALID_YR)
            return false;
        if (m < 1 || m > 12)
            return false;
        if (d < 1 || d > 31)
            return false;
        if (m == 2) {
            if (isLeap(y))
                return (d <= 29);
            else
                return (d <= 28);
        }
        if (m == 4 || m == 6 || m == 9 || m == 11)
            return (d <= 30);

        return true;
    }
    public static void main(String[] args){
        Integer m = Integer.parseInt("2");
        String d = "30";
        String y = "2024";


    }
}
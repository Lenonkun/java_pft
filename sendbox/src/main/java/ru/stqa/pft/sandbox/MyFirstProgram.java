package ru.stqa.pft.sandbox;

public class MyFirstProgram {
    public static void main(String[] args) {
        helloVoid("использование метода");
        helloVoid(String.valueOf(kvadrat(4)));

    }


    public static void helloVoid (String s){
        System.out.println(s);
    }
    public static int kvadrat(int num){
        return num*num;
    }
}
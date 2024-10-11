package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       Date date = new Date();
        Scanner scan = new Scanner(System.in);
        String fileName = "transactions.csv";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
           
        }catch(IOException e){
            System.err.println(e);
        }

    }
}

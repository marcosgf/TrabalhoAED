package com.mycompany.trabalhoaed;

import com.mycompany.trabalhoaed.Index;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author marcos
 */
public class Main {

    private static Boolean isPrime(int n) {
        int cont = 0;
        int r = (int) Math.sqrt(n);
        System.out.println(r);
        for (int i = 2; i <= r; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static double hashRegister(String str, int n) {

        double result = 0;
        char[] letras = str.toCharArray();
        for (int j = 0; j < letras.length; j++) {
            
            result = (double)(33 * result) + ((int)letras[j]);
        }
        return result % n;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Index teste = new Index("usda.sql",13);
         teste.ReadDump();
         //teste.PrintStruct();
//        String testesssss = "01029203S27";
//        System.out.println(hashRegister(testesssss,193));
////        
//        System.out.println(isPrime(2));
//        System.out.println(isPrime(4));
//        System.out.println(isPrime(9));
//        System.out.println(isPrime(14));
//        System.out.println(isPrime(15));
//        System.out.println(isPrime(20));
//         */
//        String[] testess = {"data_src", "datsrcln", "deriv_cd", "fd_group", "food_des", "footnote", "nut_data", "nutr_def", "src_cd", "weight"};
//            
//        for (int i = 0; i < testess.length; i++) {
//            System.out.println(testess[i]+" "+hashRegister(testess[i],13 ));
//        }
            

    }

}

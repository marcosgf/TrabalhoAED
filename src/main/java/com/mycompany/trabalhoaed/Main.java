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

    private static Boolean isPrime (int n){
        int cont=0;
        int r = (int) Math.sqrt(n);
        System.out.println(r);
        for(int i =2 ; i <= r ; i++){
            if(n % i == 0 ){
                return false;
            }
        }
        return true;  
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Index teste = new Index("usda.sql",13);
        teste.ReadDump();
        teste.PrintStruct();
//        
//        System.out.println(isPrime(2));
//        System.out.println(isPrime(4));
//        System.out.println(isPrime(9));
//        System.out.println(isPrime(14));
//        System.out.println(isPrime(15));
//        System.out.println(isPrime(20));
//         */
//        String[] testess = {"data_src", "datsrcln", "deriv_cd", "fd_group", "food_des", "footnote", "nut_data", "nutr_def", "src_cd", "weight"};
//        int result = 0;
//        for (int i = 0; i < testess.length; i++) {
//            char[] letras = testess[i].toCharArray();
//            for(int j = 0 ; j < letras.length; j++){
//              result += (int) ((int) letras[j]);
//            }
//            System.out.println(result);
//             result = 53*result % 13;
//            System.out.println(testess[i]+" = "+result);
//            result = 0;
//        }

    }

}

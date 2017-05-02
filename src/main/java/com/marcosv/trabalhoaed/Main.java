package com.marcosv.trabalhoaed;

import java.io.IOException;
import java.util.Scanner;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int tam = 0;
        String file = "";
        Database database;
        System.out.println("Trabalho de implementação para a disciplina de Algoritimos de Estrutura de Dados - PGCC-UFJF");
        System.out.println("Aluno: Marcos Valadão G. Ferreira");
        System.out.println("Professor: Jairo F. de Souza");
        System.out.println("--------------------------------");
        Scanner in = new Scanner(System.in);
        System.out.print("Digite o caminho do arquivo a ser indexado (default = usda.sql) :");
        file = in.nextLine();
        System.out.println("");
        System.out.print("Digite a quantidade de tabelas, de preferência um número primo para o tamanho do vetor de tabelas (default = 13) :");
        tam = in.nextInt();
        System.out.println("");
        if (file.equals("") && tam == 0) {
            database = new Database("usda.sql", 13);
            database.ReadDump();
        } else {
            database = new Database(file, tam);
            database.ReadDump();
        }
        while (true) {
            System.out.println("1 - Imprimir o banco de dados");
            System.out.println("2 - Buscar todos os Registros (sem impressão)");
            System.out.println("3 - Buscar todos os Registros (com impressão)");
            System.out.println("4 - Imprimir estrutura para cada tabela");
            System.out.println("5 - Finalizar");
            int op = in.nextInt();
            switch (op) {
                case 1:
                    database.PrintStruct();
                    break;
                case 2:
                    database.searchAllRegister("search.txt");
                    break;
                case 3:
                    database.searchAllRegister("search.txt", true);
                    break;
                case 4:
                    database.printStructHash();
                    break;
                default:
                    break;
            }
            if(op == 5)break;
        }
    }

}

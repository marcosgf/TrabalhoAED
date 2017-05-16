package com.marcosv.trabalhoaed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            database = new Database(13);
            ReadDump(database,"usda.sql");
        } else {
            database = new Database(tam);
            ReadDump(database,file);
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
    
    static void ReadDump(Database database, String path) throws IOException {
        int j = 0, TSize;
        //BufferedWriter buffWrite = new BufferedWriter(new FileWriter("search.txt"));
        String nameTable,text, id = "";
        String[] filds, info, line;
        NoTable t = null;
        NoRecord[] records;

        text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        line = text.split("\n");
        long tempoInicial = System.currentTimeMillis();
        for (int i = 0; i < line.length; i++) {
            if (line[i].contains("CREATE TABLE ")) {//estamos em uma tabela
                nameTable = line[i].replace("CREATE TABLE ", "");
                nameTable = nameTable.replace(" (", "");
                //novo nó tabela com nome lido 
                i++;
                j = i;
                while (!line[j].equals(");")) {
                    j++;
                }
                filds = new String[j - i];
                j = 0; // quantidade de campos na tabela
                while (!line[i].equals(");")) {//leitura de todos os campos em um vetor de string
                    filds[j] = line[i].split(" ")[4];
                    i++;
                    j++;
                }
                database.insertTable(nameTable, filds, t);

            } else if (line[i].contains("COPY ")) {//estou na lista de elementos de uma tabela
                nameTable = line[i].split(" ")[1];//nome da tabela que se trata os elementos
                t = database.getTable(nameTable);//retorna o NoTable com aquele nome ps: a função getTable será com hashing
                System.out.println(nameTable);
                i++;
                j = i;
                
                while (!line[j].equals("\\.")) {
                    j++;
                }
                String[] keys = database.searchPkey(nameTable,line, j);
                int[] pkeys = t.getPosPkey(keys);
                t.setNroElements(j - i);//numero de elementos daquela tabela
                System.out.println("quantidade de registros: " + (j - i));
                TSize = ((j - i) * 5) / 4;
                records = new NoRecord[TSize];//vetor de registros instanciado com a quantidade de elementos
                while (!line[i].equals("\\.")) {
                    line[i] = line[i].replace("\t", "\t ");
                    info = line[i].split("\t");//separa campos do registro
                    for (int k = 0; k < pkeys.length; k++) {
                        id += info[pkeys[k]].trim();
                    }
                    //buffWrite.append(nameTable + "\t" + id.trim() + "\n");
                    database.insertRegister(info, id.trim(), TSize, records);
                    i++;
                    id = "";
                    info = null;
                }
                t.setElements(records);
                System.out.println("colisões de tabelas :" + database.getColisoesT());
                System.out.println("colisões de registros :" + database.getColisoesR());
                System.out.println("");
                database.setColisoesR(0);
            }
        }
        
        System.out.println("Tempo para inserção de todos os registros: " + (System.currentTimeMillis() - tempoInicial));
        text = null;
        System.gc();
        //buffWrite.close();
    }
}

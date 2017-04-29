package com.mycompany.trabalhoaed;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author marcos
 */
public class Index {

    private String path;
    private NoTable[] tables;
    private int nroTables;
    private int colisoesT = 0, colisoesR = 0;

    public Index(String path, int nroTables) {
        this.path = path;
        this.nroTables = nroTables;
        this.tables = new NoTable[this.nroTables];
    }

    void ReadDump() throws IOException {
        int j = 0, TSize;
        String nameTable, aux, text, id = "";
        int posT = 0, posR = 0;
        String[] filds, tab, info, line;
        NoTable t = null;
        NoRecord[] records;

        text = new String(Files.readAllBytes(Paths.get(this.path)), StandardCharsets.UTF_8);
        line = text.split("\n");
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
                insertTable(nameTable, filds, t);

            } else if (line[i].contains("COPY ")) {//estou na lista de elementos de uma tabela
                nameTable = line[i].split(" ")[1];//nome da tabela que se trata os elementos
                t = getTable(nameTable);//retorna o NoTable com aquele nome ps: a função getTable será com hashing
                System.out.println(nameTable);
                i++;
                j = i;
                String[] keys = searchPkey(nameTable);
                int[] pkeys = t.getPosPkey(keys);
                while (!line[j].equals("\\.")) {
                    j++;
                }
                t.setNroElements(j - i);//numero de elementos daquela tabela
                System.out.println("quantidade de registros: " + (j - i));
                TSize = ((j - i) * 7) / 5;
                records = new NoRecord[TSize];//vetor de registros instanciado com a quantidade de elementos
                while (!line[i].equals("\\.")) {
                    line[i] = line[i].replace("\t", "\t ");
                    info = line[i].split("\t");//separa campos do registro
                    for (int k = 0; k < pkeys.length; k++) {
                        id += info[pkeys[k]].trim();
                    }
                    insertRegister(info, id.trim(), TSize, records);
                    i++;
                    id = "";
                    info = null;
                }
                t.setElements(records);
                System.out.println("colisões de tabelas :" + colisoesT);
                System.out.println("colisões de registros :" + colisoesR);
                System.out.println("");
                colisoesR = 0;

            }

        }

    }

    /**
     * @return the tables
     */
    public NoTable[] getAllTables() {
        return tables;
    }

    /**
     * @param tables the tables to set
     */
    public void setAllTables(NoTable[] tables) {
        this.tables = tables;
    }

    public NoTable getTable(String name) {
        for (int i = 0; i < this.tables.length; i++) {
            if (this.tables[i] != null) {
                if (this.tables[i].getName().equals(name)) {
                    return this.tables[i];
                } else {
                    NoTable aux = this.tables[i];
                    while (aux.getNext() != null) {
                        aux = aux.getNext();
                        if (aux.getName().equals(name)) {
                            return aux;
                        }
                    }
                }
            }

        }
        return null;
    }

    public void PrintStruct() {
        NoRecord auxR;
        NoTable aux;
        for (int i = 0; i < this.tables.length; i++) {
            System.out.println(i);
            if (this.tables[i] != null) {
                System.out.println("\n\n pos=" + i + this.tables[i].getName());
                for (int j = 0; j < this.tables[i].getElements().length; j++) {
                    if (this.tables[i].getElements()[j] != null) {
                        System.out.println("\t\tpos = " + j + " id= " + this.tables[i].getElements()[j].getId() + " ");
                        auxR = this.tables[i].getElements()[j];
                        while (auxR.getNext() != null) {
                            auxR = auxR.getNext();
                            System.out.println("\t\t\tpos = " + j + " id= " + auxR.getId() + "");
                        }
                    }

                }
                aux = this.tables[i];
                while (aux.getNext() != null) {
                    aux = aux.getNext();
                    System.out.println("\n\n pos=" + i + aux.getName());
                    for (int j = 0; j < aux.getElements().length; j++) {
                        if (aux.getElements()[j] != null) {
                            System.out.println("\t\tpos = " + j + " id= " + aux.getElements()[j].getId() + " ");
                            auxR = aux.getElements()[j];
                            while (auxR.getNext() != null) {
                                auxR = auxR.getNext();
                                System.out.println("\t\t\tpos = " + j + " id= " + auxR.getId() + " ");
                            }
                        }

                    }
                }
            }

        }
    }

    private int hashTable(String str) {

        double result = 0;
        char[] letras = str.toCharArray();
        for (int j = 0; j < letras.length; j++) {
            result = (int) (result) + ((int) letras[j]);
        }
        return (int) result % this.nroTables;
    }

    private int hashRegister(String str, int n, int t) {

        long result = 0;
        char[] letras = str.toCharArray();
        for (int j = 0; j < letras.length; j++) {
            result = (long) (5 * result) + ((long) letras[j]);
        }
        return (int) result % n;
    }

    private long hashRegister(String str, int n) {
        long h = 1125899906842597L; // prime
        int len = str.length();

        for (int i = 0; i < len; i++) {
            h = 31 * h + str.charAt(i);
        }
        return h % n;
    }

    private Boolean isPrime(int n) {
        int cont = 0;
        int r = (int) Math.sqrt(n);
        for (int i = 2; i <= r; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private void insertTable(String name, String[] filds, NoTable t) {
        t = new NoTable(name);
        t.setFilds(filds);//armazena o nome dos campos em um vetor de string na tabela t criada
        //hashing para tabela
        int posT = hashTable(t.getName());
        if (this.tables[posT] == null) {
            this.tables[posT] = t;
        } else {
            NoTable aux = this.tables[posT];
            while (aux.getNext() != null) {
                aux = aux.getNext();
            }
            colisoesT++;
            aux.setNext(t);
        }
    }

    private void insertRegister(String[] info, String id, int TSize, NoRecord[] records) {
        NoRecord r = new NoRecord(id);//inicialmente o id será o campo 0 das informações do registro
        r.setInfo(info);//vetor de informações de um registro
        NoRecord aux;
        int posR = (int) hashRegister(id, TSize);
        if (records[posR] == null) {
            records[posR] = r;
        } else {
            aux = records[posR];
            if (aux.getNext() != null) {
                aux = aux.getLast();
            }
            aux.setNext(r);
            colisoesR++;
            records[posR].setLast(r);
        }
    }

    private String[] searchPkey(String nameTable) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(this.path)), StandardCharsets.UTF_8);
        String aux;
        String[] line = text.split("\n"), pkey = null;
        for (int i = 0; i < line.length; i++) {
            if (line[i].contains("ALTER TABLE ONLY " + nameTable)) {
                nameTable = line[i].split(" ")[3];
                i++;
                aux = line[i].replace("(", "\t");
                aux = aux.replace(")", "\t");
                aux = aux.split("\t")[1];
                if (line[i].contains(", ")) {
                    pkey = aux.split(", ");
                } else {
                    pkey = new String[1];
                    pkey[0] = aux;
                }
                break;
            }
        }
        if (pkey == null) {
            System.out.println("pkey é null!!!");
        }
        return pkey;
    }
}

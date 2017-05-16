package com.marcosv.trabalhoaed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
public class Database {

    private NoTable[] tables;
    private static int nroTables;
    private int insertions = 0, colisoesT = 0, colisoesR = 0;

    public Database(int nroTables) {
        this.nroTables = nroTables;
        this.tables = new NoTable[this.nroTables];
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

    public void printStructHash(){
        for(NoTable tt : tables){
            if(tt != null){
                tt.printStruct();
                System.out.println("");
            }
        }
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

    public long hashTable(String str) {
        long n = this.nroTables;
        double A = 0.61803399;
        long hash = 0;
        long x = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);

            if ((x = hash & 0xF0000000L) != 0) {
                hash ^= (x >> 24);
            }
            hash &= ~x;
        }
        return (long) (n * ((hash * A) - (long) (hash * A)));
    }

    public long hashRegister(String str, long n) {
        double A = 0.61803399;
        long BitsInUnsignedInt = (long) (4 * 8);
        long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
        long OneEighth = (long) (BitsInUnsignedInt / 8);
        long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        long hash = 0;
        long test = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << OneEighth) + str.charAt(i);

            if ((test = hash & HighBits) != 0) {
                hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
            }
        }
        return (long) (n * ((hash * A) - (long) (hash * A)));
    }

    public void insertTable(String name, String[] filds, NoTable t) {
        t = new NoTable(name);
        t.setFilds(filds);//armazena o nome dos campos em um vetor de string na tabela t criada
        //hashing para tabela
        int posT = (int) hashTable(t.getName());
        if (this.tables[posT] == null) {
            this.tables[posT] = t;
        } else {
            NoTable aux = this.tables[posT];
            while (aux.getNext() != null) {
                aux = aux.getNext();
            }
            setColisoesT(getColisoesT() + 1);
            aux.setNext(t);
        }
    }

    public void insertRegister(String[] info, String id, int TSize, NoRecord[] records) {
        NoRecord r = new NoRecord(id);//inicialmente o id será o campo 0 das informações do registro
        r.setInfo(info);//vetor de informações de um registro
        NoRecord aux;
        insertions++;
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

    public String[] searchPkey(String nameTable, String[] line , int min) throws IOException {
        String aux;
        String[] pkey = null;
        for (int i = min; i < line.length; i++) {
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

    public void searchAllRegister(String file) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
        String[] lines = text.split("\n");
        long tempoInicial = System.currentTimeMillis();
        for (String line : lines) {
            searchRegister(line.split("\t")[0], line.split("\t")[1]);
        }
        System.out.println("Tempo para busca de todos os registros: " + (System.currentTimeMillis() - tempoInicial));
    }

    public void searchRegister(String nameTable, String id) {
        int posT = (int) hashTable(nameTable);
        NoTable t;
        NoRecord r;
        if (this.tables[posT].getName().equals(nameTable)) {
            int posR = (int) hashRegister(id, this.tables[posT].getElements().length);
            if (this.tables[posT].getElements()[posR].getId().equals(id)) {
                for (int i = 0; i < this.tables[posT].getElements()[posR].getInfo().length; i++) {
                    //System.out.print(this.tables[posT].getElements()[posR].getInfo()[i] + " ");
                }
                //System.out.println("");
            } else {
                r = this.tables[posT].getElements()[posR].getNext();
                while (!r.getId().equals(id) && r != null) {
                    r = r.getNext();
                }
                if (r.getId().equals(id)) {
                    for (int i = 0; i < r.getInfo().length; i++) {
                        //System.out.print(r.getInfo()[i] + " ");
                    }
                    //System.out.println("");
                } else {
                    System.out.println("REGISTRO NÃO ENCONTRADO!");
                }
            }
        } else {
            t = this.tables[posT].getNext();
            while (!t.getName().equals(nameTable) && t != null) {
                t = t.getNext();
            }
            if (t.getName().equals(nameTable)) {
                int posR = (int) hashRegister(id, t.getElements().length);
                if (t.getElements()[posR].getId().equals(id)) {
                    for (int i = 0; i < t.getElements()[posR].getInfo().length; i++) {
                        //System.out.print(t.getElements()[posR].getInfo()[i] + " ");
                    }
                    //System.out.println("");
                } else {
                    r = t.getElements()[posR].getNext();
                    while (!r.getId().equals(id) && r != null) {
                        r = r.getNext();
                    }
                    if (r.getId().equals(id)) {
                        for (int i = 0; i < r.getInfo().length; i++) {
                            //System.out.print(r.getInfo()[i] + " ");
                        }
                        //System.out.println("");
                    } else {
                        System.out.println("REGISTRO NÃO ENCONTRADO!");
                    }
                }
            } else {
                System.out.println("TABELA NÃO ENCONTRADA!");
            }
        }
    }
    
    public void searchAllRegister(String file, Boolean print) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
        String[] lines = text.split("\n");
        long tempoInicial = System.currentTimeMillis();
        for (String line : lines) {
            searchRegister(line.split("\t")[0], line.split("\t")[1],print);
        }
        System.out.println("Tempo para busca de todos os registros: " + (System.currentTimeMillis() - tempoInicial));
    }

    public void searchRegister(String nameTable, String id, Boolean print) {
        int posT = (int) hashTable(nameTable);
        NoTable t;
        NoRecord r;
        if (this.tables[posT].getName().equals(nameTable)) {
            int posR = (int) hashRegister(id, this.tables[posT].getElements().length);
            if (this.tables[posT].getElements()[posR].getId().equals(id)) {
                for (int i = 0; i < this.tables[posT].getElements()[posR].getInfo().length; i++) {
                    System.out.print(this.tables[posT].getElements()[posR].getInfo()[i] + " ");
                }
                System.out.println("");
            } else {
                r = this.tables[posT].getElements()[posR].getNext();
                while (!r.getId().equals(id) && r != null) {
                    r = r.getNext();
                }
                if (r.getId().equals(id)) {
                    for (int i = 0; i < r.getInfo().length; i++) {
                        System.out.print(r.getInfo()[i] + " ");
                    }
                    System.out.println("");
                } else {
                    System.out.println("REGISTRO NÃO ENCONTRADO!");
                }
            }
        } else {
            t = this.tables[posT].getNext();
            while (!t.getName().equals(nameTable) && t != null) {
                t = t.getNext();
            }
            if (t.getName().equals(nameTable)) {
                int posR = (int) hashRegister(id, t.getElements().length);
                if (t.getElements()[posR].getId().equals(id)) {
                    for (int i = 0; i < t.getElements()[posR].getInfo().length; i++) {
                        System.out.print(t.getElements()[posR].getInfo()[i] + " ");
                    }
                    System.out.println("");
                } else {
                    r = t.getElements()[posR].getNext();
                    while (!r.getId().equals(id) && r != null) {
                        r = r.getNext();
                    }
                    if (r.getId().equals(id)) {
                        for (int i = 0; i < r.getInfo().length; i++) {
                            System.out.print(r.getInfo()[i] + " ");
                        }
                        System.out.println("");
                    } else {
                        System.out.println("REGISTRO NÃO ENCONTRADO!");
                    }
                }
            } else {
                System.out.println("TABELA NÃO ENCONTRADA!");
            }
        }
    }

    /**
     * @return the insertions
     */
    public int getInsertions() {
        return insertions;
    }

    /**
     * @return the colisoesT
     */
    public int getColisoesT() {
        return colisoesT;
    }

    /**
     * @return the colisoesR
     */
    public int getColisoesR() {
        return colisoesR;
    }

    /**
     * @param colisoesT the colisoesT to set
     */
    public void setColisoesT(int colisoesT) {
        this.colisoesT = colisoesT;
    }

    /**
     * @param colisoesR the colisoesR to set
     */
    public void setColisoesR(int colisoesR) {
        this.colisoesR = colisoesR;
    }
}

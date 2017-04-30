package com.mycompany.trabalhoaed;

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
public class Index {

    private static String path;
    private NoTable[] tables;
    private static int nroTables;
    private int colisoesT = 0, colisoesR = 0;
    private int insertions = 0;

    public Index(String path, int nroTables) {
        this.path = path;
        this.nroTables = nroTables;
        this.tables = new NoTable[this.nroTables];
    }

    void ReadDump() throws IOException {
        int j = 0, TSize;
        //BufferedWriter buffWrite = new BufferedWriter(new FileWriter("search.txt"));
        String nameTable,text, id = "";
        String[] filds, info, line;
        NoTable t = null;
        NoRecord[] records;

        text = new String(Files.readAllBytes(Paths.get(this.path)), StandardCharsets.UTF_8);
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
                insertTable(nameTable, filds, t);

            } else if (line[i].contains("COPY ")) {//estou na lista de elementos de uma tabela
                nameTable = line[i].split(" ")[1];//nome da tabela que se trata os elementos
                t = getTable(nameTable);//retorna o NoTable com aquele nome ps: a função getTable será com hashing
                System.out.println(nameTable);
                i++;
                j = i;
                
                while (!line[j].equals("\\.")) {
                    j++;
                }
                String[] keys = searchPkey(nameTable,line, j);
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
        System.out.println("Tempo para inserção de todos os registros: " + (System.currentTimeMillis() - tempoInicial));
        //System.out.println("Quantidade de registros inseridos: "+insertions);
        //buffWrite.close();
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

    private long hashTable(String str) {
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

    private long hashRegister(String str, long n) {
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

    private long hashRegisterPJW(String str, int n) {
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

    private long hashRegisterELF(String str, int n) {
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

    private long firstHashRegister(String str, int n) {

        long result = 0;
        char[] letras = str.toCharArray();
        for (int j = 0; j < letras.length; j++) {
            result = (long) (5 * result) + ((long) letras[j]);
        }
        return (long) result % n;
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
        int posT = (int) hashTable(t.getName());
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

    private String[] searchPkey(String nameTable, String[] line , int min) throws IOException {
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

    private void searchRegister(String nameTable, String id) {
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
                    //System.out.println("REGISTRO NÃO ENCONTRADO!");
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
                        //System.out.println("REGISTRO NÃO ENCONTRADO!");
                    }
                }
            } else {
                //System.out.println("TABELA NÃO ENCONTRADA!");
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

    private void searchRegister(String nameTable, String id, Boolean print) {
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
}

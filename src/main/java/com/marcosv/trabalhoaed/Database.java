package com.marcosv.trabalhoaed;

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
public class Database {

    private NoTable[] tables;
    private static int nroTables;
    private int insertions = 0, colisoesT = 0, colisoesR = 0;

    public Database(int nroTables) {
        this.nroTables = nroTables;
        this.tables = new NoTable[this.nroTables];
    }

    public void innerJoinB(String nameTable1, String nameTable2, String[] keys) {
        Boolean aux = false, found = false;
        int cont = 0;
        NoRecord auxR1, auxR2;
        NoTable tb1 = getTable(nameTable1);//retorna tabela a apartir do nome
        NoTable tb2 = getTable(nameTable2);
        int[] ind1 = new int[keys.length];
        int[] ind2 = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            ind1[i] = tb1.getIndexFild(keys[i]);//retorna o indice do campo a partir do nome no vetor de informações do registro
            ind2[i] = tb2.getIndexFild(keys[i]);
        }
        for (int i = 0; i < tb1.getElements().length; i++) {
            System.out.println(i);
            if (tb1.getElements()[i] != null) {
                for (int j = 0; j < tb2.getElements().length; j++) {
                    if (tb2.getElements()[j] != null) {//pular posiçoes vazias nos vetores
                        for (int k = 0; k < keys.length; k++) {
                            if (tb1.getElements()[i].getInfo()[ind1[k]].trim().equals(tb2.getElements()[j].getInfo()[ind2[k]].trim())) {
                                aux = true;
                            } else {
                                aux = false;
                                break;
                            }
                        }
                        if (aux) {
//                            tb1.getElements()[i].printInfo();
//                            tb2.getElements()[j].printInfo();
                            cont ++;
                        } else { // procura na lista encadeada do indice
                            auxR2 = tb2.getElements()[j].getNext();
                            while (auxR2 != null) {
                                for (int k = 0; k < keys.length; k++) {
                                    if (tb1.getElements()[i].getInfo()[ind1[k]].trim().equals(auxR2.getInfo()[ind2[k]].trim())) {
                                        aux = true;
                                    } else {
                                        aux = false;
                                        break;
                                    }
                                }
                                if (aux) {
//                                    tb1.getElements()[i].printInfo();
//                                    auxR2.printInfo();
                                    cont ++;
                                    found = true;
                                }
                                auxR2 = auxR2.getNext();
                            }
                        }
                    }
                }
                auxR1 = tb1.getElements()[i].getNext();//varrer lista encadeada da tabela da esquerda
                while (auxR1 != null) {
                    for (int j = 0; j < tb2.getElements().length; j++) {
                        if (tb2.getElements()[j] != null) {
                            for (int k = 0; k < keys.length; k++) {
                                if (auxR1.getInfo()[ind1[k]].trim().equals(tb2.getElements()[j].getInfo()[ind2[k]].trim())) {
                                    aux = true;
                                } else {
                                    aux = false;
                                    break;
                                }
                            }
                            if (aux) {
                                cont ++;
//                                auxR1.printInfo();
//                                tb2.getElements()[j].printInfo();
                            } else {
                                auxR2 = tb2.getElements()[j].getNext();
                                while (auxR2 != null) {
                                    for (int k = 0; k < keys.length; k++) {
                                        if (auxR1.getInfo()[ind1[k]].trim().equals(auxR2.getInfo()[ind2[k]].trim())) {
                                            aux = true;
                                        } else {
                                            aux = false;
                                            break;
                                        }
                                    }
                                    if (aux) {
                                        cont ++;
//                                        auxR1.printInfo();
//                                        auxR2.printInfo();
                                        found = true;
                                    }
                                    auxR2 = auxR2.getNext();
                                }
                            }
                        }
                    }
                    auxR1 = auxR1.getNext();
                }
            }
        }
        System.out.println("Tabela de tamanho igual a: " + cont);
    }

    /*
    Método para construção da matriz auxiliar para consultas JOIN
    */
    private void vecInner(NoTable tb, String[] keys, String[][] tbl, int[] ind) {
        int j = 0;
        for (int i = 0; i < tb.getElements().length; i++) {
            if (tb.getElements()[i] != null) {
                tbl[j][0] = tb.getElements()[i].getId();//identificador sempre na primeira coluna da matriz
                for (int l = 1; l < keys.length + 1; l++) {
                    tbl[j][l] = tb.getElements()[i].getInfo()[ind[l - 1]];//demais informações
                }
                j++;
                NoRecord auxR = tb.getElements()[i].getNext();
                while (auxR != null) {
                    tbl[j][0] = auxR.getId();
                    for (int l = 1; l < keys.length + 1; l++) {
                        tbl[j][l] = auxR.getInfo()[ind[l - 1]];
                    }
                    auxR = auxR.getNext();
                    j++;
                }
            }
        }
        System.out.println("qtd inserida: " + j);
    }

    public void innerJoin(String nameTable1, String nameTable2, String[] keys) {
        Boolean aux = false, found = true;
        long tempoInicial = System.currentTimeMillis();
        String str1 = "", str2 = "";
        MergeSort ms = new MergeSort();
        int cont = 0;
        NoRecord auxR1, auxR2;
        NoTable tb1 = getTable(nameTable1);
        NoTable tb2 = getTable(nameTable2);
        int[] ind1 = new int[keys.length];
        int[] ind2 = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            ind1[i] = tb1.getIndexFild(keys[i]);//retorna o indice do campo a partir do nome no vetor de informações do registro            
            ind2[i] = tb2.getIndexFild(keys[i]);
        }
        String tbl1[][] = new String[tb1.getNroElements()][keys.length + 1];//matriz auxiliar
        String tbl2[][] = new String[tb2.getNroElements()][keys.length + 1];
        vecInner(tb1, keys, tbl1, ind1);
        vecInner(tb2, keys, tbl2, ind2);
        ms.mergeSort(tbl1);//chamada para ordenação da matriz a partir do algoritmo mergeSort
        ms.mergeSort(tbl2);
        int ult = 0;
        for (int i = 0; i < tbl1.length; i++) {
            for (int l = 1; l < tbl1[i].length; l++) {
                str1 += tbl1[i][l]; // concatena os campos na tabela da esquerda no qual a busca está sendo feita
            }
            for (int j = ult; j < tbl2.length; j++) {
                for (int l = 1; l < tbl2[j].length; l++) {
                    str2 += tbl2[j][l];// concatena os campos na tabela da direita no qual a busca está sendo feita
                }
                if(str1.equals(str2)){//se forem iguais deu match
                    if(found)ult = j;
                    found = false;
                    searchRegister(tb1.getName(), tbl1[i][0]);//procura na estrutura indexada o registro
                    searchRegister(tb2.getName(), tbl2[j][0]);
                    cont ++;
                }
                else if(str2.compareToIgnoreCase(str1)>0){//
                    str2 = "";
                    break;
                }
                str2 = "";
            }
            found = true;
            str1 = "";
        }
        System.out.println("quantidade de retornados: "+cont);
        System.out.println("Tempo para InnerJoin: " + (System.currentTimeMillis() - tempoInicial));
    }
    
    private void outerJoin(String nameTable1, String nameTable2, String[] keys) {
        Boolean aux = false, found = true;
        long tempoInicial = System.currentTimeMillis();
        String str1 = "", str2 = "";
        MergeSort ms = new MergeSort();
        int cont = 0;
        NoRecord auxR1, auxR2;
        NoTable tb1 = getTable(nameTable1);
        NoTable tb2 = getTable(nameTable2);
        int[] ind1 = new int[keys.length];
        int[] ind2 = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            ind1[i] = tb1.getIndexFild(keys[i]);//retorna o indice do campo a partir do nome no vetor de informações do registro
            ind2[i] = tb2.getIndexFild(keys[i]);
        }
        String tbl1[][] = new String[tb1.getNroElements()][keys.length + 1];
        String tbl2[][] = new String[tb2.getNroElements()][keys.length + 1];
        vecInner(tb1, keys, tbl1, ind1);
        vecInner(tb2, keys, tbl2, ind2);
        ms.mergeSort(tbl1);//chamada para ordenação da matriz a partir do algoritmo mergeSort
        ms.mergeSort(tbl2);
        int ult = 0;
        for (int i = 0; i < tbl1.length; i++) {
            for (int l = 1; l < tbl1[i].length; l++) {
                str1 += tbl1[i][l];
            }
            
            for (int j = ult; j < tbl2.length; j++) {
                for (int l = 1; l < tbl2[j].length; l++) {
                    str2 += tbl2[j][l];
                }
                if(str1.equals(str2)){//se forem iguais deu match
                    if(found)ult = j;
                    found = false;
                    searchRegister(tb1.getName(), tbl1[i][0]);
                    searchRegister(tb2.getName(), tbl2[j][0]);
                    cont ++;
                }
                else if(str2.compareToIgnoreCase(str1)>0){
                    str2 = "";
                    break;
                }
                str2 = "";
            }
            if(found){//caso não tenha ocorrido match o registro da esquerda é retornando da mesma forma
                searchRegister(tb1.getName(), tbl1[i][0]);
                cont++;
            }
            found = true;
            str1 = "";
        }
        System.out.println("quantidade de retornados: "+cont);
        System.out.println("Tempo para outer join: " + (System.currentTimeMillis() - tempoInicial));
    }
    
    public void lOuterJoin(String nameTable1, String nameTable2, String[] keys) {
        outerJoin(nameTable1, nameTable2, keys);
    }
    
    public void rOuterJoin(String nameTable1, String nameTable2, String[] keys) {
        outerJoin(nameTable2, nameTable1, keys);
    }
    

    public NoTable[] getAllTables() {
        return tables;
    }

    /**
     * @param tables the tables to set
     */
    public void setAllTables(NoTable[] tables) {
        this.tables = tables;
    }

    public void printStructHash() {
        for (NoTable tt : tables) {
            if (tt != null) {
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

    public void deleteRegister(String table, String id) {
        int posT = (int) hashTable(table), i;
        NoTable auxT = this.tables[posT];
        while (!auxT.getName().equals(table) && auxT != null) {
            auxT = auxT.getNext();
        }
        if (auxT == null) {
            System.out.println("Tabela Não Encontrada!");
        } else {
            int posR = (int) hashRegister(id, auxT.getElements().length);
            if (auxT.getElements()[posR].getNext() == null && auxT.getElements()[posR].getId().equals(id)) {//registro na primeira posição e sem lista encadeada
                auxT.getElements()[posR] = null;//libera o espaço do vetor
            } else {
                if (auxT.getElements()[posR].getId().equals(id)) {
                    auxT.getElements()[posR] = auxT.getElements()[posR].getNext();
                } else {
                    NoRecord auxR = auxT.getElements()[posR], auxA;
                    while (auxR != null) {
                        auxA = auxR;
                        auxR = auxR.getNext();
                        if (auxR.getId().equals(id)) {
                            auxA.setNext(auxR.getNext());
                            auxR = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void selectCout(String table) {
        int posT = (int) hashTable(table);
        NoTable auxT = this.tables[posT];
        while (!auxT.getName().equals(table) && auxT != null) {
            auxT = auxT.getNext();
        }
        if (auxT == null) {
            System.out.println("Tabela Não Encontrada!");
        } else {
            System.out.println(auxT.coutRegisters());
        }
    }

    public void selectCoutWhere(String table, String fild, String value) {
        int posT = (int) hashTable(table);
        NoTable auxT = this.tables[posT];
        while (!auxT.getName().equals(table) && auxT != null) {
            auxT = auxT.getNext();
        }
        if (auxT == null) {
            System.out.println("Tabela Não Encontrada!");
        } else {
            System.out.println(auxT.coutRegisters(fild, value));
        }
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
                    }
                } else {
                    r = t.getElements()[posR].getNext();
                    while (!r.getId().equals(id) && r != null) {
                        r = r.getNext();
                    }
                    if (r.getId().equals(id)) {
                        for (int i = 0; i < r.getInfo().length; i++) {
                        }
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
            searchRegister(line.split("\t")[0], line.split("\t")[1], print);
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
                while (!r.getId().equals(id) || r == null) {
                    r = r.getNext();
                    if (r == null) {
                        break;
                    }
                }
                if (r != null) {
                    if (r.getId().equals(id)) {
                        for (int i = 0; i < r.getInfo().length; i++) {
                            System.out.print(r.getInfo()[i] + " ");
                        }
                        System.out.println("");
                    }
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
                    while (!r.getId().equals(id) || r == null) {
                        r = r.getNext();
                        if (r == null) {
                            break;
                        }
                    }
                    if (r != null) {
                        if (r.getId().equals(id)) {
                            for (int i = 0; i < r.getInfo().length; i++) {
                                System.out.print(r.getInfo()[i] + " ");
                            }
                            System.out.println("");
                        }
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

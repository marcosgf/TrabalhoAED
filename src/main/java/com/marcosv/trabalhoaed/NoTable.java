package com.marcosv.trabalhoaed;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marcos
 */
public class NoTable {
    private String name;
    private int nroElements;
    private String[] filds;
    private String[] fildsPKey;
    private NoTable next ;
    private NoTable last;
    private NoRecord[] elements;
    
    
    public NoTable(String name){
        this.name = name;
        this.next = null;
        this.last =null;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the filds
     */
    public String[] getFilds() {
        return filds;
    }

    /**
     * @param filds the filds to set
     */
    public void setFilds(String[] filds) {
        this.filds=filds;
    }

    /**
     * @return the nroElements
     */
    public int getNroElements() {
        return nroElements;
    }

    /**
     * @param nroElements the nroElements to set
     */
    public void setNroElements(int nroElements) {
        this.nroElements = nroElements;
    }

    /**
     * @return the elements
     */
    public NoRecord[] getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(NoRecord[] elements) {
        this.elements = elements;
    }

    /**
     * @return the next
     */
    public NoTable getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(NoTable next) {
        this.next = next;
    }
    
    public int[] getPosPkey(String[] pkey){
        int[] pos = new int[pkey.length];
        this.fildsPKey = new String[pkey.length];
        int j =0;
        for(String key: pkey){
            for(int i = 0 ; i < this.filds.length ; i ++){
                if(key.equals(this.filds[i])){
                    this.fildsPKey[j] = this.filds[i];
                    pos[j] = i;
                    j++;
                }
            }
        }
        return pos;
    }
    
    public void printStruct(){
        System.out.println(this.name + " tamanho = "+this.elements.length);
        for(int i = 0 ; i < this.elements.length ; i++){
            if(this.elements[i]!=null){
                System.out.print(".");
            }
            else{
                System.out.print(" ");
            }
            if((i+1) % 200 == 0){
                System.out.println("");
            }
        }
    }
    
    public int getIndexFild(String fild){
        for(int i = 0 ; i < filds.length ; i++){
            if(filds[i].equals(fild))
                return i;
        }
        return 0;
    }
    
    public int coutRegisters(){
        int cont = 0 ;
        NoRecord aux;
        for(int i = 0 ; i < this.elements.length ; i++){
            if(this.elements[i]!=null){
                cont++;
                aux = this.elements[i].getNext();
                while(aux != null){
                    cont++;
                    aux = aux.getNext();
                }
            }
        }
        return cont;
    }
    
    public int coutRegisters(String fild, String value){
        int cont = 0, ind = getIndexFild(fild);
        NoRecord aux;
        for(int i = 0 ; i < this.elements.length ; i++){
            if(this.elements[i]!=null){
                if(this.elements[i].getInfo()[ind].trim().equals(value)){
                    cont++;
                }
                aux = this.elements[i].getNext();
                while(aux != null){
                    if(aux.getInfo()[ind].trim().equals(value)){
                        cont++;
                    }
                    aux = aux.getNext();
                }
            }
        }
        return cont;
    }
    
    /**
     * @return the fildsPKey
     */
    public String[] getFildsPKey() {
        return fildsPKey;
    }
}

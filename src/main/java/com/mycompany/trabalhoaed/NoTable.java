package com.mycompany.trabalhoaed;

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
    private NoTable next ;
    private NoRecord[] elements;
    
    
    public NoTable(String name){
        this.name = name;
        this.next = null;
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
        int j =0;
        for(String key: pkey){
            for(int i = 0 ; i < this.filds.length ; i ++){
                if(key.equals(this.filds[i])){
                    pos[j] = i;
                }
            }
        }
        return pos;
    }
}

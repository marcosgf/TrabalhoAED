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
    private NoRecord[] elements;
    
    
    public NoTable(String name){
        this.name = name;
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
}

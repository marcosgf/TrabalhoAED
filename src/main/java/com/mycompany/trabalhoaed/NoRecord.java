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
public class NoRecord {
    private String id;
    private String[] info;
    private NoRecord next;
    private NoRecord last;
    
    public NoRecord(String id){
        this.id = id;
        this.next = null;
        this.last = null;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the info
     */
    public String[] getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String[] info) {
        this.info = info;
    }

    /**
     * @return the next
     */
    public NoRecord getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(NoRecord next) {
        this.next = next;
    }

    /**
     * @return the last
     */
    public NoRecord getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(NoRecord last) {
        this.last = last;
    }
    
        
    
}

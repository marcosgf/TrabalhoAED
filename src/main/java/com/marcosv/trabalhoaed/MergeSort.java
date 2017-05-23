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
public class MergeSort {

    public void mergeSort(String[][] names) {
        if (names.length >= 2) {
            String[][] left = new String[names.length / 2][names[0].length];
            String[][] right = new String[names.length - names.length / 2][names[0].length];
            for (int i = 0; i < left.length; i++) {
                left[i] = names[i];
            }
            for (int i = 0; i < right.length; i++) {
                right[i] = names[i + names.length / 2];
            }

            mergeSort(left);
            mergeSort(right);
            merge(names, left, right);
        }
    }

    public void merge(String[][] names, String[][] left, String[][] right) {
        int a = 0;
        int b = 0;
        String lef = "", rig = "";
        for (int i = 0; i < names.length; i++) {
            if (a < left.length && b < right.length) {
                for (int j = 1; j < names[0].length; j++) {
                    lef += left[a][j];
                    rig += right[b][j];
                }
            }
            if (b >= right.length || (a < left.length && lef.compareToIgnoreCase(rig) < 0)) {
                names[i] = left[a];
                a++;
            } else {
                names[i] = right[b];
                b++;
            }
            lef = "";
            rig = "";
        }
    }
}

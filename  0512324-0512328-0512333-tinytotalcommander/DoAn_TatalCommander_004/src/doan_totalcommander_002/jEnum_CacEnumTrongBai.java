/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package doan_totalcommander_002;

/**
 *
 * @author Administrator
 */
public enum jEnum_CacEnumTrongBai {
    BangTrai (1),
    BangPhai (2),


    KB (1024),
    MB (1024 * 1024),
    GB (1024 & 1024 * 1024),

    
    SuaFile (2),
    XemFile (3);

    private final int value;

    jEnum_CacEnumTrongBai(int value) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}

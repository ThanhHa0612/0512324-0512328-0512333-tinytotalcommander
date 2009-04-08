/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package doan_tatalcommande_002;

/**
 *
 * @author Administrator
 */
public enum jEnum_CacBang {
    BangTrai (1),
    BangPhai (2);

    private final int value;

    jEnum_CacBang(int value) {
        this.value = value;
    }

    int value(){
        return this.value;
    }
}

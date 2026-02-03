/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String args[]){
        CountThread t = new CountThread(0,99);
        CountThread s = new CountThread(99,199);
        CountThread r = new CountThread(199,299);
        r.start();
        s.start();
        t.start();

    }
    
}

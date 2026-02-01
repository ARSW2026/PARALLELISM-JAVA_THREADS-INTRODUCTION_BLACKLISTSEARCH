/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.spamkeywordsdatasource;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlacklistsDataSourceFacade {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(HostBlacklistsDataSourceFacade.class.getName());

    private static final int REGISTERED_SERVERS = 10000;
    private static final HostBlacklistsDataSourceFacade instance = new HostBlacklistsDataSourceFacade();

    private HostBlacklistsDataSourceFacade(){
        // simulated datasource
    }

    public static HostBlacklistsDataSourceFacade getInstance(){
        return instance;
    }

    public int getRegisteredServersCount(){
        return REGISTERED_SERVERS;
    }

    public boolean isInBlackListServer(int blservernum, String host){
        // Simulated behaviour for the exercise examples
        if (host==null) return false;
        switch(host){
            case "200.24.34.55":
                return blservernum<6; // found in first 6 servers -> early stop
            case "202.24.34.55":
                return (blservernum==100 || blservernum==1500 || blservernum==3500 || blservernum==7000 || blservernum==9000);
            case "212.24.24.55":
                return false;
            default:
                // pseudo-random deterministic distribution for other hosts
                int h = Math.abs(host.hashCode() + blservernum);
                return (h % 10007) == 0;
        }
    }

    public void reportAsTrustworthy(String host){
        LOG.log(java.util.logging.Level.INFO, "HOST {0} Reported as trustworthy", host);
    }

    public void reportAsNotTrustworthy(String host) {
        LOG.log(java.util.logging.Level.INFO, "HOST {0} Reported as NOT trustworthy", host);
    }

//NO TOCAR ESTE CODIGO!!    
    
/* TODO: fix this stuff */

}
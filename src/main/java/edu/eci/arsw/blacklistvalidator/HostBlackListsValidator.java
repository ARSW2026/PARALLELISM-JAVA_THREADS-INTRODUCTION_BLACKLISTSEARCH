/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress){
        // by default use available processors
        int np = Runtime.getRuntime().availableProcessors();
        return checkHost(ipaddress, np);
    }

    public List<Integer> checkHost(String ipaddress, int N){
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();

        java.util.concurrent.atomic.AtomicInteger globalOcurrences = new java.util.concurrent.atomic.AtomicInteger(0);

        // normalize N
        if (N<=0) N=1;
        if (N>totalServers) N=totalServers;

        java.util.List<BlackListThread> threads = new java.util.ArrayList<>();

        int base = totalServers / N;
        int extra = totalServers % N;
        int start = 0;
        for (int i=0;i<N;i++){
            int length = base + (i<extra?1:0);
            int end = start + length; // exclusive
            BlackListThread t = new BlackListThread(start, end, ipaddress, skds, globalOcurrences);
            threads.add(t);
            start = end;
        }

        for (BlackListThread t: threads) t.start();

        int checkedListsCount=0;

        for (BlackListThread t: threads){
            try{
                t.join();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            blackListOcurrences.addAll(t.getFoundLists());
            checkedListsCount += t.getCheckedCount();
        }

        int ocurrencesCount = globalOcurrences.get();

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, totalServers});

        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}

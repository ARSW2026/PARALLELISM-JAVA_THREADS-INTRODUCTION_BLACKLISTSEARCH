package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackListThread extends Thread{

    private final int startInclusive;
    private final int endExclusive;
    private final String ipAddress;
    private final HostBlacklistsDataSourceFacade skds;
    private final AtomicInteger globalOcurrences;
    private final List<Integer> foundLists;
    private int checked;

    public BlackListThread(int startInclusive, int endExclusive, String ipAddress, HostBlacklistsDataSourceFacade skds, AtomicInteger globalOcurrences){
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.ipAddress = ipAddress;
        this.skds = skds;
        this.globalOcurrences = globalOcurrences;
        this.foundLists = new LinkedList<>();
        this.checked = 0;
    }

    @Override
    public void run(){
        for (int i = startInclusive; i < endExclusive; i++){
            if (globalOcurrences.get() >= HostBlackListsValidator.BLACK_LIST_ALARM_COUNT) break;

            checked++;
            if (skds.isInBlackListServer(i, ipAddress)){
                foundLists.add(i);
                globalOcurrences.incrementAndGet();
            }
        }
    }

    public int getOcurrencesCount(){
        return foundLists.size();
    }

    public List<Integer> getFoundLists(){
        return foundLists;
    }

    public int getCheckedCount(){
        return checked;
    }

}

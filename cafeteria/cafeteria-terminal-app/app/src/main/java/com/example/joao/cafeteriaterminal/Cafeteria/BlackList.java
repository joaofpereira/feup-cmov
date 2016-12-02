package com.example.joao.cafeteriaterminal.Cafeteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlackList implements Serializable {
    private static BlackList instance = null;

    private List<BlackListUser> blacklist;

    protected BlackList() {
        this.blacklist = new ArrayList<>();
    }

    public List<BlackListUser> getBlacklist() {
        return this.blacklist;
    }

    public void setBlackList(List<BlackListUser> blacklist) {
        this.blacklist = blacklist;
    }

    public BlackListUser getByID(UUID userID) {
        for (int i = 0; i < blacklist.size(); i++)
            if(blacklist.get(i).getUserID().equals(userID))
                return blacklist.get(i);

        return null;
    }

    public boolean userExistsInBlacklist(UUID userID) {
        for (int i = 0; i < blacklist.size(); i++)
            if(blacklist.get(i).getUserID().equals(userID))
                return true;

        return false;
    }

    public void add(BlackListUser blu) {
        this.blacklist.add(blu);
    }

    public static BlackList getInstance() {
        if(instance == null) {
            instance = new BlackList();
        }
        return instance;
    }

    public String toString() {
        String result = new String();

        for (BlackListUser blu : blacklist)
            result += blu.toString();

        return result;
    }
}

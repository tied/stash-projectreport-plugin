/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valens;

import com.atlassian.stash.commit.Commit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author IHutuleac
 */
public class CommitArchiveService
{
        
    List<Commit> list = Collections.synchronizedList(new ArrayList<Commit>());

    public List<Commit> getList()
    {
        return list;
    }

    public void setList(List<Commit> list)
    {
        this.list = list;
    }
            
    private CommitArchiveService()
    {
    }
    
    public static CommitArchiveService getInstance()
    {
        return CommitArchiveServiceHolder.INSTANCE;
    }
    
    private static class CommitArchiveServiceHolder
    {

        private static final CommitArchiveService INSTANCE = new CommitArchiveService();
    }
}

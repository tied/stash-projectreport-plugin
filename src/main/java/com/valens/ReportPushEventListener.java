/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valens;

import com.atlassian.event.api.EventListener;
import com.atlassian.stash.commit.Commit;
import com.atlassian.stash.commit.CommitService;
import com.atlassian.stash.commit.CommitsBetweenRequest;
import com.atlassian.stash.event.RepositoryPushEvent;
import com.atlassian.stash.nav.NavBuilder;
import com.atlassian.stash.repository.RefChange;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageRequest;
import com.atlassian.stash.util.PageRequestImpl;
/**
 *
 * @author IHutuleac
 */
public class ReportPushEventListener
{
    
    private final CommitService commitService;
    private final NavBuilder navBuilder;
    
    public ReportPushEventListener(CommitService commitService, NavBuilder navBuilder) {
        this.commitService = commitService;
        this.navBuilder = navBuilder;
    }
    
    @EventListener
    public void mylistener(RepositoryPushEvent pushEvent)
    {
        for(RefChange refChange :pushEvent.getRefChanges())
        {
            CommitsBetweenRequest.Builder commitsBetweenBuilder = new CommitsBetweenRequest.Builder(pushEvent.getRepository() );
            commitsBetweenBuilder.exclude(refChange.getFromHash()); //Starting with
            commitsBetweenBuilder.include(refChange.getToHash()); // ending with

            PageRequest pageRequest = new PageRequestImpl(0,6);

            Page<Commit> commits = commitService.getCommitsBetween(commitsBetweenBuilder.build(), pageRequest);
            String key = pushEvent.getRepository().getProject().getKey();
            //TODO handle Pages
            for (Commit commit : commits.getValues()) {    
                if(CommitArchiveService.getInstance().getList().size() > 1000)
                    CommitArchiveService.getInstance().getList().remove(0);
                CommitArchiveService.getInstance().getList().add(commit);
               
            }
        }
    }
}

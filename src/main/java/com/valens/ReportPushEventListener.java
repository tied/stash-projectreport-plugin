/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valens;

import com.atlassian.event.api.EventListener;
import com.atlassian.bitbucket.commit.Commit;
import com.atlassian.bitbucket.commit.CommitService;
import com.atlassian.bitbucket.commit.CommitsBetweenRequest;
import com.atlassian.bitbucket.event.repository.RepositoryPushEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequest;
import com.atlassian.bitbucket.util.PageRequestImpl;
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

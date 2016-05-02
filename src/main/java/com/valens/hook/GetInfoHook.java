package com.valens.hook;

import com.atlassian.bitbucket.commit.Commit;
import com.atlassian.bitbucket.commit.CommitService;
import com.atlassian.bitbucket.commit.CommitsBetweenRequest;
import com.atlassian.bitbucket.hook.repository.*;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.repository.*;
import com.atlassian.bitbucket.setting.*;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequest;
import com.atlassian.bitbucket.util.PageRequestImpl;
import com.valens.CommitArchiveService;
import java.net.URL;
import java.util.Collection;

public class GetInfoHook implements AsyncPostReceiveRepositoryHook, RepositorySettingsValidator
{
    
    private final CommitService commitService;
    private final NavBuilder navBuilder;
    
    /**
     * Connects to a configured URL to notify of all changes.
     */
    public GetInfoHook(CommitService commitService, NavBuilder navBuilder) {
        this.commitService = commitService;
        this.navBuilder = navBuilder;
    }
    
    @Override
    public void postReceive(RepositoryHookContext context, Collection<RefChange> refChanges)
    {
        for(RefChange refChange :refChanges)
        {
            CommitsBetweenRequest.Builder commitsBetweenBuilder = new CommitsBetweenRequest.Builder(context.getRepository() );
            commitsBetweenBuilder.exclude(refChange.getFromHash()); //Starting with
            commitsBetweenBuilder.include(refChange.getToHash()); // ending with

            PageRequest pageRequest = new PageRequestImpl(0,6);

            Page<Commit> commits = commitService.getCommitsBetween(commitsBetweenBuilder.build(), pageRequest);
            String key = context.getRepository().getProject().getKey();
            //TODO handle Pages
            for (Commit commit : commits.getValues()) {    
                if(CommitArchiveService.getInstance().getList().size() > 1000)
                    CommitArchiveService.getInstance().getList().remove(0);
                CommitArchiveService.getInstance().getList().add(commit);
               
            }
        }
    }

    @Override
    public void validate(Settings settings, SettingsValidationErrors errors, Repository repository)
    {
        if (settings.getString("url", "").isEmpty())
        {
            errors.addFieldError("url", "Url field is blank, please supply one");
        }
    }
}
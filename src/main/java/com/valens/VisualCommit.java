/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valens;

import com.atlassian.bitbucket.commit.Commit;
import java.util.Date;

/**
 *
 * @author IHutuleac
 */
public class VisualCommit
{
    private String author;
    private String date;
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    private String commitHash;
    private String shortHash;
    private String message;
    private String repository;
    private String project;
    
    public VisualCommit(Commit m)
    {
        this.author = m.getAuthor().getName();
        this.date = m.getAuthorTimestamp().toGMTString();
        this.commitHash = m.getId();
        this.shortHash = m.getDisplayId();
        this.message = m.getMessage();
        this.repository = m.getRepository().getName();
        this.project = m.getRepository().getProject().getKey();
        this.timestamp = m.getAuthorTimestamp();
    }

    public String getRepository()
    {
        return repository;
    }

    public void setRepository(String repository)
    {
        this.repository = repository;
    }

    public String getProject()
    {
        return project;
    }

    public void setProject(String project)
    {
        this.project = project;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getCommitHash()
    {
        return commitHash;
    }

    public void setCommitHash(String commitHash)
    {
        this.commitHash = commitHash;
    }

    public String getShortHash()
    {
        return shortHash;
    }

    public void setShortHash(String shortHash)
    {
        this.shortHash = shortHash;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
    
    
}

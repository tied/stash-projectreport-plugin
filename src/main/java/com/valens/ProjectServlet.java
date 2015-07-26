package com.valens;

import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.commit.Commit;
import com.atlassian.stash.commit.CommitService;
import com.atlassian.stash.nav.NavBuilder;
import com.atlassian.stash.project.Project;
import com.atlassian.stash.project.ProjectService;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryService;
import com.atlassian.stash.user.Permission;
import com.atlassian.stash.user.SecurityService;
import com.atlassian.stash.util.Operation;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageProvider;
import com.atlassian.stash.util.PageRequest;
import com.atlassian.stash.util.PageRequestImpl;
import com.atlassian.stash.util.PageUtils;
import com.atlassian.stash.util.PagedIterable;
import com.google.common.collect.ImmutableMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProjectServlet extends AbstractServlet
{

    private final ProjectService projectService;
    private final RepositoryService repositoryService;
    private final SecurityService securityService;
    private final CommitService commitService;
    
    
    public ProjectServlet(SoyTemplateRenderer soyTemplateRenderer, ProjectService projectService,
            RepositoryService repositoryService,
            SecurityService securityService,
            CommitService commitService)
    {
        super(soyTemplateRenderer);
        this.projectService = projectService;
        this.repositoryService = repositoryService;
        this.securityService = securityService;
        this.commitService = commitService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // Get projectKey from path
        String pathInfo = req.getPathInfo();

        String[] components = pathInfo.split("/");

        if (components.length < 2)
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Project project = projectService.getByKey(components[1]);

        if (project == null)
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        boolean isSettings = false;
        if (components.length == 3 && "settings".equalsIgnoreCase(components[2]))
        {
            isSettings = true;
        }

        ArrayList<Commit> commits = new ArrayList<Commit>();
        for (Commit aux : CommitArchiveService.getInstance().getList())
        {
            if (aux.getRepository().getProject().getKey().equalsIgnoreCase(project.getKey()))
            {
                commits.add(aux);
                

            }
        }

        Collections.sort(commits, new Comparator<Commit>()
        {
            @Override
            public int compare(Commit fruite1, Commit fruite2)
            {

                return fruite2.getAuthorTimestamp().compareTo(fruite1.getAuthorTimestamp());
            }
        });

        String template = "plugin.project";

        render(resp, template, ImmutableMap.<String, Object>of("project", project,
                "commits", commits));
    }

}

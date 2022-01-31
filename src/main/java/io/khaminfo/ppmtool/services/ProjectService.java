package io.khaminfo.ppmtool.services;

import io.khaminfo.ppmtool.domain.Backlog;
import io.khaminfo.ppmtool.domain.Project;
import io.khaminfo.ppmtool.domain.User;
import io.khaminfo.ppmtool.exceptions.ProjectIdException;
import io.khaminfo.ppmtool.repositories.BacklogRepository;
import io.khaminfo.ppmtool.repositories.ProjectRepository;
import io.khaminfo.ppmtool.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project  ,String userName){
        try{
        	
        	

            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }else{	
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
                if(project.getBacklog() ==null || !project.getProjectLeader().equals(userName)) {
                	throw new ProjectIdException("Error while Updating '"+project.getProjectIdentifier().toUpperCase() + "' Please check Attributes ");
                }
            }
            User user = userRepository.findByUsername(userName);
            project.setUser(user);
            project.setProjectLeader(userName);
        	project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            return projectRepository.save(project);

        }catch (Exception e){
        	if(e.getClass().equals(ProjectIdException.class))
        		throw e;
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }

    }


    public Project findProjectByIdentifier(String projectId, String userName){

        Project project = projectRepository.findByProjectIdentifierAndProjectLeader(projectId.toUpperCase() , userName);

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' does not exist");

        }


        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }


    public void deleteProjectByIdentifier(String projectid,String userName){
    	
        try {
        	projectRepository.deleteProjectByProjectIdentifier(projectid , userName);
        	
        }catch(Exception e){
            throw  new  ProjectIdException("Cannot Project with ID '"+projectid+"'. This project does not exist");
        }
    }

}

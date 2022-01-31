package io.khaminfo.ppmtool.web;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.khaminfo.ppmtool.domain.ProjectTask;
import io.khaminfo.ppmtool.exceptions.ProjectNotFoundException;
import io.khaminfo.ppmtool.services.MapValidationErrorService;
import io.khaminfo.ppmtool.services.ProjectTaskService;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,Principal principal,
                                            BindingResult result, @PathVariable String backlog_id){

        ResponseEntity<?> erroMap = mapValidationErrorService.MapValidationService(result);
        if (erroMap != null) return erroMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask , principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

    }
    
    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectTaskList(@PathVariable String backlog_id , Principal principal){
    	try{
    	    return projectTaskService.findBacklogById(backlog_id , principal.getName());
    	}catch(Exception e) {
    		throw new ProjectNotFoundException("Project with id : (" + backlog_id + ") does not exist.");
    	}
    }
    @GetMapping("/{backlog_id}/{projectSequence}")
    public ResponseEntity<ProjectTask> getProjectTaskList(@PathVariable String backlog_id ,@PathVariable String projectSequence , Principal principal){
    	return new ResponseEntity<ProjectTask>(projectTaskService.findPTByProjectSequence(backlog_id,projectSequence , principal.getName()),HttpStatus.OK);
    }
    
    @PatchMapping("/updateTask")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,Principal principal,
                                            BindingResult result){

        ResponseEntity<?> erroMap = mapValidationErrorService.MapValidationService(result);
        if (erroMap != null) return erroMap;

        ProjectTask projectTask1 = projectTaskService.updateProjectTask(projectTask , principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.OK);

    }
    
    @DeleteMapping("/{sequence}")
    public ResponseEntity<?> deleteProject(@PathVariable String sequence , Principal principal){
        projectTaskService.deleteProjectBySequence(sequence,principal.getName());

        return new ResponseEntity<String>("Project task: '"+sequence+"' was deleted", HttpStatus.OK);
    }
    


}

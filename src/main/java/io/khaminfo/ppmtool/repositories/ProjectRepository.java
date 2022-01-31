package io.khaminfo.ppmtool.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.khaminfo.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectId);
    Project findByProjectIdentifierAndProjectLeader(String projectId , String projectLeader);
    @Modifying
    @Transactional
    @Query("delete from Project u where u.projectIdentifier = ?1 and u.projectLeader = ?2")
    void deleteProjectByProjectIdentifier(String id , String userName);

    @Override
    Iterable<Project> findAll();
	Iterable<Project> findAllByProjectLeader(String username);
}

package io.khaminfo.ppmtool.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.khaminfo.ppmtool.domain.ProjectTask;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
ProjectTask findByProjectIdentifierAndProjectSequence(String projectIdentifier,String projectSequence);
@Modifying
@Transactional
@Query("delete from ProjectTask u where u.projectSequence = ?1 ")
void deleteProjectByProjectIdentifier(String sequence );
}

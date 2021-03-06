package slash.process.meapp.repository;

import slash.process.meapp.domain.Project;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import slash.process.meapp.domain.Task;

import java.util.List;

/**
 * Spring Data  repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select project from Project project where project.user.login = ?#{principal.username}")
    List<Project> findByUserIsCurrentUser();

}

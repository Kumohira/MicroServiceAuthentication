package org.sid.Dao;

import org.sid.Entities.AppRole;
import org.sid.Entities.ModuleFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ModuleFeatureRepository extends JpaRepository<ModuleFeature, Long> {
    public ModuleFeature findByModuleAndFeature(String moduleName, String featureName);

    public List<ModuleFeature> findAllByRole(AppRole appRole);

    @Query(value = "SELECT * FROM module_feature mf WHERE mf.role_id = ?1", nativeQuery = true)
    public List<ModuleFeature> chercher(Long roleId);

    @Query(value = "SELECT DISTINCT module FROM module_feature mf WHERE mf.role_id = ?1", nativeQuery = true)
    public List<String> findAllModuleByRole(Long roleId);

    @Query(value = "SELECT feature FROM module_feature mf WHERE mf.module = ?1 AND mf.role_id = ?2", nativeQuery = true)
    public List<String> findAllFeatureByModuleAndRole(String module, Long roleId);
}

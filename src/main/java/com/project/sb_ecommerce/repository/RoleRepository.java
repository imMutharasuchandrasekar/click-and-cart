package com.project.sb_ecommerce.repository;

import com.project.sb_ecommerce.model.Enums.AppRole;
import com.project.sb_ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByRoleName( AppRole appRole );
}

package com.project.sb_ecommerce.model;

import com.project.sb_ecommerce.model.Enums.AppRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private AppRole roleName;

    public Role(AppRole RoleName)
    {
        this.roleName = RoleName;
    }
}

package org.varayasolusi.saktiauth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppRoleEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "app_role_name", length = 30, nullable = false)
    private String appRoleName;

    @Column(name = "app_role_description", length = 100)
    private String appRoleDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_role_group_id", nullable = false)
    private AppRoleGroupEntity appRoleGroup;

    @Column(name = "is_active")
    private Short isActive;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
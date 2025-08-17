package org.varayasolusi.saktiauth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "app_role_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppRoleGroupEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", length = 4, nullable = false)
    private String code;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Column(name = "is_active")
    private Short isActive;
}
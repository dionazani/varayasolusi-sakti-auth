package org.varayasolusi.saktiauth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "application_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationTypeEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", length = 4, nullable = false)
    private String code;

    @Column(name = "application_type_name", length = 30, nullable = false)
    private String applicationTypeName;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "is_active")
    private Short isActive;
}

package org.varayasolusi.saktiauth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name="app_user")
public class AppUserEntity {

	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "app_user_type")
    private Integer appUserType;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "mobile_phone", length = 50, nullable = false, unique = true)
    private String mobilePhone;

    @Column(name = "app_user_name", length = 100)
    private String appUserName;
	
    @CreationTimestamp
    @Column(name="created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updateAt;
}

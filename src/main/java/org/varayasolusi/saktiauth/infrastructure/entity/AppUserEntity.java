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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    private int appUserType;

    @CreationTimestamp
    @Column(name="created_at")
    private Timestamp createdAt;
    
    private Timestamp updateAt;
    
	private String email;

	private String mobilePhone;
	
	private String appUserName;
}

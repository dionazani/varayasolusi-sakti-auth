package org.varayasolusi.saktiauth.infrastructure.entity;

import java.util.Date;
import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name="app_user_person")
public class AppUserPersonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
	private UUID id;

	private UUID appUserId;

	private UUID appRoleId;
	
	private String password;
	
	private short mustChangePassword;

	private Date changePasswordNextDate;

	private short isLock;
	
	@CreationTimestamp
	@Column(name="created_at")
	private Timestamp createdAt;
	
	private Timestamp updatedAt;

}

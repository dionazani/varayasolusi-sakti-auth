package org.varayasolusi.saktiauth.infrastructure.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="app_user_verified")
public class AppUserVerifiedEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;
	
	private UUID appUserId;
	
	private String verifiedFor;
	
	private Timestamp verifiedDate;
	
	@CreationTimestamp
	@Column(name="created_at")
	private Timestamp createdAt;
}

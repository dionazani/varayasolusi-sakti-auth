package org.varayasolusi.saktiauth.infrastructure.entity;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name="sign_up")
public class SignUpEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
	private UUID id;
	
	private String reqDesc;
	
	@CreationTimestamp
	@Column(name="created_at")
	private Timestamp createdAt;
 
}

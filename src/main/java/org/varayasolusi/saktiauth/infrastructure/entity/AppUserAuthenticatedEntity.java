package org.varayasolusi.saktiauth.infrastructure.entity;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="app_user_authenticated")
public class AppUserAuthenticatedEntity {
	
    @Id
    private UUID id;
    
    private UUID appUserId;
    
    private UUID applicationTypeId;
    
    private String tokenValue;
    
}

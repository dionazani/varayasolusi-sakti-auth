package org.varayasolusi.saktiauth.infrastructure.entity;

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
@Table(name="app_user_signup")
public class AppUserSignUpEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;
    
    private UUID appUserId;
    
    private UUID signUpId;
}

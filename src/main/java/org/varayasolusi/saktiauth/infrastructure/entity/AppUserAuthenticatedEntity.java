package org.varayasolusi.saktiauth.infrastructure.entity;

import java.sql.Timestamp;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="app_user_authenticated")
public class AppUserAuthenticatedEntity {
	
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUserEntity appUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_type_id", nullable = false)
    private ApplicationTypeEntity applicationType;
    
    @Column(name = "access_token", length = 300, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 300, nullable = false)
    private String refreshToken;

    @Column(name = "behaviour", length = 1, nullable = false)
    private String behaviour;

    @Column(name = "logout",  nullable = true)
    private Timestamp logout;
    
    @CreationTimestamp
    private Timestamp createdAt;
}

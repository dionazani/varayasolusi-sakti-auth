package org.varayasolusi.saktiauth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "app_user_signup", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"app_user_id", "sign_up_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserSignupEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUserEntity appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sign_up_id", nullable = false)
    private SignUpEntity signUp;
}

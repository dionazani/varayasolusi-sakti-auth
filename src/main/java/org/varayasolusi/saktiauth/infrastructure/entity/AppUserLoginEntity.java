package org.varayasolusi.saktiauth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "app_user_person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserLoginEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_user_id", nullable = false)
	private AppUserEntity appUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_role_id", nullable = false)
	private AppRoleEntity appRole;

	@Column(name = "password", length = 255, nullable = false)
	private String password;

	@Column(name = "must_change_password")
	private Short mustChangePassword;

	@Column(name = "change_password_next_date")
	private Date changePasswordNextDate;

	@Column(name = "is_lock")
	private Short isLock;

	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "user_person_status", length = 10)
	private String userPersonStatus;
}

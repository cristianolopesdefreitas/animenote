package br.com.animenote.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import br.com.animenote.constants.Status;

@Entity(name = "tb_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	public User() {
		this.status = Status.I;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Size(min = 3, max = 50)
	@Valid
	@Column(nullable = false, length = 50)
	private String name;

	@NotBlank
	@Email
	@Size(max = 50)
	@Valid
	@Column(nullable = false, length = 50)
	private String email;

	@NotBlank
	@Size(min = 6, max = 50)
	@Valid
	@Column(nullable = false, length = 32)
	private String username;

	@NotBlank
	@Size(min = 8)
	@Valid
	@Column(nullable = false, length = 60)
	private String password;

	// @NotBlank
	@Transient
	private String passwordConfirmation;

	// @Column(nullable = true, length = 50)
	// private String avatar;

	@Column(nullable = true, columnDefinition = "longblob")
	private byte[] avatar;

	@Column(nullable = true, length = 10)
	private String avatarType;

	@Column(nullable = true)
	private Date birthDate;

	@Column(nullable = true)
	private String about;

	@ManyToMany(targetEntity = Role.class)
	@JoinTable(name = "tb_user_role", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", nullable = false) })
	private Set<Role> roles;

	@Column(nullable = false, columnDefinition = "enum('A', 'I') default 'I'")
	@Enumerated(EnumType.STRING)
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	// public String getAvatar() {
	// return avatar;
	// }
	//
	// public void setAvatar(String avatar) {
	// this.avatar = avatar;
	// }

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public String getAvatarType() {
		return avatarType;
	}

	public void setAvatarType(String avatarType) {
		this.avatarType = avatarType;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}

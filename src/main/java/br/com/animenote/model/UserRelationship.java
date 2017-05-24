package br.com.animenote.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.animenote.constants.Status;

@Table(name = "tb_user_relationship")
@Entity
public class UserRelationship implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public UserRelationship() {
		this.status = Status.A;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_follower_id", nullable = false)
    private User follower;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_followed_id", nullable = false)
    private User followed;
	
	@Column(nullable = false, columnDefinition = "enum('A', 'I') default 'I'")
	@Enumerated(EnumType.STRING)
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}

	public User getFollowed() {
		return followed;
	}

	public void setFollowed(User followed) {
		this.followed = followed;
	}
	
}

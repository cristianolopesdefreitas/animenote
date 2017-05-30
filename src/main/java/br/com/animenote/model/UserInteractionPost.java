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

import br.com.animenote.constants.Status;
import br.com.animenote.constants.UserPostInteractions;

@Entity(name = "tb_user_interaction_post")
public class UserInteractionPost implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public UserInteractionPost() {
		this.status = Status.A;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_post_id", nullable = false)
	private UserPost post;
	
	@Column(nullable = false, columnDefinition = "enum('CURTIR', 'DESCURTIR')")
	@Enumerated(EnumType.STRING)
	private UserPostInteractions userPostInteractions;

	@Column(nullable = false, columnDefinition = "enum('A', 'I') default 'I'")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public UserPostInteractions getUserPostInteractions() {
		return userPostInteractions;
	}

	public void setUserPostInteractions(UserPostInteractions userPostInteractions) {
		this.userPostInteractions = userPostInteractions;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserPost getPost() {
		return post;
	}

	public void setPost(UserPost post) {
		this.post = post;
	}
	
}

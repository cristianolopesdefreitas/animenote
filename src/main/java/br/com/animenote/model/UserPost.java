package br.com.animenote.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import br.com.animenote.constants.Status;
import br.com.animenote.utils.BaseEntity;

@Entity
@Table(name = "tb_user_post")
public class UserPost extends BaseEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	
	public UserPost() {
		this.status = Status.A;
	}
	
	@NotEmpty
	@Column(nullable = false, columnDefinition = "text")
	private String post;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	@Column(nullable = false, columnDefinition = "enum('A', 'I') default 'A'")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm")
	@Column(nullable = true)
	private Calendar postDate;

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Calendar getPostDate() {
		return postDate;
	}

	public void setPostDate(Calendar postDate) {
		this.postDate = postDate;
	}
	
}

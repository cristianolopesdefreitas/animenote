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
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import br.com.animenote.constants.Status;

@Entity(name = "tb_user_private_message")
public class UserPrivateMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Valid
	@NotBlank
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sender_id", nullable = false)
    private User userSender;
	
	@Valid
	@NotBlank
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_receiver_id", nullable = false)
    private User userReceiver;
	
	@Valid
	@NotBlank
	@Column(nullable = true, length = 255)
	private String message;
	
	@Column(nullable = false, columnDefinition = "enum('A', 'I') default 'A'")
	@Enumerated(EnumType.STRING)
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUserSender() {
		return userSender;
	}

	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}

	public User getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(User userReceiver) {
		this.userReceiver = userReceiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}

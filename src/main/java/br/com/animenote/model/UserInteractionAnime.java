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

import org.hibernate.validator.constraints.NotBlank;

import br.com.animenote.constants.AnimeInteractions;
import br.com.animenote.constants.Status;

@Entity
@Table(name = "tb_user_interaction_anime")
public class UserInteractionAnime implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public UserInteractionAnime() {
		this.status = Status.A;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "anime_id", nullable = false)
	private Anime anime;
	
	@Column(nullable = false, columnDefinition = "enum('JA_VI', 'ESTOU_VENDO', 'QUERO_VER')")
	@Enumerated(EnumType.STRING)
	private AnimeInteractions animeInteractions;
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Anime getAnime() {
		return anime;
	}

	public void setAnime(Anime anime) {
		this.anime = anime;
	}

	public AnimeInteractions getAnimeInteractions() {
		return animeInteractions;
	}

	public void setAnimeInteractions(AnimeInteractions animeInteractions) {
		this.animeInteractions = animeInteractions;
	}
	
}

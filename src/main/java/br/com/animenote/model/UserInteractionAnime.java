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

import org.hibernate.validator.constraints.NotBlank;

import br.com.animenote.constants.AnimeInteractions;

@Entity(name = "tb_user_interaction_anime")
public class UserInteractionAnime implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@NotBlank
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "anime_id", nullable = false)
	private Anime anime;
	
	@Column(nullable = false, columnDefinition = "enum('JA_VI', 'ESTOU_VENDO', 'QUERO_VER')")
	@Enumerated(EnumType.STRING)
	private AnimeInteractions animeInteractions;

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

package br.com.animenote.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import br.com.animenote.constants.Status;

@Entity(name = "tb_anime")
public class Anime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	@Valid
	private Short startYear;

	@Column(nullable = false)
	@Valid
	private Short endYear;

	@Column(nullable = false)
	@Valid
	private Integer episodeNumbers;

	@ManyToMany(targetEntity = AnimeCategory.class)
	@JoinTable(name = "tb_anime_and_anime_category", joinColumns = {
			@JoinColumn(name = "anime_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "anime_category_id", nullable = false) })
	private Set<AnimeCategory> animeCategories;

	@ManyToMany(targetEntity = AnimeCreator.class)
	@JoinTable(name = "tb_anime_and_anime_creator", joinColumns = {
			@JoinColumn(name = "anime_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "anime_creator_id", nullable = false) })
	private Set<AnimeCreator> animeCreators;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

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

	public Short getStartYear() {
		return startYear;
	}

	public void setStartYear(Short startYear) {
		this.startYear = startYear;
	}

	public Short getEndYear() {
		return endYear;
	}

	public void setEndYear(Short endYear) {
		this.endYear = endYear;
	}

	public Integer getEpisodeNumbers() {
		return episodeNumbers;
	}

	public void setEpisodeNumbers(Integer episodeNumbers) {
		this.episodeNumbers = episodeNumbers;
	}

	public Set<AnimeCategory> getAnimeCategories() {
		return animeCategories;
	}

	public void setAnimeCategories(Set<AnimeCategory> animeCategories) {
		this.animeCategories = animeCategories;
	}

	public Set<AnimeCreator> getAnimeCreators() {
		return animeCreators;
	}

	public void setAnimeCreators(Set<AnimeCreator> animeCreators) {
		this.animeCreators = animeCreators;
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

}

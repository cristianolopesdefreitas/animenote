package br.com.animenote.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.Valid;

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

	@ManyToMany
	@JoinTable(name = "tb_anime_and_anime_category", joinColumns = {
			@JoinColumn(name = "anime_id") }, inverseJoinColumns = { @JoinColumn(name = "anime_category_id") })
	private List<AnimeCategory> animeCategory;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

}

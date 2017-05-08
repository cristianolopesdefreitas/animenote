package br.com.animenote.model;

import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import br.com.animenote.constants.Status;

@Entity
@Table(name = "tb_anime")
public class Anime implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Anime() {
		this.status = Status.I;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = true, columnDefinition = "longblob")
	private byte[] image;
	
	@Column(nullable = true, length = 10)
	private String imageType;

//	@Column(nullable = false, length = 4)
//	@Size(min = 4, max = 4)
//	@Valid
//	@NotBlank
//	private Short releaseYear;
	
	@Valid
	@NotNull
	@DateTimeFormat(pattern="yyyy")
	@Column(nullable = false)
	private Calendar releaseYear;

	@Column(nullable = true)
	@Valid
	private Integer episodeNumbers;
	
	@Valid
	@NotBlank
	@Column(nullable = false, columnDefinition = "text")
	private String resume;
	
	@Valid
	@NotEmpty
	@ManyToMany(targetEntity = AnimeCategory.class)
	@JoinTable(name = "tb_anime_and_anime_category", joinColumns = {
			@JoinColumn(name = "anime_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "anime_category_id", nullable = false) })
	private Set<AnimeCategory> animeCategories;

//	@ManyToMany(targetEntity = AnimeCreator.class)
//	@JoinTable(name = "tb_anime_and_anime_creator", joinColumns = {
//			@JoinColumn(name = "anime_id", nullable = false) }, inverseJoinColumns = {
//					@JoinColumn(name = "anime_creator_id", nullable = false) })
//	private Set<AnimeCreator> animeCreator;
	
	@Valid
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "anime_creator_id", nullable = false, referencedColumnName = "id")
	private AnimeCreator animeCreator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
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

//	public Set<AnimeCreator> getAnimeCreator() {
//		return animeCreator;
//	}
//
//	public void setAnimeCreators(Set<AnimeCreator> animeCreator) {
//		this.animeCreator = animeCreator;
//	}
	
	public AnimeCreator getAnimeCreator() {
		return animeCreator;
	}

	public void setAnimeCreator(AnimeCreator animeCreator) {
		this.animeCreator = animeCreator;
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

//	public Short getReleaseYear() {
//		return releaseYear;
//	}
//
//	public void setReleaseYear(Short releaseYear) {
//		this.releaseYear = releaseYear;
//	}
	
	public Calendar getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Calendar releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

}

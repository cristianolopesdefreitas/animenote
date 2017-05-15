package br.com.animenote.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "tb_user_post_comment_report")
public class UserPostCommentReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 255)
	private String report;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_post_comment_id", nullable = false)
    private UserPostComment userPostCommentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_report_id", nullable = false)
	private User userReport;

	public User getUserReport() {
		return userReport;
	}

	public void setUserReport(User userReport) {
		this.userReport = userReport;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public UserPostComment getUserPostCommentId() {
		return userPostCommentId;
	}

	public void setUserPostCommentId(UserPostComment userPostCommentId) {
		this.userPostCommentId = userPostCommentId;
	}
	
}

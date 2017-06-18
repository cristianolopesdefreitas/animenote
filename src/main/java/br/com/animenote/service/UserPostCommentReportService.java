package br.com.animenote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.UserPostCommentReport;
import br.com.animenote.repository.UserPostCommentReportRepository;

@Service
public class UserPostCommentReportService {
	@Autowired
	private UserPostCommentReportRepository userPostCommentReportRepository;
	
	private static final Logger LOGGER = Logger.getLogger(UserPostCommentReportService.class);
	
	public UserPostCommentReport saveAndFlush(UserPostCommentReport userPostCommentReport) {
		LOGGER.info("Salvando denúncia de comentário");
		return userPostCommentReportRepository.saveAndFlush(userPostCommentReport);
	}
	
	public List<UserPostCommentReport> findAll() {
		return userPostCommentReportRepository.findAll();
	}
}

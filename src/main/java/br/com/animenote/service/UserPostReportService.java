package br.com.animenote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.UserPostReport;
import br.com.animenote.repository.UserPostReportRepository;

@Service
public class UserPostReportService {
	@Autowired
	private UserPostReportRepository userPostReportRepository;
	
	private static final Logger LOGGER = Logger.getLogger(UserPostReportService.class);
	
	public UserPostReport saveAndFlush(UserPostReport userPostReport) {
		LOGGER.info("Salvando denúncia de postagem");
		return userPostReportRepository.saveAndFlush(userPostReport);
	}
	
	public List<UserPostReport> findAll() {
		return userPostReportRepository.findAll();
	}
}

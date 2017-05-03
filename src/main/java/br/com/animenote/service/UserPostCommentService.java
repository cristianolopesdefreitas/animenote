package br.com.animenote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.UserPost;
import br.com.animenote.model.UserPostComment;
import br.com.animenote.repository.UserPostCommentRepository;

@Service
public class UserPostCommentService {
	@Autowired
	UserPostCommentRepository userPostCommentRepository;

	private static final Logger LOGGER = Logger.getLogger(UserPostService.class);

	public UserPostComment savePostComment(UserPostComment userPostComment) {
		LOGGER.info("Salvando postagem.");
		return userPostCommentRepository.saveAndFlush(userPostComment);
	}
	
	public List<UserPostComment> findByUserPost(UserPost userPost) {
		LOGGER.info("Buscando comentários da postagem.");
		return userPostCommentRepository.findByUserPost(userPost);
	}
}

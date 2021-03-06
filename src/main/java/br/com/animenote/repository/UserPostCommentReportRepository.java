package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPostCommentReport;

@Repository
public interface UserPostCommentReportRepository extends JpaRepository<UserPostCommentReport, Long> {

}

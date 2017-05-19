package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPostReport;

@Repository
public interface UserPostReportRepository extends JpaRepository<UserPostReport, Long> {

}

package br.com.animenote.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	
	@Modifying
	@Transactional
	@Query("UPDATE tb_user u SET u.status = :status WHERE u.id = :id")
	int changeStatus(@Param("id") Long long1, @Param("status") Status status);
}

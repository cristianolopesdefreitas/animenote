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
	
	User findById(Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE tb_user u SET u.status = :status WHERE u.id = :id")
	int changeStatus(@Param("id") Long id, @Param("status") Status status);
	
	@Modifying
	@Transactional
	@Query("UPDATE tb_user u SET u.avatar = :avatar WHERE u.id = :id")
	int changeAvatar(@Param("id") Long id, @Param("avatar") String avatar);
}

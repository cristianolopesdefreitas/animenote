package br.com.animenote.repository;

import java.util.Calendar;

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
	@Query("UPDATE tb_user u SET u.avatar = :avatar, u.avatarType = :avatarType WHERE u.id = :id")
	int changeAvatar(@Param("id") Long id, @Param("avatar") byte[] avatar, @Param("avatarType") String avatarType);

	@Modifying
	@Transactional
	@Query("UPDATE tb_user u SET u.name = :name, u.username = :username, u.email = :email, u.birthDate = :birthDate, u.about = :about WHERE u.id = :id")
	int updateUser(@Param("id") Long id, @Param("name") String name, @Param("username") String username,
			@Param("email") String email, @Param("birthDate") Calendar BirthDate, @Param("about") String about);
	
	@Modifying
	@Transactional
	@Query("UPDATE tb_user u SET u.password = :password WHERE u.id = :id")
	int changePassword(@Param("id") Long id, @Param("password") String string);
}

package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
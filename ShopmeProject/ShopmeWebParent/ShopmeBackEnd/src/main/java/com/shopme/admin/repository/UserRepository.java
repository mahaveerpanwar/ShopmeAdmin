package com.shopme.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.email=:email")
	public User getUserByEmail(@Param("email") String email);

	public Long countById(Integer id);

//	@Query("SELECT u FROM User u WHERE u.firstname LIKE %?1% OR u.lastname LIKE %?1% OR u.email LIKE %?1% ")
//	public Page<User> findAll(String keyword, Pageable pageable);

	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ',u.firstname, ' ',u.lastname) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);

	@Query("UPDATE User u SET u.enabled =?2 Where u.id=?1")
	@Modifying
	public void enabledStatus(Integer id, boolean enabled);

}
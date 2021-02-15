package com.smart.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.Contact;
import com.smart.entities.User;

@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Integer> {
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactsByUserId(@Param("userId") int userId,Pageable pageable);
	public List<Contact> findByNameContainingAndUser(String query, User user);
}

package com.iexpress.spring.api.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.iexpress.spring.domain.Post;
import com.iexpress.spring.domain.PostStatus;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer>{

	@Query("Select p from Post p where (p.taggedUser.id = ?1 "
			+ "or p.taggedUserFirstName = ?2 or p.taggedUserLastName = ?3) and p.creator.id = ?4 and p.isDeleted =0 ")
	List<Post> findPostWrittenForLoggedInUser(int loggedInUserId, String firstName, String lastName, int creator);
	
	@Query("Select p from Post p where p.creator.id = ?1 and p.content like ?2  and p.isDeleted =0 and p.status = ?3")
	Page<Post> listAllUsersPostByUserId(int userId, String key, PostStatus status,Pageable pagable);

	@Query("Select p from Post p where p.creator.id = ?1 and p.content like ?2  and p.isDeleted =0 ")
	Page<Post> listAllUsersPostByUserId(int userId, String key, Pageable pagable);

	@Query("Select p from Post p where p.creator.id = ?1 and p.isDeleted =0 and p.status = ?2")
	Page<Post> listAllUsersPostByUserId(int userId, PostStatus status,Pageable pagable);

	@Query("Select p from Post p where p.creator.id = ?1 and p.isDeleted =0 ")
	Page<Post> listAllUsersPostByUserId(int userId, Pageable pagable);

	@Modifying
	@Query("delete from Post p where p.id in ?1 ")
	void deleteByIds(List<Integer> questionIds);
}

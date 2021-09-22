package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer>{

}

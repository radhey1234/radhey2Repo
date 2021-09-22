package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.PostSeen;

@Repository
public interface PostSeenRepo extends JpaRepository<PostSeen, Integer> {

}

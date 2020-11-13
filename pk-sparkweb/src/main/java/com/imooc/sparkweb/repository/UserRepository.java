package com.imooc.sparkweb.repository;

import com.imooc.sparkweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}

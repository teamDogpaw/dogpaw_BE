package com.project.dogfaw.user.repository;

import com.project.dogfaw.user.model.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StackRepository extends JpaRepository<Stack, Long> {

    Optional<Stack> deleteAllByUserId(Long userId);

    List<Stack> findByUserId(Long userId);


    void deleteByUserId(Long id);
}

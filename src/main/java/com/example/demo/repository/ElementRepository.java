package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Element;

public interface ElementRepository extends JpaRepository<Element, Long> {

	List<Element> findByPublished(boolean published);

	List<Element> findByTitleContaining(String title);

}

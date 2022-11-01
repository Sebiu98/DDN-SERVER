package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Element;
import com.example.demo.repository.ElementRepository;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api")
public class ElementController {

	@Autowired
	ElementRepository elementRepository;

	@GetMapping("/elements")
	public ResponseEntity<List<Element>> getAllElements(@RequestParam(required = false) String title) {
		try {

			List<Element> elements = new ArrayList<Element>();

			if (title == null)
				elementRepository.findAll().forEach(elements::add);
			else
				elementRepository.findByTitleContaining(title).forEach(elements::add);

			if (elements.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(elements, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/elements/{id}")
	public ResponseEntity<Element> getElementById(@PathVariable("id") long id) {
		Optional<Element> elementData = elementRepository.findById(id);

		if (elementData.isPresent()) {
			return new ResponseEntity<>(elementData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/elements")
	public ResponseEntity<Element> createElement(@RequestBody Element element) {

		try {
			Element _element = elementRepository
					.save(new Element(element.getTitle(), element.getDescription(), false));
			return new ResponseEntity<>(_element, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/elements/{id}")
	public ResponseEntity<Element> updateElement (@PathVariable("id") long id, @RequestBody Element element) {
		Optional<Element> elementData = elementRepository.findById(id);
		
		if (elementData.isPresent()) {
			Element _element = elementData.get();
			_element.setTitle(element.getTitle());
			_element.setDescription(element.getDescription());
			_element.setPublished(element.isPublished());
			return new ResponseEntity<>(elementRepository.save(_element), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping("/elements/{id}")
	public ResponseEntity<Element> deleteElement(@PathVariable("id") long id) {
		try {
			elementRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/elements")
	public ResponseEntity<Element> deleteAllElements() {
		try {
			elementRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/elements/published")
	public ResponseEntity<List<Element>> findByPublished() {
		
		try {
			List<Element> elements = elementRepository.findByPublished(true);
			
			if(elements.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(elements, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}

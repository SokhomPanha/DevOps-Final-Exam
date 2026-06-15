package com.example.demo.controller;

import com.example.demo.model.Favorite;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteRepository favoriteRepository;

    public FavoriteController(FavoriteRepository favoriteRepository) { this.favoriteRepository = favoriteRepository; }

    @GetMapping
    public List<Favorite> list() { return favoriteRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> get(@PathVariable Long id) {
        return favoriteRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Favorite create(@RequestBody Favorite favorite) { return favoriteRepository.save(favorite); }
}

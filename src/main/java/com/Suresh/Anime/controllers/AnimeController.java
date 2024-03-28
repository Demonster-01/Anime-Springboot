package com.Suresh.Anime.controllers;

import com.Suresh.Anime.models.WishList;
import com.Suresh.Anime.services.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/anime")
public class AnimeController {
    private final AnimeRepository animeRepository;

    @Autowired
    public AnimeController(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }
    @GetMapping("")
    public String showAnimeList(Model model){
//        List<WishList> animes =animeRepository.findAll();
        List<WishList> animes =animeRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("animes",animes);
        return  "index"; // Update to point directly to "index.html"
    }
}

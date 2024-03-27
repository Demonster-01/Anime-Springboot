package com.Suresh.Anime.controllers;



import com.Suresh.Anime.models.WishList;
import com.Suresh.Anime.services.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/anime")

public class AnimeController {
    @Autowired
    private AnimeRepository repo;

    @GetMapping({"","/"})
    public String showAnimeList(Model model){
        List<WishList> animes =repo.findAll();
        model.addAttribute("Animes",animes);
        return  "animes/index";
    }

}


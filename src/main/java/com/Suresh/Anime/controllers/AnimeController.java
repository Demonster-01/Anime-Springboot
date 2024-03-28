package com.Suresh.Anime.controllers;

import com.Suresh.Anime.models.AnimeDto;
import com.Suresh.Anime.models.WishList;
import com.Suresh.Anime.services.AnimeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String showAnimeList(Model model) {
//        List<WishList> animes =animeRepository.findAll();
        List<WishList> animes = animeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("animes", animes);
        return "anime/index"; // Update to point directly to "index.html"
    }

    @GetMapping("/add")
    public String showAddAnime(Model model) {
        AnimeDto animeDto = new AnimeDto();
        model.addAttribute("animeDto", animeDto);
        return "anime/addAnime";

    }

    @PostMapping("/add")
    public String addAnime(
            @Valid @ModelAttribute AnimeDto animeDto, BindingResult result) {
        if (animeDto.getImageFile () . isEmpty())
        {
        result.addError (new FieldError("productDto", "imageFile", "The image file field is empty"));
        }
        if(result.hasErrors()){
            return "anime/addAnime";
        }
        return "redirect:/anime";
    }
}

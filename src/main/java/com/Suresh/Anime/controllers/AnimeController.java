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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
//import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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
            @Valid @ModelAttribute AnimeDto animeDto, BindingResult result) throws IOException {
        if (animeDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "The image file field is empty"));
        }
        if (result.hasErrors()) {
            return "anime/addAnime";
        }


        // save image file
        MultipartFile image = animeDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) { // Corrected the syntax error here
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (
                Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        WishList whistlist = new WishList();
        whistlist.setName(animeDto.getName());
        whistlist.setGenre(animeDto.getGenre());
        whistlist.setEposide(animeDto.getEpisode());
        whistlist.setDirector(animeDto.getDirector());
        whistlist.setImageFilename(storageFileName);

        animeRepository.save(whistlist);
        return "redirect:/anime";
    }

    @GetMapping ("/edit")
    public String showEditPage (Model model, @RequestParam int id){
        try {
            WishList wishList = animeRepository.findById(id).get();
            model.addAttribute("anime",wishList);
//            model.addAttribute("imageFileName", wishList.getImageFileName());


            model.addAttribute("imageFilename", wishList.getImageFilename());
            AnimeDto animeDto =new AnimeDto();
            animeDto.setName(wishList.getName());
            animeDto.setGenre(wishList.getGenre());
            animeDto.setEpisode((float) wishList.getEposide());
            animeDto.setDirector(wishList.getDirector());
//            animeDto.setImageFile(wishList.getImageFilename()); // Assuming getImageFilename() returns the image file path or URL



            model.addAttribute("wishList", wishList);
            model.addAttribute("animeDto", animeDto);


        }
        catch (Exception ex){
            System.out.println("Exception: "+ex.getMessage());
            return "redirect:/anime";
        }
        return "anime/editAnime";

    }
}

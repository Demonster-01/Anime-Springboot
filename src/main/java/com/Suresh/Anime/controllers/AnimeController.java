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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public String addAnime(@Valid @ModelAttribute AnimeDto animeDto, BindingResult result) throws IOException {
        if (animeDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("animeDto", "imageFile", "The image file field is empty"));
        }
        if (result.hasErrors()) {
            return "anime/addAnime";
        }

        MultipartFile image = animeDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
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

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            WishList wishList = animeRepository.findById(id).get();
            model.addAttribute("anime", wishList);
            model.addAttribute("imageFilename", wishList.getImageFilename());
            AnimeDto animeDto = new AnimeDto();
            animeDto.setName(wishList.getName());
            animeDto.setGenre(wishList.getGenre());
            animeDto.setEpisode((float) wishList.getEposide());
            animeDto.setDirector(wishList.getDirector());
            model.addAttribute("wishList", wishList);
            model.addAttribute("animeDto", animeDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/anime";
        }
        return "anime/editAnime";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute("animeDto") AnimeDto animeDto,
                                BindingResult result) {
        try {
            if (result.hasErrors()) {
                // If there are validation errors, return to the edit page
                return "anime/editAnime";
            }

            WishList wishList = animeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid anime Id:" + id));
            model.addAttribute("wishList", wishList);

            if (!animeDto.getImageFile().isEmpty()) {
                // Delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + wishList.getImageFilename());
                try {
                    Files.delete(oldImagePath);
                } catch (IOException ex) {
                    System.out.println("Error deleting old image: " + ex.getMessage());
                }

                // Save new image file
                MultipartFile image = animeDto.getImageFile();
                String storageFileName = image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                    wishList.setImageFilename(storageFileName);
                } catch (IOException ex) {
                    System.out.println("Error saving new image: " + ex.getMessage());
                    return "redirect:/anime";
                }
            }
            wishList.setName(animeDto.getName());
            wishList.setGenre(animeDto.getGenre());
            wishList.setEposide(animeDto.getEpisode());
            wishList.setDirector(animeDto.getDirector());
            animeRepository.save(wishList);

            return "redirect:/anime"; // Redirect to anime list after successful update
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/anime"; // Redirect to anime list if an exception occurs
        }
    }

//    @GetMapping("/delete")
//    public String deleteAnime(@RequestParam int id) {
//        try {
//            WishList wishList = animeRepository.findById(id).get();
//            // delete product image
//            Path imagePath = Paths.get("public/images/" + wishList.getImageFilename());
//            try {
//                Files.delete(imagePath);
//            } catch (Exception ex) {
//                System.out.println("Exception: " + ex.getMessage());
//            }
//            animeRepository.delete(wishList);
//        } catch (Exception ex) {
//            System.out.println("Exception: " + ex.getMessage());
//        }
//        return "redirect:/anime";
//    }




    @GetMapping("/delete")
    public String deleteAnime(@RequestParam int id) {
        try {
            Optional<WishList> optionalWishList = animeRepository.findById(id);
            if (optionalWishList.isPresent()) {
                WishList wishList = optionalWishList.get();
                // Delete product image
                Path imagePath = Paths.get("public/images/" + wishList.getImageFilename());
                try {
                    Files.delete(imagePath);
                } catch (IOException ex) {
                    System.out.println("Error deleting image: " + ex.getMessage());
                }
                // Delete wishList from the repository
                animeRepository.delete(wishList);
            } else {
                System.out.println("Anime with ID " + id + " not found.");
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred while deleting anime: " + ex.getMessage());
        }
        return "/index";
    }

}


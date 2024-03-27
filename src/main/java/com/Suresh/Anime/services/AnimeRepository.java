package com.Suresh.Anime.services;

import com.Suresh.Anime.models.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<WishList,Integer> {
}



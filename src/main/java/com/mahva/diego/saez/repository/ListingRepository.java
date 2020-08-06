package com.mahva.diego.saez.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahva.diego.saez.model.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, String> {

}

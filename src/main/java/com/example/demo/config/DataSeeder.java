package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {
    private final TerrainRepository terrainRepository;
    private final TerrainImageRepository terrainImageRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public DataSeeder(TerrainRepository terrainRepository,
                      TerrainImageRepository terrainImageRepository,
                      BookingRepository bookingRepository,
                      PaymentRepository paymentRepository,
                      ReviewRepository reviewRepository,
                      FavoriteRepository favoriteRepository) {
        this.terrainRepository = terrainRepository;
        this.terrainImageRepository = terrainImageRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (terrainRepository.count() > 0) return;

        Terrain t1 = new Terrain();
        t1.setOwnerId(1L);
        t1.setTitle("Central Park Field");
        t1.setDescription("A well maintained grass field");
        t1.setLocation("123 Main St");
        t1.setAreaSize(new BigDecimal("2000"));
        t1.setPricePerDay(new BigDecimal("120"));
        t1.setAvailableFrom(LocalDateTime.now());
        t1.setAvailableTo(LocalDateTime.now().plusMonths(6));
        terrainRepository.save(t1);

        TerrainImage img = new TerrainImage();
        img.setTerrainId(t1.getId());
        img.setImagePath("/images/central.jpg");
        terrainImageRepository.save(img);

        t1.setMainImage(img);
        terrainRepository.save(t1);

        Booking b = new Booking();
        b.setTerrainId(t1.getId());
        b.setRenterId(2L);
        b.setStartDate(LocalDateTime.now().plusDays(2));
        b.setEndDate(LocalDateTime.now().plusDays(3));
        b.setTotalPrice(new BigDecimal("240"));
        bookingRepository.save(b);

        Payment p = new Payment();
        p.setBookingId(b.getId());
        p.setPaymentMethod("card");
        p.setAmountPaid(new BigDecimal("240"));
        p.setPaymentDate(LocalDateTime.now());
        p.setTransactionId("tx123");
        paymentRepository.save(p);

        Review r = new Review();
        r.setTerrainId(t1.getId());
        r.setUserId(2L);
        r.setRating(5);
        r.setComment("Great field");
        reviewRepository.save(r);

        Favorite f = new Favorite();
        f.setTerrainId(t1.getId());
        f.setUserId(2L);
        favoriteRepository.save(f);
    }
}

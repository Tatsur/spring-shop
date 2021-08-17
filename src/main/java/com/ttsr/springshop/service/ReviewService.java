package com.ttsr.springshop.service;

import com.ttsr.springshop.model.Review;
import com.ttsr.springshop.model.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//Poltergeist, класс реализован только для того, чтобы соответствовать общей архитектуре проекта, но не несет в себе никакой ценности, являясь прослойкой
//Я не вижу это ошибкой, так как предполагается добавить функционал для работы с Review.
@Service
@RequiredArgsConstructor
public class ReviewService {
   private final ReviewRepository reviewRepository;

   public void save(Review review){
       reviewRepository.save(review);
   }

   public List<Review> findReviewByProductId(UUID id){
        return reviewRepository.findByProductId(id);
   }

}

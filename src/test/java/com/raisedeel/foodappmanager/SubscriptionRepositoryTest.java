package com.raisedeel.foodappmanager;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.subscription.repository.SubscriptionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

@DataJpaTest
@DisplayName("Tests to check if the calculation of averages of ratings is accurate for many ratings")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionRepositoryTest {

  @Autowired
  RestaurantRepository restaurantRepository;

  @Autowired
  SubscriptionRepository subscriptionRepository;

  // The size of the array can be modified depending on the number of subscriptions to test
  Subscription[] subscriptions = new Subscription[100];
  Restaurant restaurant = new Restaurant(1L, null, null, null, null, null, null, 0.0, null, null, null, null);
  double rating;

  {
    for (int i = 0; i < subscriptions.length; i++) {
      Subscription sub = new Subscription(
          i + 1L,
          (int) (Math.random() * 6),
          null,
          restaurant
      );
      subscriptions[i] = sub;

      rating += sub.getRating();
    }

    rating = rating / Arrays.stream(subscriptions).filter(sub -> sub.getRating() != 0).count();
  }

  @BeforeAll
  public void setup() {
    restaurantRepository.save(restaurant);

    for (Subscription sub : subscriptions) {
      subscriptionRepository.save(sub);
    }
  }

  @Test
  @DisplayName("Check if the average of ratings keeps being accurate after 100 subscriptions insertions")
  @Order(1)
  public void addRatingsTest() {
    Assertions.assertEquals(rating, subscriptionRepository.averageOfRatingsByRestaurantId(1L).orElse(0.0));
  }

  @Test
  @DisplayName("Check if the average of ratings keeps being accurate after a random number of subscriptions deletions")
  @Order(2)
  public void removeRatingsTest() {

    int bound = (int) (Math.random() * subscriptions.length);
    int counter = 0;
    double decreasedRating = 0.0;

    for (int i = bound; i < subscriptions.length; i++) {
      decreasedRating += subscriptions[i].getRating();
      if (subscriptions[i].getRating() != 0) counter++;
    }

    if (counter != 0) {
      decreasedRating = decreasedRating / counter;
    }

    for (int i = 0; i < bound; i++) {
      subscriptionRepository.deleteById(subscriptions[i].getId());
    }

    Assertions.assertEquals(decreasedRating, subscriptionRepository.averageOfRatingsByRestaurantId(1L).orElse(0.0));
  }

}

package com.raisedeel.foodappmanager.subscription.dto;

import com.raisedeel.foodappmanager.subscription.model.Subscription;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring"
)
public interface SubscriptionMapper {
  SubscriptionDto subscriptionToDto(Subscription subscription);

  Subscription dtoToSubscription(SubscriptionDto subscriptionDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Subscription updateSubscriptionFromDto(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription);
}

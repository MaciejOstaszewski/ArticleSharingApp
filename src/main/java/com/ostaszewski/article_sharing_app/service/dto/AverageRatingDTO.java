package com.ostaszewski.article_sharing_app.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageRatingDTO {

    private Double avgRate;

    private Long rateAmount;
}

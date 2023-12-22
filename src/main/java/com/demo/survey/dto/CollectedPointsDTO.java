package com.demo.survey.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectedPointsDTO {

    private Long surveyId;

    private Integer collectedPoints;
}

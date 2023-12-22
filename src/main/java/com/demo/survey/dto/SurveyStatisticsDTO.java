package com.demo.survey.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStatisticsDTO {

    private Long surveyId;

    private String surveyName;

    private Long numberOfCompletes;

    private Long numberOfFiltered;

    private Long numberOfRejected;

    private Double averageLengthSpent;
}

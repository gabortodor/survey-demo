package com.demo.survey.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {

    private Long id;

    private String name;

    private Long expectedCompletes;

    private Integer completionPoints;

    private Integer filteredPoints;

}

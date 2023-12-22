package com.demo.survey.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Survey implements Serializable {

    @JsonProperty("Survey Id")
    private Long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Expected completes")
    private Long expectedCompletes;

    @JsonProperty("Completion points")
    private Integer completionPoints;

    @JsonProperty("Filtered points")
    private Integer filteredPoints;

}

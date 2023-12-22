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
public class Status implements Serializable {

    @JsonProperty("Status Id")
    private Long id;

    @JsonProperty("Name")
    private String name;
}

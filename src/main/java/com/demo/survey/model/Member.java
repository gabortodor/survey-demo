package com.demo.survey.model;

import com.demo.survey.util.NumericBooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    @JsonProperty("Member Id")
    private Long id;

    @JsonProperty("Full name")
    private String fullName;

    @JsonProperty("E-mail address")
    private String email;

    @JsonProperty("Is Active")
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    private Boolean active;
}

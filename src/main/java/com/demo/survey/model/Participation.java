package com.demo.survey.model;

import com.demo.survey.util.MemberFieldDeserializer;
import com.demo.survey.util.StatusFieldDeserializer;
import com.demo.survey.util.SurveyFieldDeserializer;
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
public class Participation implements Serializable {

    @JsonProperty("Member Id")
    @JsonDeserialize(using = MemberFieldDeserializer.class)
    private Member member;

    @JsonProperty("Survey Id")
    @JsonDeserialize(using = SurveyFieldDeserializer.class)
    private Survey survey;

    @JsonProperty("Status")
    @JsonDeserialize(using = StatusFieldDeserializer.class)
    private Status status;

    @JsonProperty("Length")
    private Long length;
}

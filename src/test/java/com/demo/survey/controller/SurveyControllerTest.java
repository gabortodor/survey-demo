package com.demo.survey.controller;

import com.demo.survey.dto.CollectedPointsDTO;
import com.demo.survey.dto.MemberDTO;
import com.demo.survey.dto.SurveyDTO;
import com.demo.survey.dto.SurveyStatisticsDTO;
import com.demo.survey.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyController.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SurveyService service;

    private final String PATH  = "/demo";

    private final Long ID = 1L;

    final MemberDTO memberDTO = MemberDTO.builder().id(ID).fullName("Test Member").email("test@test.com").active(true).build();
    final SurveyDTO surveyDTO = SurveyDTO.builder().id(ID).name("Test Survey").expectedCompletes(10L).completionPoints(12).filteredPoints(5).build();
    final CollectedPointsDTO pointsDTO = CollectedPointsDTO.builder().surveyId(ID).collectedPoints(15).build();
    final SurveyStatisticsDTO statisticsDTO = SurveyStatisticsDTO.builder().build();



    @Test
    void testGetSurveyMembersShouldReturnNotFoundWhenExceptionIsThrownByService() throws Exception {
        // Given
        when(service.getSurveyMembers(ID)).thenThrow(new ResponseStatusException(NOT_FOUND));

        // When
        this.mockMvc.perform(get(PATH + "/members/" + ID)).andDo(print()).andExpect(status().isNotFound());

        // Then
        verify(service).getSurveyMembers(ID);
    }

    @Test
    void testGetSurveyMembersShouldReturnMessageFromService() throws Exception {
        // Given
        when(service.getSurveyMembers(ID)).thenReturn(List.of(memberDTO));

        // When
        final MvcResult mvcResult = this.mockMvc.perform(get(PATH + "/members/" + ID))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        final String actualResponseBody = mvcResult.getResponse().getContentAsString();

        // Then
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(List.of(memberDTO)));
        verify(service).getSurveyMembers(ID);
    }

    @Test
    void testGetCompletedSurveysForMemberShouldReturnNotFoundWhenExceptionIsThrownByService() throws Exception {
        // Given
        when(service.getCompletedSurveysForMember(ID)).thenThrow(new ResponseStatusException(NOT_FOUND));

        // When
        this.mockMvc.perform(get(PATH + "/completed-surveys/" + ID)).andDo(print()).andExpect(status().isNotFound());

        // Then
        verify(service).getCompletedSurveysForMember(ID);
    }

    @Test
    void testGetCompletedSurveysForMemberShouldReturnMessageFromService() throws Exception {
        // Given
        when(service.getCompletedSurveysForMember(ID)).thenReturn(List.of(surveyDTO));

        // When
        final MvcResult mvcResult = this.mockMvc.perform(get(PATH + "/completed-surveys/" + ID))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        final String actualResponseBody = mvcResult.getResponse().getContentAsString();

        // Then
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(List.of(surveyDTO)));
        verify(service).getCompletedSurveysForMember(ID);
    }

    @Test
    void testGetPointsForMemberShouldReturnNotFoundWhenExceptionIsThrownByService() throws Exception {
        // Given
        when(service.getPointsForMember(ID)).thenThrow(new ResponseStatusException(NOT_FOUND));

        // When
        this.mockMvc.perform(get(PATH + "/points/" + ID)).andDo(print()).andExpect(status().isNotFound());

        // Then
        verify(service).getPointsForMember(ID);
    }

    @Test
    void testGetPointsForMemberShouldReturnMessageFromService() throws Exception {
        // Given
        when(service.getPointsForMember(ID)).thenReturn(List.of(pointsDTO));

        // When
        final MvcResult mvcResult = this.mockMvc.perform(get(PATH + "/points/" + ID))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        final String actualResponseBody = mvcResult.getResponse().getContentAsString();

        // Then
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(List.of(pointsDTO)));
        verify(service).getPointsForMember(ID);
    }

    @Test
    void testGetInvitableMemberForSurveyShouldReturnNotFoundWhenExceptionIsThrownByService() throws Exception {
        // Given
        when(service.getInvitableMemberForSurvey(ID)).thenThrow(new ResponseStatusException(NOT_FOUND));

        // When
        this.mockMvc.perform(get(PATH + "/invitable-members/" + ID)).andDo(print()).andExpect(status().isNotFound());

        // Then
        verify(service).getInvitableMemberForSurvey(ID);
    }

    @Test
    void testGetInvitableMemberForSurveyShouldReturnMessageFromService() throws Exception {
        // Given
        when(service.getInvitableMemberForSurvey(ID)).thenReturn(List.of(memberDTO));

        // When
        final MvcResult mvcResult = this.mockMvc.perform(get(PATH + "/invitable-members/" + ID))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        final String actualResponseBody = mvcResult.getResponse().getContentAsString();

        // Then
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(List.of(memberDTO)));
        verify(service).getInvitableMemberForSurvey(ID);
    }

    @Test
    void testGetSurveyStatisticsShouldReturnNotFoundWhenExceptionIsThrownByService() throws Exception {
        // Given
        when(service.getSurveyStatistics()).thenThrow(new ResponseStatusException(NOT_FOUND));

        // When
        this.mockMvc.perform(get(PATH + "/statistics")).andDo(print()).andExpect(status().isNotFound());

        // Then
        verify(service).getSurveyStatistics();
    }

    @Test
    void testGetSurveyStatisticsShouldReturnMessageFromService() throws Exception {
        // Given
        when(service.getSurveyStatistics()).thenReturn(List.of(statisticsDTO));

        // When
        final MvcResult mvcResult = this.mockMvc.perform(get(PATH + "/statistics"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        final String actualResponseBody = mvcResult.getResponse().getContentAsString();

        // Then
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(List.of(statisticsDTO)));
        verify(service).getSurveyStatistics();
    }


}

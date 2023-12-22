package com.demo.survey.service;

import com.demo.survey.dto.CollectedPointsDTO;
import com.demo.survey.dto.MemberDTO;
import com.demo.survey.dto.SurveyDTO;
import com.demo.survey.dto.SurveyStatisticsDTO;
import com.demo.survey.model.Member;
import com.demo.survey.model.Participation;
import com.demo.survey.model.Status;
import com.demo.survey.model.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class SurveyServiceTest {
    private final SurveyService underTest = new SurveyService(new ModelMapper());

    final Long INVALID_ID = 0L;
    final Long FIRST_ID = 1L;
    final Long SECOND_ID = 2L;
    final Long THIRD_ID = 3L;
    final Long FOURTH_ID = 4L;

    @BeforeEach
    public void testSetup() {
        final Member member1 = Member.builder().id(FIRST_ID).fullName("Test Member1").email("test1@test.com").active(true).build();
        final Member member2 = Member.builder().id(SECOND_ID).fullName("Test Member2").email("test2@test.com").active(true).build();
        final Member member3 = Member.builder().id(THIRD_ID).fullName("Test Member3").email("test3@test.com").active(false).build();

        final Survey survey1 = Survey.builder().id(FIRST_ID).name("Test Survey1").expectedCompletes(10L).completionPoints(12).filteredPoints(5).build();
        final Survey survey2 = Survey.builder().id(SECOND_ID).name("Test Survey2").expectedCompletes(25L).completionPoints(20).filteredPoints(10).build();
        final Survey survey3 = Survey.builder().id(THIRD_ID).name("Test Survey3").expectedCompletes(3L).completionPoints(7).filteredPoints(1).build();

        final Status status1 = Status.builder().id(FIRST_ID).name("Not asked").build();
        final Status status2 = Status.builder().id(SECOND_ID).name("Rejected").build();
        final Status status3 = Status.builder().id(THIRD_ID).name("Filtered").build();
        final Status status4 = Status.builder().id(FOURTH_ID).name("Completed").build();

        final Participation participation1 = Participation.builder().member(member1).survey(survey1).status(status4).length(10L).build();
        final Participation participation2 = Participation.builder().member(member2).survey(survey1).status(status4).length(5L).build();
        final Participation participation3 = Participation.builder().member(member3).survey(survey1).status(status1).length(null).build();
        final Participation participation4 = Participation.builder().member(member1).survey(survey2).status(status3).length(15L).build();
        final Participation participation5 = Participation.builder().member(member2).survey(survey2).status(status4).length(30L).build();
        final Participation participation6 = Participation.builder().member(member3).survey(survey2).status(status2).length(null).build();
        final Participation participation7 = Participation.builder().member(member1).survey(survey3).status(status2).length(null).build();
        final Participation participation8 = Participation.builder().member(member3).survey(survey3).status(status2).length(null).build();

        SurveyService.memberMap = Map.of(FIRST_ID, member1, SECOND_ID, member2, THIRD_ID, member3);
        SurveyService.surveyMap = Map.of(FIRST_ID, survey1, SECOND_ID, survey2, THIRD_ID, survey3);
        SurveyService.statusMap = Map.of(FIRST_ID, status1, SECOND_ID, status2, THIRD_ID, status3, FOURTH_ID, status4);
        SurveyService.participationList = List.of(participation1, participation2, participation3,
                participation4, participation5, participation6, participation7, participation8);
    }

    @Test
    public void testGetSurveyMembersShouldThrowResponseStatusExceptionWhenSurveyNotFound() {
        // Given - When - Then
        assertThrows(ResponseStatusException.class, () -> underTest.getSurveyMembers(INVALID_ID));
    }

    @Test
    public void testGetSurveyMembersShouldReturnEmptyListWhenNoMemberCompletedTheSurvey() {
        // Given - When
        List<MemberDTO> actual = underTest.getSurveyMembers(THIRD_ID);

        // Then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetSurveyMembersShouldReturnCorrectListWhenMembersCompletedTheSurvey() {
        // Given - When
        List<MemberDTO> actual1 = underTest.getSurveyMembers(FIRST_ID);
        List<MemberDTO> actual2 = underTest.getSurveyMembers(SECOND_ID);

        // Then
        assertEquals(2, actual1.size());
        assertTrue(actual1.stream().anyMatch(t -> FIRST_ID.equals(t.getId())));
        assertTrue(actual1.stream().anyMatch(t -> SECOND_ID.equals(t.getId())));

        assertEquals(1, actual2.size());
        assertTrue(actual2.stream().anyMatch(t -> SECOND_ID.equals(t.getId())));
    }

    @Test
    public void testGetCompletedSurveysForMemberShouldThrowResponseStatusExceptionWhenMemberNotFound() {
        // Given - When - Then
        assertThrows(ResponseStatusException.class, () -> underTest.getCompletedSurveysForMember(INVALID_ID));
    }

    @Test
    public void testGetCompletedSurveysForMemberShouldReturnEmptyListWhenMemberNotCompletedAnySurvey() {
        // Given - When
        List<SurveyDTO> actual = underTest.getCompletedSurveysForMember(THIRD_ID);

        // Then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetCompletedSurveysForMemberShouldReturnCorrectListWhenMemberCompletedSurveys() {
        // Given - When
        List<SurveyDTO> actual1 = underTest.getCompletedSurveysForMember(FIRST_ID);
        List<SurveyDTO> actual2 = underTest.getCompletedSurveysForMember(SECOND_ID);

        // Then
        assertEquals(1, actual1.size());
        assertTrue(actual1.stream().anyMatch(t -> FIRST_ID.equals(t.getId())));

        assertEquals(2, actual2.size());
        assertTrue(actual2.stream().anyMatch(t -> FIRST_ID.equals(t.getId())));
        assertTrue(actual2.stream().anyMatch(t -> SECOND_ID.equals(t.getId())));
    }

    @Test
    public void testGetPointsForMemberShouldThrowResponseStatusExceptionWhenMemberNotFound() {
        // Given - When - Then
        assertThrows(ResponseStatusException.class, () -> underTest.getPointsForMember(INVALID_ID));
    }

    @Test
    public void testGetPointsForMemberShouldReturnEmptyListWhenMemberDoesNotHaveAnyPoints() {
        // Given - When
        List<CollectedPointsDTO> actual = underTest.getPointsForMember(THIRD_ID);

        // Then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetPointsForMemberShouldReturnCorrectListWhenMemberGotPoints() {
        // Given - When
        List<CollectedPointsDTO> actual1 = underTest.getPointsForMember(FIRST_ID);
        List<CollectedPointsDTO> actual2 = underTest.getPointsForMember(SECOND_ID);

        // Then
        assertEquals(2, actual1.size());
        assertTrue(actual1.stream().anyMatch(t -> FIRST_ID.equals(t.getSurveyId())));
        assertTrue(actual1.stream().anyMatch(t -> t.getCollectedPoints().equals(10)));
        assertTrue(actual1.stream().anyMatch(t -> SECOND_ID.equals(t.getSurveyId())));
        assertTrue(actual1.stream().anyMatch(t -> t.getCollectedPoints().equals(12)));

        assertEquals(2, actual2.size());
        assertTrue(actual2.stream().anyMatch(t -> FIRST_ID.equals(t.getSurveyId())));
        assertTrue(actual2.stream().anyMatch(t -> t.getCollectedPoints().equals(20)));
        assertTrue(actual2.stream().anyMatch(t -> SECOND_ID.equals(t.getSurveyId())));
        assertTrue(actual2.stream().anyMatch(t -> t.getCollectedPoints().equals(12)));
    }

    @Test
    public void testGetInvitableMemberForSurveyShouldThrowResponseStatusExceptionWhenSurveyNotFound() {
        // Given - When - Then
        assertThrows(ResponseStatusException.class, () -> underTest.getInvitableMemberForSurvey(INVALID_ID));
    }

    @Test
    public void testGetInvitableMemberForSurveyShouldReturnEmptyListWhenNoMemberCanBeInvited() {
        // Given - When
        List<MemberDTO> actual1 = underTest.getInvitableMemberForSurvey(FIRST_ID);
        List<MemberDTO> actual2 = underTest.getInvitableMemberForSurvey(SECOND_ID);

        // Then
        assertTrue(actual1.isEmpty());
        assertTrue(actual2.isEmpty());
    }

    @Test
    public void testGetInvitableMemberForSurveyShouldReturnCorrectListWhenMembersCanBeInvited() {
        // Given - When
        List<MemberDTO> actual = underTest.getInvitableMemberForSurvey(THIRD_ID);

        // Then
        assertEquals(1, actual.size());
        assertTrue(actual.stream().anyMatch(t -> SECOND_ID.equals(t.getId())));

    }

    @Test
    public void testGetSurveyStatisticsShouldReturnCorrectStatisticsList() {
        // Given - When
        List<SurveyStatisticsDTO> actual = underTest.getSurveyStatistics();

        // Then
        assertEquals(3, actual.size());
        List<SurveyStatisticsDTO> list1 = actual.stream().filter(t -> FIRST_ID.equals(t.getSurveyId())).toList();
        assertEquals(1, list1.size());
        SurveyStatisticsDTO statistics1 = list1.get(0);
        assertEquals(7.5, statistics1.getAverageLengthSpent());
        assertEquals(2, statistics1.getNumberOfCompletes());
        assertEquals(0, statistics1.getNumberOfFiltered());
        assertEquals(0, statistics1.getNumberOfRejected());

        List<SurveyStatisticsDTO> list2 = actual.stream().filter(t -> SECOND_ID.equals(t.getSurveyId())).toList();
        assertEquals(list2.size(), 1);
        SurveyStatisticsDTO statistics2 = list2.get(0);
        assertEquals(22.5, statistics2.getAverageLengthSpent());
        assertEquals(1, statistics2.getNumberOfCompletes());
        assertEquals(1, statistics2.getNumberOfFiltered());
        assertEquals(1, statistics2.getNumberOfRejected());

        List<SurveyStatisticsDTO> list3 = actual.stream().filter(t -> THIRD_ID.equals(t.getSurveyId())).toList();
        assertEquals(list2.size(), 1);
        SurveyStatisticsDTO statistics3 = list3.get(0);
        assertEquals(Double.NaN, statistics3.getAverageLengthSpent());
        assertEquals(0, statistics3.getNumberOfCompletes());
        assertEquals(0, statistics3.getNumberOfFiltered());
        assertEquals(2, statistics3.getNumberOfRejected());
    }

}

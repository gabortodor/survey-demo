package com.demo.survey.controller;

import com.demo.survey.dto.CollectedPointsDTO;
import com.demo.survey.dto.MemberDTO;
import com.demo.survey.dto.SurveyDTO;
import com.demo.survey.dto.SurveyStatisticsDTO;
import com.demo.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(final SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * A GET endpoint which retrieves a list of members who completed the survey with the given id.
     * @param surveyId Path variable, the id of the survey.
     * @return A {@link List} of {@link MemberDTO}.
     */
    @GetMapping("/members/{surveyId}")
    public List<MemberDTO> getSurveyMembers(@PathVariable final Long surveyId) {
        return surveyService.getSurveyMembers(surveyId);
    }

    /**
     * A GET endpoint which retrieves a list of surveys that were completed by the member with the given id.
     * @param memberId Path variable, the id of the member.
     * @return A {@link List} of {@link SurveyDTO}.
     */
    @GetMapping("/completed-surveys/{memberId}")
    public List<SurveyDTO> getCompletedSurveysForMember(@PathVariable final Long memberId) {
        return surveyService.getCompletedSurveysForMember(memberId);
    }

    /**
     * A GET endpoint which retrieves a list of surveyId-points pairs that represent
     * the points collected by the member with the given id.
     * @param memberId Path variable, the id of the member.
     * @return A {@link List} of {@link CollectedPointsDTO}.
     */
    @GetMapping("/points/{memberId}")
    public List<CollectedPointsDTO> getPointsForMember(@PathVariable final Long memberId) {
        return surveyService.getPointsForMember(memberId);
    }

    /**
     * A GET endpoint which retrieves a list of currently active members who can be
     * invited to participate in the survey with the given id.
     * @param surveyId Path variable, the id of the survey.
     * @return A {@link List} of {@link MemberDTO}.
     */
    @GetMapping("/invitable-members/{surveyId}")
    public List<MemberDTO> getInvitableMemberForSurvey(@PathVariable final Long surveyId) {
        return surveyService.getInvitableMemberForSurvey(surveyId);
    }

    /**
     * A GET endpoint which retrieves a list of statistics
     * that contains information about each survey.
     *
     * @return A {@link List} of {@link SurveyStatisticsDTO}.
     */
    @GetMapping("/statistics")
    public List<SurveyStatisticsDTO> getSurveyStatistics() {
        return surveyService.getSurveyStatistics();
    }
}

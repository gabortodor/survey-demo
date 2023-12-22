package com.demo.survey.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO  {

    private Long id;

    private String fullName;

    private String email;

    private Boolean active;
}

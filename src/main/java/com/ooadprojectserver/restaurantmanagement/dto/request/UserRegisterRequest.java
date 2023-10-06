package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.constant.RoleConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    @JsonProperty("password")
    private String password;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("role")
    private RoleConstant.ROLE role;
    @JsonProperty("status")
    private Integer status;

    //    Address
    @JsonProperty("address_line")
    private String addressLine;
    @JsonProperty("city")
    private String city;
    @JsonProperty("region")
    private String region;

    //    Owner
    @JsonProperty("branch")
    private String branch;
    @JsonProperty("license_business")
    private String licenseBusiness;

    //    Manager
    @JsonProperty("experienced_year")
    private String experiencedYear;
    @JsonProperty("certification_management")
    private String certificationManagement;

    //    Staff
    @JsonProperty("foreign_language")
    private String foreignLanguage;
    @JsonProperty("academic_level")
    private String academicLevel;
}

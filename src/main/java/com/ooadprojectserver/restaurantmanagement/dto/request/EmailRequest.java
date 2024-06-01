package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    @JsonProperty("emailTo")
    private String emailTo;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("content")
    private String content;
    @JsonProperty("file_path")
    private String filePath;

    @Override
    public String toString() {
        return "EmailRequest{" +
                "emailTo='" + emailTo + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

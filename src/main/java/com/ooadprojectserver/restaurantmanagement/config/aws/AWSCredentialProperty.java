package com.ooadprojectserver.restaurantmanagement.config.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/
@Data
@ConfigurationProperties(prefix = "spring.aws")
public class AWSCredentialProperty {
    private String accessKey;
    private String secretKey;
    private String region;
}

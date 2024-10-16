package com.task.onlinecoursemanagementsystem.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum AllowedFileTypes {
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf");

    private final String value;

    public static Set<String> getValues(AllowedFileTypes... type) {
        return Arrays.stream(type)
                .map(AllowedFileTypes::getValue)
                .collect(Collectors.toSet());
    }

}

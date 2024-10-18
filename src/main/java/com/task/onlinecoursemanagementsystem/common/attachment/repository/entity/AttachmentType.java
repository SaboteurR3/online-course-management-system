package com.task.onlinecoursemanagementsystem.common.attachment.repository.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AttachmentType {
    SYLLABUS("სილაბუსი"),
    LESSONS("გაკვეთილები");

    private final String typeName;
}

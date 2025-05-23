package com.osiki.World_Banking_Application.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String recipient;

    private String messageBody;

    private String attachment;

    private String subject;

    private boolean isHtml;
}

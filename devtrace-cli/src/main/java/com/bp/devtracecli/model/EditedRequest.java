package com.bp.devtracecli.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditedRequest {

    private String headers;
    private String body;
}

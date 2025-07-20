package org.varayasolusi.saktiauth.infrastructure.model;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseModel {

	@JsonIgnore
	private int httpStatusCode;
	private String responseCode;
    private String responseMessage;
    private Timestamp timeStamp;
    private Object data;
}

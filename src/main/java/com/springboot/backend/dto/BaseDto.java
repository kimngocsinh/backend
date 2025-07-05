package com.springboot.backend.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2697372426703892581L;

    private Long id;

    private Date createdDate;

    private Date modifiedDate;

    private String createdBy;

    private String modifiedBy;

}

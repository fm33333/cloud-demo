package com.demo.feign.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 * @TableName user
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String password;

    private static final long serialVersionUID = 1L;
}
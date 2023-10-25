package com.demo.feign.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserDTO implements Serializable {
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

    private String serverName;

    private String uri;

    private static final long serialVersionUID = 1L;
}
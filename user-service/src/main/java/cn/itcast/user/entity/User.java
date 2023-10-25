package cn.itcast.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName user
 */
@Data
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
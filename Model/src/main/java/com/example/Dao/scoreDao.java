package com.example.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class scoreDao {
    private String userId;
    private int count;
    private String userImg;
    private String userName;
}

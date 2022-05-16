package com.example.application.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TrafficCount {

  private Integer id;

  private String name;

  private String host;

  private Long count;

  private LocalDateTime date;
  
  private String userId;

}

package com.todolist.todolist.controllers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Error {
    
   private Long timestamp;
   private Integer status;
   private String title;
   private String message;
   private String path;

}

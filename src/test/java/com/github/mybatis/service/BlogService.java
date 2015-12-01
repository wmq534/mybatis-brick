package com.github.mybatis.service;

import com.github.mybatis.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class BlogService {
  @SuppressWarnings("unused")
  @Autowired
  private BlogMapper mapper;
}

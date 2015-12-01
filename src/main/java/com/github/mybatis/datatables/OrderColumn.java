package com.github.mybatis.datatables;

import java.io.Serializable;

/**
 * 排序字段
 * Created by kevin on 15/2/8.
 */
public class OrderColumn implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8935112959563927697L;
  private int column;
  private String dir;

  public OrderColumn(int column, String dir) {
    this.column = column;
    this.dir = dir;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }
}

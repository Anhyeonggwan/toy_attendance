package com.attendance.dao;

import org.apache.ibatis.annotations.Mapper;
import com.attendance.vo.Sample;

@Mapper
public interface SampleDao {

  public int insertSample(Sample sample);

}

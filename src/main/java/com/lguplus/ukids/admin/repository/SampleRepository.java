package com.lguplus.ukids.admin.repository;

import java.util.List;

import com.lguplus.ukids.admin.dto.SampleDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleRepository {
    List<SampleDto> selectSampleAll() throws Exception;

    List<SampleDto> selectSamples(SampleDto sample) throws Exception;

    SampleDto selectSampleDetail(String id) throws Exception;

    int insertSample(SampleDto sample) throws Exception;

    int deleteSample(SampleDto sample) throws Exception;

    int updateSample(SampleDto sample) throws Exception;
}

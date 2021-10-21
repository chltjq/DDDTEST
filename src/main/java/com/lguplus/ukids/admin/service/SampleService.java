package com.lguplus.ukids.admin.service;

import java.util.List;

import com.lguplus.ukids.admin.constants.StatusCodeConstants;
import com.lguplus.ukids.admin.dto.CommonCUDQueryReturnDto;
import com.lguplus.ukids.admin.dto.SampleDto;
import com.lguplus.ukids.admin.exception.BusinessException;
import com.lguplus.ukids.admin.repository.SampleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sampleService")
public class SampleService {
    @Autowired
    private SampleRepository sampleRepository;

    public List<SampleDto> retrieveSampleAll() throws Exception {
        List<SampleDto> results = sampleRepository.selectSampleAll();
        return results;
    }

    public List<SampleDto> retrieveSamples(final SampleDto sample) throws Exception {
        List<SampleDto> result = sampleRepository.selectSamples(sample);
        return result;
    }

    public SampleDto retrieveSampleDetail(final String id) throws Exception {
        SampleDto result = sampleRepository.selectSampleDetail(id);
        return result;
    }

    // exception 시 rollback(예제) - 여기서는 transaction은 없음
    @Transactional(rollbackFor = Exception.class)
    public CommonCUDQueryReturnDto createSample(final SampleDto sample) throws Exception {
        SampleDto dupSample = retrieveSampleDetail(sample.getId());
        if (dupSample != null) {
            throw new BusinessException("Duplication id", StatusCodeConstants.INVALID_PARAMETERS);
        }
        int result = sampleRepository.insertSample(sample);
        return CommonCUDQueryReturnDto.builder().affectedRows(result).build();
    }

    public CommonCUDQueryReturnDto updateSample(final SampleDto sample) throws Exception {
        int result = sampleRepository.updateSample(sample);
        return CommonCUDQueryReturnDto.builder().affectedRows(result).build();
    }

    public CommonCUDQueryReturnDto deleteSample(final SampleDto sample) throws Exception {
        int result = sampleRepository.deleteSample(sample);
        return CommonCUDQueryReturnDto.builder().affectedRows(result).build();
    }

}

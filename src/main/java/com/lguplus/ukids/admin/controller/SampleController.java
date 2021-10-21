package com.lguplus.ukids.admin.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.lguplus.ukids.admin.constants.ResponseEntityConstants;
import com.lguplus.ukids.admin.constants.StatusCodeConstants;
import com.lguplus.ukids.admin.dto.CommonCUDQueryReturnDto;
import com.lguplus.ukids.admin.dto.CommonReturnDto;
import com.lguplus.ukids.admin.dto.SampleDto;
import com.lguplus.ukids.admin.exception.BusinessException;
import com.lguplus.ukids.admin.service.SampleService;
import com.lguplus.ukids.admin.utility.ValidateUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Sample Controller", description = "Sample 컨트롤러")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @Operation(summary = "전체샘플목록조회", description = "전체 샘플 목록을 조회한다.")
    @GetMapping(path = "/v1/samples", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonReturnDto<List<SampleDto>>> retrieveSampleAll() throws Exception {
        return new ResponseEntity<>(
            CommonReturnDto.<List<SampleDto>>builder().successOrNot(ResponseEntityConstants.SUCCESS_YES_FLAG)
                        .statusCode(StatusCodeConstants.OK).data(sampleService.retrieveSampleAll()).build(),
                OK);
    }

    @Operation(summary = "샘플목록조회", description = "이름 like로 샘플 목록을 조회한다.")
    @GetMapping(path = "/v1/samples/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonReturnDto<List<SampleDto>>> retrieveSamples(
        @Parameter(description="이름", example="경종") @PathVariable("name") final String name
    ) throws Exception {
        return new ResponseEntity<>(
            CommonReturnDto.<List<SampleDto>>builder().successOrNot(ResponseEntityConstants.SUCCESS_YES_FLAG)
                        .statusCode(StatusCodeConstants.OK).data(sampleService.retrieveSamples(SampleDto.builder().name(name).build())).build(),
                OK);
    }

    @Operation(summary = "샘플상세조회", description = "아이디로 샘플 목록을 조회한다.")
    @GetMapping(path = "/v1/sample/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonReturnDto<SampleDto>> retrieveSampleDetail(
        @Parameter(description="아이디", example="1") @RequestParam(required = true) final String id
    ) throws Exception {
        return new ResponseEntity<>(
            CommonReturnDto.<SampleDto>builder().successOrNot(ResponseEntityConstants.SUCCESS_YES_FLAG)
                        .statusCode(StatusCodeConstants.OK).data(sampleService.retrieveSampleDetail(id)).build(),
                OK);
    }

    @Operation(summary = "샘플생성", description = "샘플을 생성 한다.")
    @PostMapping(path = "/v1/sample", produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<CommonReturnDto<CommonCUDQueryReturnDto>> createSample(
        @Parameter(description="샘플 생성 요청 정보") @RequestBody final SampleDto requestParam
        ) throws Exception {
        if ( ValidateUtility.isEmpty(requestParam.getId())
            || ValidateUtility.isEmpty(requestParam.getName())
        ) {
            throw new BusinessException("id, name", StatusCodeConstants.MANDATORY_PARAM_ERR);
        }

        return new ResponseEntity<>(
            CommonReturnDto.<CommonCUDQueryReturnDto>builder().successOrNot(ResponseEntityConstants.SUCCESS_YES_FLAG)
                        .statusCode(StatusCodeConstants.OK).data(sampleService.createSample(requestParam)).build(),
                OK);
    }

    @Operation(summary = "샘플수정", description = "샘플을 수정한다")
    @PutMapping(path = "/v1/sample/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<CommonReturnDto<CommonCUDQueryReturnDto>> updateContentsComments(
        @Parameter(description="아이디", example="1") @PathVariable("id") final String id,
        @Parameter(description="샘플 수정 요청 정보") @RequestBody final SampleDto requestParam) throws Exception {
        if ( ValidateUtility.isEmpty(id) || ValidateUtility.isEmpty(requestParam.getName())) {
            throw new BusinessException("id, name", StatusCodeConstants.MANDATORY_PARAM_ERR);
        }

        SampleDto param = SampleDto.builder().id(id).name(requestParam.getName()).build();

        return new ResponseEntity<>(
            CommonReturnDto.<CommonCUDQueryReturnDto>builder().successOrNot(ResponseEntityConstants.SUCCESS_YES_FLAG)
                        .statusCode(StatusCodeConstants.OK).data(sampleService.updateSample(param)).build(),
                OK);
    }

    @Operation(summary = "샘플 삭제", description = "샘플을 삭제한다.")
    @DeleteMapping(path = "/v1/sample/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonReturnDto<CommonCUDQueryReturnDto>> deleteContentsComments(
        @Parameter(description="아이디", example="1") @PathVariable("id") final String id) throws Exception {
        if ( ValidateUtility.isEmpty(id)) {
            throw new BusinessException("id", StatusCodeConstants.MANDATORY_PARAM_ERR);
        }

        SampleDto param = SampleDto.builder().id(id).build();

        return new ResponseEntity<>(
            CommonReturnDto.<CommonCUDQueryReturnDto>builder().successOrNot(ResponseEntityConstants.SUCCESS_YES_FLAG)
                        .statusCode(StatusCodeConstants.OK).data(sampleService.deleteSample(param)).build(),
                OK);
    }
}

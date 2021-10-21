package com.lguplus.ukids.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "View Controller", description = "JSP View 컨트롤러")
public class ViewController {

    @Operation(summary = "view 응답", description = "전달받은 경로의 jsp를 응답한다.")
    @GetMapping(path = "/v1/pages")
    public String jspViewPages(
        Model model,
        @Parameter(description="jsp", example="ukidsHello") @RequestParam(required = true) final String view
    ) throws Exception {
        model.addAttribute("view", view);
        return view;
    }
}

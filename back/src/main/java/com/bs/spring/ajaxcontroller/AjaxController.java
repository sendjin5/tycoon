package com.bs.spring.ajaxcontroller;


import com.bs.spring.member.model.dto.Member;
import com.bs.spring.member.model.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class AjaxController {
    //    private final BoardService boardService;
    private final MemberService memberService;
    @GetMapping("/")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("Backend Start");
    }
    }
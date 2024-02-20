package backend.sculptor.domain.stone.controller;

import backend.sculptor.domain.stone.dto.SculptorResultDTO;
import backend.sculptor.domain.stone.dto.StoneAchievesListDTO;
import backend.sculptor.domain.stone.dto.StoneSculptRequest;
import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.repository.StoneRepository;
import backend.sculptor.domain.stone.service.AchieveService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AchieveController {

    private final AchieveService achieveService;
    private final StoneRepository stoneRepository;

    //[POST] 돌 조각하기_달성현황 기록하기
    @PostMapping("/stones/{stoneId}/sculpt")
    public APIBody<SculptorResultDTO> sculptStone(@CurrentUser SessionUser user, @PathVariable UUID stoneId, @RequestBody StoneSculptRequest request) {
        try {
            // 유효성 검사
            if (request == null || !request.isValid()) {
                return APIBody.of(400, "잘못된 요청 데이터입니다.", null);
            }
            SculptorResultDTO newAchieve = achieveService.sculptStone(user.getId(), stoneId, request);
            if (newAchieve == null) {

                //달성 기록 실패 (서비스 로직 내 실패)
                return APIBody.of(500, "달성 현황을 기록하지 못했습니다.", null);
            }
            //달성 현황 기록 성공
            return APIBody.of(200, "달성 현황 기록 성공", newAchieve);
        } catch (Exception e) {
            //기타 서버 오류
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }

    }

    //[GET] 돌 달성현황 전체 조회
    @GetMapping("/workplace/stones/{stoneId}/achieves")
    public APIBody<StoneAchievesListDTO> getAllAchievesByStoneId(@CurrentUser SessionUser user, @PathVariable UUID stoneId) {
        if (user == null) {
            return APIBody.of(401, "인증되지 않은 사용자입니다.", null);
        }
        try {
            // 돌의 존재 여부 확인
            Optional<Stone> stoneOptional = stoneRepository.findById(stoneId);
            if (stoneOptional.isEmpty()) {
                // 돌을 찾지 못한 경우
                return APIBody.of(404, "해당 돌을 찾을 수 없습니다.", null);
            }

            StoneAchievesListDTO stoneAchieves = achieveService.findAllAchievesByStoneId(stoneId);
            // 달성 기록이 없거나 achieveDTOs 리스트가 비어 있는 경우
            if (stoneAchieves == null || stoneAchieves.getAchieves().isEmpty()) {
                // 달성 현황 카운트를 모두 0으로 설정
                Map<String, Long> emptyAchievementCounts = new HashMap<>();
                emptyAchievementCounts.put("A", 0L);
                emptyAchievementCounts.put("B", 0L);
                emptyAchievementCounts.put("C", 0L);

                // 새로운 StoneAchievesListDTO 객체 생성하여 반환
                stoneAchieves = new StoneAchievesListDTO(stoneId, emptyAchievementCounts, new ArrayList<>());
            }
            return APIBody.of(200, "달성 기록 조회 성공", stoneAchieves);
        } catch (Exception e) {
            return APIBody.of(500, "서버 오류 발생: " + e.getMessage(), null);
        }
    }

    //돌 이끼 제거


    //돌 균열 메꾸기


}


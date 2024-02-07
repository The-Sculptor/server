package backend.sculptor.domain.museum.controller;

import backend.sculptor.domain.comment.dto.CommentDTO;
import backend.sculptor.domain.comment.service.CommentService;
import backend.sculptor.domain.museum.dto.MuseumDTO;
import backend.sculptor.domain.museum.dto.MuseumDetailDTO;
import backend.sculptor.domain.museum.service.MuseumDetailService;
import backend.sculptor.domain.museum.service.MuseumService;
import backend.sculptor.domain.user.entity.SessionUser;
import backend.sculptor.global.api.APIBody;
import backend.sculptor.global.oauth.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/museum")
public class MuseumController {
    private final MuseumService museumService;
    private final MuseumDetailService museumDetailService;
    private final CommentService commentService;

    @GetMapping("/users/{ownerId}")
    public APIBody<MuseumDTO> getMuseumInfo(@CurrentUser SessionUser user, @PathVariable UUID ownerId) {
        MuseumDTO museumDTO = museumService.getMuseumInfo(ownerId, user.getId());
        return APIBody.of(HttpStatus.OK.value(), "박물관 조회 성공", museumDTO);
    }

    @GetMapping("/stones/{stoneId}")
    public APIBody<MuseumDetailDTO> getStoneDetail(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        MuseumDetailDTO museumDetail = museumDetailService.getMuseumDetailInfo(user.getId(), stoneId);
        return APIBody.of(HttpStatus.OK.value(), "박물관 세부 조회 성공", museumDetail);
    }

    @GetMapping("/stones/{stoneId}/comments")
    public APIBody<List<CommentDTO.Info>> getComments(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId) {
        List<CommentDTO.Info> comments = commentService.getComments(user.getId(), stoneId);
        return APIBody.of(HttpStatus.OK.value(), "방명록 조회 성공", comments);
    }
    
    @PostMapping("/stones/{stoneId}/comments")
    public APIBody<CommentDTO.Response> saveComment(
            @CurrentUser SessionUser user,
            @PathVariable UUID stoneId,
            @RequestBody CommentDTO.Request commentRequest) {
        CommentDTO.Response savedComment = commentService.createComment(user.getId(), stoneId, commentRequest);
        return APIBody.of(HttpStatus.OK.value(), "방명록 작성 성공", savedComment);
    }

    @PatchMapping("/comments/{commentId}/like")
    public APIBody<Boolean> toggleCommentLike(
            @CurrentUser SessionUser user,
            @PathVariable UUID commentId) {
        Boolean isLike = commentService.toggleCommentLike(user.getId(), commentId);
        return APIBody.of(HttpStatus.OK.value(), "좋아요 상태 변경 성공", isLike);
    }
}

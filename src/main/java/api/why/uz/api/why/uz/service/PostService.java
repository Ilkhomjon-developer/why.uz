package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.dto.post.PostCreateDTO;
import api.why.uz.api.why.uz.dto.post.PostDTO;
import api.why.uz.api.why.uz.dto.post.PostFilterDTO;
import api.why.uz.api.why.uz.dto.post.SimilarPostListDTO;
import api.why.uz.api.why.uz.entity.PostEntity;
import api.why.uz.api.why.uz.enums.ProfileRole;
import api.why.uz.api.why.uz.repository.PostRepository;
import api.why.uz.api.why.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AttachService attachService;

    public PostDTO create(PostCreateDTO dto){
        PostEntity entity = new PostEntity();
        entity.setTitle(dto.title());
        entity.setContent(dto.content());
        entity.setPhotoId(dto.photo().id());
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        postRepository.save(entity);

        return getPostDTO(entity);
    }
    public PageImpl<PostDTO> getProfilePostList(int page, int size){
        PageRequest request = PageRequest.of(page, size);
        Page<PostEntity> entityList = postRepository.findAllByProfileId(SpringSecurityUtil.getCurrentUserId(), request);
        List<PostDTO> dtoList = entityList.getContent().stream().map(this::getPostDTO).toList();
        return new PageImpl<>(dtoList, request, entityList.getTotalElements());
    }

    public PostDTO getById(String id) {
        return getPostDTO(postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found")));
    }
    public PostDTO updatePost(String id, PostCreateDTO dto) {
        PostEntity entity = getInfoById(id);
        if(!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new RuntimeException("You can't edit this post");
        }
        String deletePhotoId = null;
        if(!dto.photo().id().equals(entity.getPhotoId())){
            deletePhotoId = entity.getPhotoId();
        }
        entity.setTitle(dto.title());
        entity.setContent(dto.content());
        entity.setPhotoId(dto.photo().id());
        postRepository.save(entity);
        if(deletePhotoId != null){
            attachService.deletePhotoById(deletePhotoId);
        }
        return getPostDTO(entity);
    }

    private PostEntity getInfoById(String id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }
    private PostDTO getPostDTO(PostEntity entity) {
        return new PostDTO(entity.getId(), entity.getTitle(), attachService.attachDto(entity.getPhotoId()));
    }

    public String deletePost(String id) {
        PostEntity entity = getInfoById(id);
        if (!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new RuntimeException("You can't delete this post");
        }
        attachService.updateVisible(entity.getPhotoId());
        postRepository.updateVisible(id);
        return "Successfully deleted";
    }
    public PageImpl<PostDTO> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostEntity> entityList = postRepository.getAllPosts(pageRequest);
        List<PostDTO> dtoList = entityList.getContent().stream().map(this::getPostDTO).toList();
        return new PageImpl<>(dtoList, pageRequest, entityList.getTotalElements());
    }

    public List<PostDTO> getSimilarPostList(SimilarPostListDTO dto) {
        List<PostEntity> entityList = postRepository.findNPosts(dto.exceptId());
        return entityList.stream().map(this::getPostDTO).toList();
    }
    public PageImpl<PostDTO> searchPosts(int page, int size, PostFilterDTO dto) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostEntity> entityList = postRepository.searchPost(pageRequest, dto.str());
        List<PostDTO> dtoList = entityList.getContent().stream().map(this::getPostDTO).toList();
        return new PageImpl<>(dtoList, pageRequest, entityList.getTotalElements());
    }
}
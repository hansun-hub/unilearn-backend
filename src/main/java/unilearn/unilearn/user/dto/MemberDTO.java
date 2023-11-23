package unilearn.unilearn.user.dto;

public class MemberDTO {
    private String nickname;
    private Long stdListId;

    public MemberDTO(String nickname, Long stdListId){
        this.nickname = nickname;
        this.stdListId = stdListId;
    }
}

package com.example.checkcheck.util;


import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.NotificationRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class ComfortUtils {

    private Time time;

    public ComfortUtils(Time time) {
        this.time = time;
    }

    //TODO: 수정필요
    public String getTime(LocalDateTime cratedAt) {
        long now = ChronoUnit.MINUTES.between(cratedAt , LocalDateTime.now());

        return time.times(now);
    }

    public String getUserRank(int point) {

        if (point >= 0 && point < 101) {
            return "B";
        } else if (point >= 101 && point < 201) {
            return "S";
        } else if (point >= 201 && point < 501) {
            return "G";
        } else if (point >= 501 && point < 1001) {
            return "P";
        } else
            return "D";
    }

    public String getCategoryKorean(Category category) {
        if (category.equals(Category.clothes)) {
            return "의류/잡화";
        } else if (category.equals(Category.digital)) {
            return "디지털/생활가전";
        } else if (category.equals(Category.sports)) {
            return "스포츠/레저";
        } else if (category.equals(Category.interior)) {
            return "가구/인테리어";
        } else if (category.equals(Category.hobby)) {
            return "도서/여행/취미";
        } else if (category.equals(Category.food)) {
            return "식품";
        } else if (category.equals(Category.pet)) {
            return "반려동물, 식물";
        } else
            return "기타";
    }

    public String getProcessKorean(Process process) {
        if (process.equals(Process.process)) {
            return "진행중";
        } else if (process.equals(Process.done)) {
            return "채택 성공";
        } else
            return String.valueOf(process);
    }

//    public String getUserNickName() {
//        return String.valueOf((int)(Math.random()*Math.pow(10,6)));
//    }

    public String makeUserNickName() {
        StringBuilder sb = new StringBuilder();
        List<String> firstName = Arrays.asList("설운","최상의","답답한","좋은","영리한","구석진","아닌","큼직한","있는","짓궂은","푸른","반가운","된","잘난","짧은","둥근","졸린","깊은","그리운","긴","화려한","굳은","기쁜",
                "먼","고픈","뒤늦은","우스운","조그만","낮은","새로운","괜찮은","같은","얕은","뽀얀","아픈","딱한","뼈저린","어린","푸근한","고마운","취한","젊은","큰","무딘","익은","짜증난","당황한","누런","흐린","굵은","가는",
                "얇은","놀라운","당당한","빨간","뻘건","서툰","고른","한가한","소심한","그른","동그란","어색한","넓은","이른","시원한","짙은","솔직한","친숙한","밤늦은","둥그런","엉뚱한","귀여운","무거운","엄청난","친절한","섣부른",
                "우월한","얄미운","건조한","붉은","민망한","좁은","보람찬","멋쩍은","화가난","깜찍이","멋진","나쁜","무른","밝은","여문","검은","메마른","쓰린","거센","씁쓸한","개운한","산뜻한","달콤한","즐거운","바쁜","고달픈","깜찍한",
                "억울한","깨끗한","날랜","그런","초조한","아쉬운","쓴","싼","즐기는","다른","줄기찬","외적인","빠른","수줍은","노란","쾌적한","많은","곧은","않은","비싼","느린","어려운","성가신","무모한","매운","적은","조용한","늦은","예쁜",
                "외로운","턱없는","희망찬","너른","행복한","진지한","흰","상냥한","부끄런","다정한","옳은","질긴","약은","점잖은","딱딱한","유일한","고독한","잘생긴","위험한","힘찬","지나친","알맞은","유명한","없는","무서운","힘겨운","가엾은",
                "애매한","네모난","약빠른","쓰디쓴","언짢은","서운한","뜨거운","가냘픈","미운","싫은","애틋한","재밌는","뿌연","탐스런","단호한","나은","고운","슬픈","건강한","지겨운","돈","드문","안된","최고의","눅은","차가운","쉬운","말많은",
                "작은","짠","든든한","명랑한","어두운","게으른","기다란","열띤","메스꺼운","파란","부른","올바른","더운","높은","거친","잘빠진","내적인","똑똑한");
        List<String> middleName = Arrays.asList("꿩" ,"닭" ,"참새" ,"생쥐" ,"쥐" ,"곰" ,"불곰" ,"까치" ,"뱀" ,"용" ,"소" ,"말" ,"양" ,"학" ,"매" ,"삵" ,"개" ,"제비" ,"토끼" ,"사람" ,"돼지" ,"박쥐" ,"타조" ,"거위" ,
                "오리" ,"들소" ,"사자" ,"수달" ,"가젤" ,"사슴" ,"퓨마" ,"고래" ,"늑대" ,"노루" ,"산양" ,"여우" ,"표범" ,"치타" ,"도요" ,"백로" ,"저새" ,"고니" ,"황새" ,"뇌조" ,"앵무" ,"곰쥐" ,"꿀새" ,"거북" ,"고래" ,"팽귄" ,
                "상어" ,"재칼" ,"악어" ,"하마" ,"물소" ,"염소" ,"영양" ,"담비" ,"물개" ,"물범" ,"솔개" ,"밍크" ,"기린" ,"들개" ,"팬더" ,"물곰" ,"비버","" ,"갈매기" ,"개개비" ,"거위"  ,"게사니" ,"고니" ,"공작" ,"구관조" ,
                "귀촉도" ,"극락조" ,"금계" ,"기러기" ,"까마귀" ,"까치" ,"까투리" ,"꺼병이" ,"꾀꼬리" ,"꿩" ,"닭" ,"도요새" ,"독수리" ,"동고비" ,"두견" ,"두견새" ,"두루미" ,"따오기" ,"때까치" ,"레아","매" ,"멋쟁이" ,"멧도요" ,
                "문조" ,"물수리" ,"물총새" ,"박새" ,"반구" ,"반시" ,"백로" ,"백조" ,"벌새" ,"병아리" ,"부엉이" ,"비둘기" ,"산비취" ,"삼광조" ,"새" ,"솔개" ,"송골매" ,"수꿩" ,"부엉이" ,"수리" ,"수탉" ,"신천옹" ,"십자매" ,"아비" ,"할미새" ,
                "암꿩" ,"암탉" ,"앵무" ,"앵무새" ,"야금" ,"어치","올빼미" ,"원앙" ,"장끼" ,"제비" ,"종다리" ,"직박" ,"구리" ,"집오리" ,"레기" ,"찌르" ,"참새" ,"철새" ,"콩새" ,"크낙새" ,"클락새" ,"키위" ,"타조" ,"파랑새" ,"팔색조" ,"펭귄" ,"풍조" ,"학" ,"홍학");
        List<String> lastName = Arrays.asList("군", "양", "씨", "님");
        Collections.shuffle(firstName);
        Collections.shuffle(middleName);
        Collections.shuffle(lastName);
        sb.append(firstName.get(0)).append(middleName.get(0)).append(lastName.get(0));
        return sb.toString();
    }

}

// package com.example.checkcheck.repository;

// import com.example.checkcheck.model.Member;
// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.io.IOException;
// import java.time.LocalDateTime;
// import java.util.Optional;

// import static org.assertj.core.api.Assertions.*;


// @SpringBootTest
// class MemberRepositoryTest {

//     @Autowired
//     MemberRepository memberRepository;


//     @Test
//     void 회원가입() {
//         Member member1 = Member.builder()
//                 .userName("유저1")
//                 .nickName("유저1")
//                 .createdAt(LocalDateTime.now())
//                 .isDeleted(false)
//                 .isAccepted(false)
//                 .password("1234")
//                 .provider("naver")
//                 .userEmail("n_test1@naver.com")
//                 .build();
//         memberRepository.save(member1);
//         Optional<Member> testMember = memberRepository.findByUserEmail("n_test1@naver.com");
//         System.out.println(testMember);

//         assertThat(testMember.get().getNickName()).isEqualTo("유저1");

//     }

// }

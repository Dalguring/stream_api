package test;

import main.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


public class StreamTest {
    private static List<User> users;

    @BeforeAll
    public static void fillUser() {
        users = List.of(
                new User(1L, "Seo", "SeongMin", "Daljee", "blussm@kakao.com", 263, List.of("로웬", "볼다이크")),
                new User(2L, "Cho", "MinJi", "Elforming", "forming@google.com", 223, List.of("엘가시아", "볼다이크", "쿠르잔")),
                new User(3L, "Kim", "DeokBae", "Duckduck", "duckship@naver.com", 182, List.of("로웬", "엘가시아")),
                new User(4L, "Park", "", "0_hee", "zzim@google.com", 53, List.of("루테란", "애니츠")),
                new User(5L, "Seo", "SeongHo", "blussh", "seongho@kakao.com", 263, List.of("파푸니카", "볼다이크")),
                new User(6L, "Cha", "EunJoo", "eunzoo", "eunzoo@naver.com", 1, List.of("루테란", "베른북부")),
                new User(7L, "Gweon", "DeokHo", "thecoya", "thecoya@daum.net", 78, List.of("애니츠", "베른남부"))
        );
    }

    /**
     * stream().filter() => Predicate에 의해 지정된 Test를 통과하는 요소를 가진 스트림을 새로 생성
     */
    @Test
    void filter_users_with_mail_and_level() {
        List<User> filterUsers =
                users.stream().filter(user -> hasGoogleAccount(user)) // == this::hasGoogleAccount
                        .filter(user -> levelGreaterThan150(user))    // == this::levelGreaterThan150
                        .toList();

        int expected = 1;
        assertThat(filterUsers.size()).isEqualTo(expected);
    }

    private boolean hasGoogleAccount(User user) {
        return user.getEmail().endsWith("google.com");
    }

    private boolean levelGreaterThan150(User user) {
        return user.getLevel() > 150;
    }

    /**
     * stream().map() => 원본 스트림의 각 요소에 함수를 적용하고 스트림을 새로 생성
     */
    @Test
    void get_users_whose_name_is_not_fullname() {
        List<String> notFullNames =
                users.stream()
                        .map(user -> createFullName(user).get())
                        .filter(this::isFullName)
                        .toList();

        String expected = users.get(3).getFirstName().concat(" ");
        assertThat(notFullNames).contains(expected);
    }

    private Optional<String> createFullName(User user) {
        return Optional.ofNullable("%s %s".formatted(user.getFirstName(), user.getLastName()));
    }

    private boolean isFullName(String username) {
        return username.split(" ").length != 2;
    }

    /**
     * stream().findFirst() => 스트림의 첫 번째 항목에 대해 Optional을 반환
     */
    @Test
    void first_person_whose_journeyBook_is_엘가시아() {
        Optional<User> user = users.stream()
                .filter(this::getJourneyBookElgacia)
                .findFirst();

        // 출력부
        users.stream()
                .filter(this::getJourneyBookElgacia)
                .toList()
                .forEach(i -> System.out.printf("%s %s%n", i.getFirstName(), i.getLastName()));

        Long expected = 2L;
        assertThat(user.isPresent() ? user.get().getId() : "그런사람 또 없습니다").isEqualTo(expected);
    }

    private boolean getJourneyBookElgacia(User user) {
        return user.getJourneyBook().contains("엘가시아");
    }

    /**
     * stream().flatMap() => 추가적인 작업을 단순화 하기 위해 데이터 구조를 평탄화 시킴(다차원의 stream을 1차원화)
     */
    @Test
    void count_journeyBook_볼다이크() {
        long expected =
                users.stream()
                        .map(User::getJourneyBook)
                        .flatMap(Collection::stream)
                        .filter(journeyBook -> journeyBook.equals("볼다이크"))
                        .count();

        // flatMap() 적용 시점의 출력
        System.out.println(users.stream()
                .map(User::getJourneyBook)
                .flatMap(Collection::stream).toList());
    }

    @Test
    void collect_unique_ids() {
        Set<Long> uniqueIds =
                users.stream()
                        .map(User::getId)
                        .collect(Collectors.toSet());

        assertThat(uniqueIds).isEqualTo(Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
    }
}

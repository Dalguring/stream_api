package test;

import main.Employee;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeStreamTest {
    private static List<Employee> empList = new ArrayList<>();

    @BeforeAll
    public static void fillEmployees() {
        empList.add(new Employee(1, "곽마권", 28, 123, "F", "HR", "부산", 2020));
        empList.add(new Employee(2, "김덕배", 29, 120, "F", "HR", "안산", 2015));
        empList.add(new Employee(3, "곽팔용", 30, 115, "M", "HR", "서울", 2014));
        empList.add(new Employee(4, "곽두팔", 32, 125, "F", "HR", "인천", 2013));

        empList.add(new Employee(5, "조민지", 22, 150, "F", "IT", "수원", 2013));
        empList.add(new Employee(6, "김필두", 27, 140, "M", "IT", "광주", 2017));
        empList.add(new Employee(7, "서성민", 26, 130, "F", "IT", "부산", 2016));
        empList.add(new Employee(8, "길원춘", 23, 145, "M", "IT", "서울", 2015));
        empList.add(new Employee(9, "배팔식", 25, 160, "M", "IT", "대전", 2010));
    }

    @AfterEach
    public void print() {
        System.out.println("=====================================");
    }

    /*
     * < 참고 : https://velog.io/@jyleedev/groupingby-mapping >
     *
     * stream().collect() => 데이터 중간 처리 후 마지막에 원하는 형태로 변환해주는 역할
     * - stream 요소들을 List, Set, Map 자료형으로 변환
     * - stream 요소들의 결합(joining)
     * - stream 요소들의 통계(최대, 최소, 평균값 등)
     * - stream 요소들의 그룹화와 분할
     *
     * Collectors.toList() => 모든 Stream의 요소를 List 인스턴스로 수집하는데 사용할 수 있음.
     *
     * Collectors.groupingBy()
     * -> Collectors의 groupingBy() 또는 groupingByConcurrent() 가 리턴하는 Collector을 매개값으로 대입하여 사용 가능
     *    * groupingBy() : Map 객체 리턴, groupingByConcurrent() : ConcurrentMap 객체 리턴
     *
     * 1) groupingBy(Function<T, K> classifier) / T를 K로 매핑한 후, 키가 K이면서 T를 저장하는 요소를 값으로 갖는 Map 생성
     *    ex) groupingBy(Employee::getCity) -> T = Employee, K = Employee.getCity() 이므로 Map<Employee.getCity(K), List<Employee(T)>> 가 생성됨.
     * 2) groupingBy(Function<T, K> classifier, Collector<T, A, D> downstream) / T를 K로 매핑한 후, 키가 K이면서 키에 저장된 D객체에 T를 누적한 Map 생성
     *    ex) groupingBy(Employee::getCity, Collectors.mapping(Employee::getName, Collectors.toList())
     */
    @Test
    @DisplayName("도시 별 사원")
    void group_employees_by_city() {
        Map<String, List<Employee>> empByCity;

        empByCity = empList.stream()
                .collect(Collectors.groupingBy(Employee::getCity));
        System.out.println(empByCity);
    }

    @Test
    @DisplayName("특정 도시 출신의 사원")
    void group_employees_by_city_and_get_employee_name() {
        Map<String, List<String>> empName;

        empName = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getCity,
                        Collectors.mapping(Employee::getName, Collectors.toList())));

        empName.get("부산").forEach(System.out::println);
    }

    @Test
    @DisplayName("나이별 사원")
    void group_employees_by_age() {
        Map<Integer, List<Employee>> empByAge;

        empByAge = empList.stream()
                .collect(Collectors.groupingBy(Employee::getAge));

        System.out.println(empByAge);
    }

    @Test
    @DisplayName("특정 성별을 가진 사원 수")
    void group_employees_by_sex_and_count_employees() {
        Map<String, Long> empCountBySex;

        empCountBySex = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getGender,
                        Collectors.counting()));

        System.out.println(empCountBySex);
    }
}

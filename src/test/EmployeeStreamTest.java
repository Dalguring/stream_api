package test;

import main.Employee;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stream API 실습
 * < 참고 : https://medium.com/@veenaraofr/java8-stream-api-commonly-asked-questions-about-employee-highest-salary-99c21cec4d98 >
 */
public class EmployeeStreamTest {
    private static final List<Employee> empList = new ArrayList<>();

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
     * Collections와 Collectors의 차이 ?
     * -> Collections 는 java.util 패키지에 속하는 클래스로 컬렉션 프레임워크(List, Map, Set) 등 컬렉션 객체들을 다루기 위한 정적 메서드를 제공함
     * -> Collectors는 java.util.stream 패키지에 속하는 클래스로 스트림 API를 사용하여 컬렉션을 변환, 요약, 그룹화 하는 등의 종단 연산을 처리할 수 있는 메서드를 제공함.
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

    @Test
    @DisplayName("조직 내 모든 부서 명")
    void all_departments_name_in_organization() {
        empList.stream()
                .map(Employee::getDeptName)
                .distinct()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("특정 나이 이상 사원 정보")
    void certain_age_employee_details() {
        int age = 28;

        empList.stream()
                .filter(emp -> emp.getAge() >= age)
                .toList()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("가장 나이가 많은 사원의 나이")
    void maximum_age_of_employee() {
        OptionalInt max = empList.stream()
                .mapToInt(Employee::getAge)
                .max();

        if (max.isPresent()) {
            System.out.println(max.getAsInt());
        }
    }

    @Test
    @DisplayName("성별에 따른 평균 나이")
    void average_age_grouped_by_gender() {
        Map<String, Double> avgAge;

        avgAge = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getGender,
                        Collectors.averagingInt(Employee::getAge)
                ));

        System.out.println(avgAge);
    }

    @Test
    @DisplayName("부서별 사원 수")
    void number_of_employees_in_each_department() {
        Map<String, Long> countByDept;

        countByDept = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDeptName
                        , Collectors.counting()
                ));

        System.out.println(countByDept);
    }

    @Test
    @DisplayName("가장 나이가 많은 사원의 사원 정보")
    void oldest_employee_details() {
        Optional<Employee> oldestEmp;

        oldestEmp = empList.stream()
                .max(Comparator.comparingInt(Employee::getAge));

        System.out.println(oldestEmp.get());
    }

    @Test
    @DisplayName("가장 어린 여자 사원 정보")
    void youngest_femal_employee() {
        Optional<Employee> youngestEmp;
        youngestEmp = empList.stream()
                .filter(e -> e.getGender().equals("F"))
                .min(Comparator.comparingInt(Employee::getAge));

        System.out.println(youngestEmp);
    }

    /**
     * Collectors.partitioningBy() => 스트림의 요소를 지정한 조건에 일치하는 그룹과 일치하지 않는 그룹, 두 가지로 분할
     * 매개변수 : Predicate, 리턴 : Map<Boolean, List<E>>
     */
    @Test
    @DisplayName("30세 이상의 직원과 30세 이하의 직원")
    void employee_whose_age_greater_than_30_and_less_than_30() {
        Map<Boolean, List<Employee>> partitionEmployeesByAge;

        partitionEmployeesByAge = empList.stream()
                .collect(Collectors.partitioningBy(e -> e.getAge() > 30));

        for (Map.Entry<Boolean, List<Employee>> entry : partitionEmployeesByAge.entrySet()) {
            if (entry.getKey()) {
                System.out.println("30세보다 나이가 많은 직원");
                System.out.println(entry.getValue());
            } else {
                System.out.println("30세보다 나이가 작거나 같은 직원");
                System.out.println(entry.getValue());
            }
        }
    }

    @Test
    @DisplayName("가장 사원이 많은 부서의 부서명")
    void max_number_of_employees_present_in_department() {
        Map.Entry<String, Long> maxNoOfEmployeesInDept;

        maxNoOfEmployeesInDept = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDeptName,
                        Collectors.counting()
                ))// return Type = Map<String, Long>()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        System.out.println(maxNoOfEmployeesInDept.getKey());
    }

    @Test
    @DisplayName("HR부서 내 사원 유무")
    void any_employee_in_HR_dept() {
        Optional<Employee> employee = empList.stream()
                .filter(e -> e.getDeptName().equalsIgnoreCase("HR")) // equalsIgnoreCase : 대소문자 구분 x, equals : 대소문자 구분 o
                .findAny();

        employee.ifPresent(System.out::println);
    }

    @Test
    @DisplayName("사원 수가 3명 이상인 부서명")
    void dept_names_where_employees_over_3() {
        empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDeptName
                        , Collectors.counting()
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() >= 3)
                .forEach(dept -> System.out.println(dept.getKey()));
    }

    @Test
    @DisplayName("서로 다른 부서명")
    void distinct_dept_names() {
        empList.stream()
                .map(Employee::getDeptName)
                .distinct()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("부산에 사는 사원의 정보")
    void employees_who_lives_in_Busan() {
        empList.stream()
                .filter(e -> e.getCity().equalsIgnoreCase("부산"))
                .sorted(Comparator.comparing(Employee::getName))
                .forEach(e -> System.out.println(e.getName()));
    }

    @Test
    @DisplayName("사내 사원의 수")
    void no_of_employee_in_the_dept() {
        long cnt = empList.stream()
                .count();

        System.out.println(cnt);
    }

    @Test
    @DisplayName("이름과 나이순으로 사원 정보 정렬")
    void sorting_based_on_name_and_age() {
        empList.stream()
                .sorted(Comparator.comparing(Employee::getName)
                        .thenComparing(Employee::getAge))
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("경력이 가장 오래된 사원")
    void highest_experienced_employee() {
        Optional<Employee> employee;
        employee = empList.stream()
                .sorted(Comparator.comparingInt(Employee::getYearOfJoining))
                //.reversed()) => 가장 경력이 짧은 사원
                .findFirst();

        System.out.println(employee);
    }

    @Test
    @DisplayName("평균 연봉과 총 연봉")
    void average_and_total_salary() {
        DoubleSummaryStatistics empSal = empList.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));

        System.out.println(empSal);
        System.out.println(empSal.getAverage());
        System.out.println(empSal.getSum());
    }

    @Test
    @DisplayName("부서별 평균 연봉")
    void average_salary_of_each_department() {
        empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDeptName
                        , Collectors.summarizingDouble(Employee::getSalary)))
                .forEach((key, value) -> System.out.println(key + " : " + value.getAverage()));
    }

    @Test
    @DisplayName("최고 연봉자")
    void highest_salary_in_organization() {
        Optional<Employee> emp = empList.stream()
                .max(Comparator.comparingLong(Employee::getSalary));

        emp.ifPresent(employee -> System.out.println(employee.getSalary()));

        emp = empList.stream()
                .sorted(Comparator.comparingLong(Employee::getSalary).reversed())
                .findFirst();

        System.out.println(emp.get().getSalary());
    }

    @Test
    @DisplayName("조직 내 두 번째로 연봉이 많은 사람")
    void second_highest_salary_in_organization() {
        Optional<Employee> emp2 = empList.stream()
                .sorted(Comparator.comparingLong(Employee::getSalary).reversed())
                .skip(1)
                .findFirst();

        emp2.ifPresent(System.out::println);
    }

    @Test
    @DisplayName("성별별 최고 연봉자")
    void highest_salary_based_on_gender() {
        Map<String, Optional<Employee>> highestPaidMFEmployee = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getGender
                        , Collectors.maxBy((t1, t2) -> (int) (t1.getSalary() - t2.getSalary()))));

        System.out.println(highestPaidMFEmployee);
    }

    @Test
    @DisplayName("부서별 최저 연봉자")
    void lowest_salary_in_each_depart() {
        empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDeptName
                        , Collectors.minBy((t1, t2) -> (int) (t1.getSalary() - t2.getSalary()))
                ))
                .entrySet()
                .stream()
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));

    }

    @Test
    @DisplayName("사내 연봉 오름차순, 내림차순")
    void employees_salary_sorted_by_ascending() {
        System.out.println("오름차순");
        empList.stream()
                .sorted(Comparator.comparingLong(Employee::getSalary))
                .forEach(System.out::println);

        System.out.println("내림차순");
        empList.stream()
                .sorted(Comparator.comparingLong(Employee::getSalary).reversed())
                .forEach(System.out::println);

    }

    @Test
    @DisplayName("부서별 두 번째 최고 연봉자(collectingAndThen)")
    void second_highest_salary_employee() {
        Map<String, Optional<Employee>> emp = empList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDeptName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingLong(Employee::getSalary).reversed())
                                        .skip(1)
                                        .findFirst()
                        )
                ));

        System.out.println(emp);
    }
}

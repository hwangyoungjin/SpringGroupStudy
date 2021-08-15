 # [Spring-boot Study](https://github.com/sieunkr/spring-study-group)
---

### 목차
1. 1주차 : [간단한 API서버 만들기](https://github.com/hwangyoungjin/SpringGroupStudy#1주차)
2. 2주차 : [스프링부트 AutoConfiguration](https://github.com/hwangyoungjin/SpringGroupStudy#2주차)
3. 3주차 : [예외 처리, 스프링 부트 테스트 코드 작성](https://github.com/hwangyoungjin/SpringGroupStudy#3주차)
4. 4주차 : [AOP와 스프링캐시 추상화, Redis 연동](https://github.com/hwangyoungjin/SpringGroupStudy#4주차)

## 1주차
### [간단한 API서버 만들기](https://brunch.co.kr/@springboot/531)
---
### 1. Annotation 정리 
```java
  * @ComponentScan : Bean 설정을 위해 스캔할 범위를 지정
  * @Component : 개발자가 직접 작성한 class를 스프링 IoC Container에 Bean으로 등록하기 위한 어노테이션 
    - @Repository : DB나 file같은 외부 I/O를 처리하는 클래스에 사용하여 Bean으로 등록
    - @Service : 웹 내부에서 서비스를 처리하는 클래스에 사용하여 Bean으로 등록
    - @Controller : 웹요청과 응답을 처리하는 클래스에 사용하여 Bean으로 등록
    - @Configuration : 스프링 IoC Container에게 해당 클래스가 Bean구성 class임을 알려주는 어노테이션
  * @RequiredArgsConstructor 어노테이션은 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다. 주로 의존성 주입(Dependency Injection) 편의성을 위해서 사용되곤 합니다.
  
  * 외부설정
    - @ConfigurationProperties : 자바 Bean 스펙을 따라서 *.properties , *.yml 파일에 있는 값을 해당 어노테이션 붙은 클래스에 Getter, Setter를 이용하여 필드값의 바인딩 해준다.
	1. 사용하기 위해선 configuration-processor 의존성 추가필요
	2. @Configuration과 같이 사용
	3. prefix="name" 사용시 *.properties에서 해당"name"으로 시작하는 값만 클래스 field에 바인딩 된다.

  * Spring web
    - @RequsetBody :  요청본문에 들어있는 데이터를 HttpMessageConverter를 통해 자바 객체로 받아올 때 사용
	1. @Valid or @Validated를 사용하여 값 검증 가능
	2. BindingResult 아규먼트를 사용해 코드로 바인딩 또는 검증 에러를 확인 가능

    - @ResponseBody : 자바 객체의 데이터를 HttpMessageConverter를 사용해 응답 본문 메시지로 변환하여 보낼 때 사용

    - @RestController : @Controller 어노테이션과 @ResponseBody 어노테이션을 합쳐놓은 어노테이션 
	1. 자동으로 모든 핸들러 메소드에 @ResponseBody 적용하여 메소드마다 선언하지 않아도 된다.
	2. @Controller는 viewPage를 반환하지만 @RestController는 객체를 반환하면 변환되어 바로 응답으로

    - @RequestMapping : 스프링 웹 MVC에서 HttpMethod 맵핑하는 Annotation
	1. class,method Level에서 사용
	2. class에 선언되어 있다면 선언한  url 패턴뒤에 이어 붙여서 맵핑
	3. 특정 HttpMethod 설정
	  Ex> @RequestMapping(method = RequestMethod.GET)
	3. 특정한 타입의 데이터 담고 있는 요청만 처리하는 핸들러의 경우
	  Ex> @RequestMapping(consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	4. 특정한 타입의 응답을 만드는 핸들러의 경우
	  Ex> @RequesMapping(produces="application/json")
	5. 특정한 헤더가 있는 요청을 처리하고 싶은 경우
	  Ex> @RequesMapping(headers="key")
	6. 특정한 요청 매개변수 키를 가지고 있는 요청을 처리하고 싶은 경우
	  Ex> @RequesMapping(params="a")
	 
    - @GetMapping : @RequestMapping과 동일 하지만 GET요청만 맵핑한다.
	- 이외 에도 @PostMapping, @PutMapping, @DeleteMapping 등등 있다.

  * Lombok : 반복적으로 만드는 코드를 어노테이션을 통해 줄여 주는 라이브러리
    - @Getter & @Setter : 필드에 접근자와 설정자를 자동으로 생성해준다.
	1. 클래스 레벨에 설정하는 경우 모든 필드에 접근자와 설정자가 자동으로 생성
 
    - @Builder : Class에 대한 복잡한 Builder API들을 자동으로 생성
	1. 클래스, 생성자, 메서드 레벨에서 사용가능
	2. 빌더패턴 : 객체 생성할때 사용되는 패턴 - 참고 : https://jh-7.tistory.com/3
 
    - @AllArgsConstructor : 모든 필드 값을 파라미터로 받는 생성자를 만들어준다.
    - @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성해준다.
    - @Data : @Getter, @Setter, @RequireArgsConstuctor, @ToString, @EqualsAndHashCode를 한꺼번에 설정해준다.
```
### 2. DI (Dependenct Injection) : '의존성 주입'에 대해서[Ioc & DI의 개념 & Bean생성방법](https://brunch.co.kr/@springboot/532)
```java 
* IoC (Inversion of Control) : DI라고도 하며 어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는게 아니라 주입 받아 사용하는 방법을 말한다.
* IoC Container : 애플리케이션 컴포넌트의 중앙 저장소로 빈 설정 소스로 부터 빈 정의를 읽어들이고, 빈을 구성하고 제공한다.
	
* DI 장점
  1. 의존성 주입으로 인해 모듈 간의 결합도가 낮아지고 유연성이 높아진다.
  2. 재사용성이 증가한다.
  3. 더 많은 테스트 코드 만들 수 있다.
  4. 코드 읽기가 쉬워진다.
```
<img src="https://user-images.githubusercontent.com/60174144/104797462-8ab10280-5801-11eb-90a0-8ff3a5988eec.png" width="70%" height="70%">

```java
* 질문사항
1. ResponseMovie없이 Movie객체로 응답 받을 수 는 없는건지?
2. 아래 Logic이 맞는지
```
```java
* 환경
- 2.3.7 RELEASE
- Lombok의존성 추가 
- gradle
```

1. ### **[네이버 오픈 API 활용하기](https://developers.naver.com/docs/common/openapiguide/apilist.md#%EA%B2%80%EC%83%89)**
	- 사용 api : **검색**
	- #### **API 서버의 시스템 구성도**
	- <img src="https://user-images.githubusercontent.com/60174144/104416698-927d6680-55b7-11eb-870b-7d8be4e94696.png" width="70%" height="70%">


2. ### **API 서버 구축하기**
	```java
	[ directory 구조 ]
	* config
	  - HttpClientConfig
	  - NaverProperies
	* model
	  - Movie
	  - MovieGroup
	  - ReponseMovie
	     - Item
	* Service
	  - MovieService
	* repository
	  - MovieRepository
	* repositoryImpl
	  - MovieRepositoryImpl
	* controller
	  - SearchController
	``` 

	1. #### **restTemplate 사용**
		```java
		* 다른 서버의 API endpoint를 호출할 때 RestTemplate을 많이 쓴다.
		```
		- RestClicent 사용하기 위해 의존성 추가
		```java
		implementation 'org.apache.httpcomponents:httpclient:4.5'
		```
		- RestTemplate @Bean 설정
		```java
		@Configuration
		public class HttpClientConfig {
		    @Bean
		    public RestTemplate restTemplate(){
		        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		        HttpClient httpClient = HttpClientBuilder.create()
		                .setMaxConnTotal(50)
		                .setMaxConnPerRoute(10)
		                .build();
		
		        factory.setHttpClient(httpClient);
		        factory.setConnectTimeout(3000);
		        factory.setReadTimeout(5000);
		        return new RestTemplate(factory);
		    }
		}
		```

	2. #### **Active Profiles**
		- Naver OpenAPI를 호출하기 위해 필요한 정보
		- resource 안 application-sercre.properties에 추가 (**git.ignore**)
		- 기존에 있던 application.properties에 추가[참고](http://honeymon.io/tech/2021/01/16/spring-boot-config-data-migration.html)
		```properties
		#profile
		spring.profiles.include=secret
		```
	3. #### **Config 설정**
		- naver api를 사용하기 위하여 prefix를 naver.openapi로 설정
		```java
		@Getter
		@Setter
		@Configuration
		@ConfigurationProperties(prefix = "naver.openapi") 
		//*.properties에서 앞에 naver.openapi로 시작하는 값만 가져와서 바인딩
		public class NaverProperties {
		    private String movieUrl; //application.properties에 있는 value에 해당
		    private String clientId; //application-secret.properties에 있는 value에 해당
		    private String clientSecret; //application-secret.properties에 있는 value에 해당
		}
		```

	4. #### **영화 검색 서비스 구현하기**
		- 네이버 오픈 api의 결과를 받아 바인딩할 객체 정의
		```java
		@Getter
		@Setter
		@AllArgsConstructor
		@NoArgsConstructor
		public class ResponseMovie {
		    private List<Item> items;
		
		    @Getter
		    @Setter
		    @AllArgsConstructor
		    @NoArgsConstructor			
		    public static class Item{
		        private String title;
		        private String link;
		        private String actor;
		        private String director;
		        private float userRating;
		        //TODO: 필드추가
		    }
		}

		*Lombok Annotation 인텔리제이에서 자동추가 안되는 문제
		-> 직접 import lombok.Annotation; 해줌으로써 해결
		```

		- 직접 사용할 Movie 객체 정의
		```java
		@Builder
		@Getter
		public class Movie implements Serializable {
		    private String title;
		    private String link;
		    private float userRating;
		}
		```

		- MovieRepository 인터페이스 정의
		```java
		@Repository
		public interface MovieRepository {
		    List<Movie> findByQuery(String query);
		}
		```

		- MovieRepository 구현 : MovieRepositoryImpl
		```java

		* never open api를 통해 데이터 가져오는 부분 
		* restTamplate의 경우 map 또는 사용자가 정의한 class 등 다양한 형태로 데이터를 바로 파싱해서 받는다
		* RestTemplate Method :  exchange 
		  - HttpMethod : Any
		  - 설명 : 헤더세팅해서 HttpMethod 요청 보내고 HttpMessageConverter를 통해         
		          인자로 넘긴 Object타입으로 json 데이터 변환해서 ResponseEntitiy로 받는다.
		  - Parameter
		     1. url
		     2. HttpMethod
		     3. HttpEntity (requestEntity)
		     4. Object (Class responseType)

		@Component
		public class MovieRepositoryImpl implements MovieRepository {
		    @Autowired
		    private RestTemplate restTemplate;
		    @Autowired
		    private NaverProperties naverProperties;
		    @Override
		    public List<Movie> findByQuery(String query) {
		        HttpHeaders httpHeaders = new HttpHeaders();
		        httpHeaders.add("X-Naver-Client-Id",naverProperties.getClientId());
		        httpHeaders.add("X-Naver-Client-Secret",naverProperties.getClientSecret());
		
		        String url = naverProperties.getMovieUrl()+"?query=" + query;
		        
		        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), ResponseMovie.class)
		                .getBody() // 응답 본문
		                .getItems()
		                .stream()
		                .map(m->Movie.builder() // Movie객체에 담는다.
		                    .title(m.getTitle())
		                    .link(m.getLink())
		                    .userRating(m.getUserRating())
		                    .build())
		                .collect(Collectors.toList());
		    }
		}
		```		

		- MovieService에서 Repository 통해 가져온 데이터 응용
		```java
		@Service
		public class MovieService {
		
		    @Autowired
		    MovieRepository movieRepository;
		
		    public List<Movie> search(final String query){
		        return movieRepository.findByQuery(query);
		    }
		}
		```

		- SearchController 정의
		```java
		@RestController
		@RequestMapping("/api/v1/search")
		public class SearchController {
		    @Autowired
		    private MovieService movieService;
		
		    @GetMapping("/movies")
		    public List<Movie> getMoviesByQuery (@RequestParam(name = "q") String query){
		         return movieService.search(query);
		    }
		}
		```

		- WebBrower에서 JSON 데이터형식 응답 확인
		- <img src="https://user-images.githubusercontent.com/60174144/104403195-5805d080-559b-11eb-8362-0a34f8b7e4b1.png" width="70%" height="70%">

		- 평점순으로 정렬하기 위해 [NAVER 스펙 확인](https://developers.naver.com/docs/search/movie/)
			- 평점 순 정렬기능 제공X

	5. #### **영화 검색 서비스 평점 순 정렬 기능 추가**
		- MovieGroup model 추가 해서 정렬기능 만들기
		```java
		public class MovieGroup {
		    private final List<Movie> list;
		    public MovieGroup(final List<Movie> list){
		        this.list=list;
		    }
		
		    public List<Movie> getList() {
		        return list;
		    }
		
		    public List<Movie> getListOrderRating(){
		        return list.stream().filter(b->!((Float)b.getUserRating()).equals(0.0f))
		                .sorted((a,b)->b.getUserRating() > a.getUserRating() ? 1:-1)
		                .collect(Collectors.toList());
		    }
		}
		```
		- MovieService 수정
		```java
		public List<Movie> search(final String query){
		  MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
		  return movieGroup.getListOrderRating();
		}
		```
		- <img src="https://user-images.githubusercontent.com/60174144/104404088-386fa780-559d-11eb-9ba3-220deb1ce049.png" width="70%" height="70%">
	
	6. #### **영화 데이터 전부 가져올 수 있도록 필드 추가**
		- Movie, ResponseMovie의 item의 필드 추가
		```java
		@Builder
		@Getter
		public class Movie implements Serializable {
		    private String title;
		    private String link;
		    private float userRating;
		    private String image;
		    private String pubDate;
		    private String actor;
		    private String director;
		    //titile, link, userRating, image, pubDate, actor, director
		}
		****************
		public class ResponseMovie {
		    private List<Item> items;

		    @Getter
		    @Setter
		    @AllArgsConstructor
		    @NoArgsConstructor
		    public static class Item{
		        //titile, link, userRating, image, pubDate, actor, director
		        private String title;
		        private String link;
		        private float userRating;
		        private String image;
		        private String pubDate;
		        private String actor;
		        private String director;
		        //TODO: 필드추가
		    }
		}
		```
		- <img src="https://user-images.githubusercontent.com/60174144/104413722-2fd59c00-55b2-11eb-9144-857fec57f751.png" width="70%" height="70%">

## 2주차
### [스프링부트 AutoConfiguration](https://brunch.co.kr/@springboot/533)
---
1. ### 스프링 AutoConfiguration                                  
	1. #### 스프링부트 @SpringBootApplication
	```java
	@SpringBootApplication에는 3가지 Annotation이 포함되어 있다.
	  - @EnableAutoConfiguration : 스프링부트의 AutoConfiguration을 사용하겠다는 어노테이션
	  - @SpringBootConfiguration : 스프링부트에서 @Configuration을 대체하는 스프링부트 필수 어노테이션
	  - @ComponentScan : 해당 어노테이션 이하 파일에서 등록할 Bean 스캔

	* 참고 Annotation
	1. @Conditional : 스프링IoC컨테이너에 조건부로 Bean등록하는 역할 가능하다.
	  - @ConditionOnClass(*.class) : classpath에 해당(*)클래스가 존재하면 Bean등록 (즉, 의존성으로 들어와 있다면 해당 로직 수행된다.)
	  - @ConditionOnMissingClass(*.class) : 해당(*)클래스가 없다면 Bean등록
	  - @ConditionOnBean(*.class) : 해당 Bean(*)이 존재하면 Bean으로 등록
	  - @ConditionOnMissingBean(*.class) : 해당(*)Bean이 없다면 Bean등록
	```
	2. #### @EnableAutoConfiguration의 역할
	```java
	스프링부트에서 Bean을 읽을 때 2단계로 읽혀진다
	1단계. @ComponentScan ()
	2단계. @EnableAutoConfiguration
	
	* @EableAutoConfiguration은
	External Library인 org.springframework.boot.autoconfigure.EnableAutoConfiguration 의
	spring.factoriese에 있는 항목(class)들을 Scan 후 Bean으로 등록하여 자동설정
	```
	3. #### 자동설정을 application.properties를 통해 커스터마이징하기
	```java
	1. @EableAutoConfiguration이 등록한 자동설정의 Bean을 사용할때
	2. 대부분의 Bean Class는 등록된 properties(class)를 읽어온다 
	3. properties(class)에는 @ConfigurationProperties를 사용하여 prefix가 정해져있다.
	4. 따라서 application.properties를 활용하여 코드에 큰 수정없이 prefix값으로 커스터마이징 가능  
	```
2. ### 과제
	1. #### 임베디드 톰캣의 쓰레드 풀 사이즈 변경
		```java
		server.tomcat.threads.max=200 // application.properties에서 변경
		```
	2. #### naver open API를 통해 쇼핑 검색서비스 추가하기
		- [Naver 쇼핑 API 정보](https://developers.naver.com/docs/search/shopping/)
		- PostMan으로 헤더에 인증값 넣고 naver open api 요청보내서 응답 결과 확인해보기
		- <img src="https://user-images.githubusercontent.com/60174144/105951107-e3f71c80-60b2-11eb-9018-0be15eff6527.png" width="50%" height="50%">
		
		1. ##### application.properties에 json형식의 요청 url 추가
		```java
		naver.openapi.shopUrl=https://openapi.naver.com/v1/search/shop.json
		```

		2. ##### NaverProperties에 shopUrl 추가
		```java
		Getter
		@Setter
		@Configuration
		@ConfigurationProperties(prefix = "naver.openapi")
		public class NaverProperties {
		    private String movieUrl;
		    private String shopUrl;
		    private String clientId;
		    private String clientSecret;
		}
		```

		3. ##### 네이버 오픈 api의 Shop 결과를 받아 바인딩할 ResponseShop 객체 정의
		```java
		@Getter
		@Setter
		@AllArgsConstructor
		@NoArgsConstructor
		public class ResponseShop {
		    private List<Item> items;
		
		    @Getter
		    @Setter
		    @AllArgsConstructor
		    @NoArgsConstructor
		    public static class Item{
		        private String title;
		        private String link;
		        private String image;
		        private Long iprice; //최저가
		        private Long hprice; //최고가
		        private Long productId; //상품 id
		    }
		}
		```

		4. ##### 서버에서 활용할 Shop model 만들기
		```java
		@Builder
		@Getter
		@Setter
		public class Shop {
		    private String title;
		    private String link;
		    private String image;
		    private Long iprice; //최저가
		    private Long hprice; //최고가
		    private Long productId; //상품 id
		}
		``` 

		5. ##### ShopRepository 추가
		```java
		@Repository
		public interface ShopRepository {
		    List<Shop> findByQuery(String query);
		}
		```
		
		6. ##### ShopRepository 구현 : ShopRepositoryImpl
		```java
		@Component
		public class ShopRepositoryImpl implements ShopRepository {
		
		    @Autowired
		    RestTemplate restTemplate;
		
		    @Autowired
		    NaverProperties naverProperties;
		
		    @Override
		    public List<Shop> findByQuery(String query) {
		        HttpHeaders httpHeaders = new HttpHeaders();
		        httpHeaders.add("X-Naver-Client-Id",naverProperties.getClientId());
		        httpHeaders.add("X-Naver-Client-Secret",naverProperties.getClientSecret());
		
		        String url = naverProperties.getShopUrl()+"?query="+query;
		        return restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(httpHeaders), ResponseShop.class)
		                .getBody() //ResponseShop 객체로 요청 결과 (응답) 바인딩됨
		                .getItems()//ResponseShop의 item
		                .stream()
		                .map(m->Shop.builder() //item의 데이터를 Shop 객체로 바인딩하기
		                .title(m.getTitle())
		                .link(m.getLink())
		                .image(m.getImage())
		                .iprice(m.getIprice())
		                .hprice(m.getHprice())
		                .productId(m.getProductId())
		                .build())
		                .collect(Collectors.toList()); //List<Shop> 으로 return
		    }
		}
		```

		7. ##### Repository를 통해 가져온 데이터 활용할 ShopService 구현
		```java
		@Service
		public class ShopService {
		    @Autowired
		    ShopRepository shopRepository;
		    public List<Shop> search(final String query){
		        return shopRepository.findByQuery(query);
		    }
		}
		```
		8. ##### 기존 SearchController에 응답 받을 shop url 추가
		```java
		@GetMapping("/shops")
		public List<Shop> getShopsByQuery(@RequestParam(name = "s") String query){
		    return shopService.search(query);
		}
		```
		
		9. ##### **http://localhost:8080/api/v1/search/shops?s=바지**로 결과 확인
		- <img src="https://user-images.githubusercontent.com/60174144/105951229-0c7f1680-60b3-11eb-88c4-2b2e2e6a7a50.png" width="50%" height="50%">

		10. ##### 최저가로 정렬되어 출력되로록 코드 수정
			```java
			- 최저가 출력 변수 lprice인데 iprice으로 실행되어 null 값 나오고 이었음
			- 해결
			```
			1. Shop model 코드 추가
			```java
			@Builder
			@Getter
			@Setter
			public class Shop implements Comparable<Shop> {
			    private String title;
			    private String link;
			    private String image;
			    private Long lprice; //최저가
			    private Long hprice; //최고가
			    private Long productId; //상품 id
    
			    //기준정렬 만들기 - 최저가 오름차순
			    @Override
			    public int compareTo(Shop o) {
			        return this.lprice > o.lprice ? 1 : -1;
			    }
			```
			2. ShopService 코드 추가
			```java
			    //이름정렬
			    public List<Shop> getOrderName(String query){
			        List<Shop> shops = shopRepository.findByQuery(query);
			        return shops.stream().filter(a->!a.getLprice().equals(0)).sorted().collect(Collectors.toList());
			    }
			```
			3. SearchController 코드 수정
			```java
			    @GetMapping("/shops")
			    public List<Shop> getShopsByQuery(@RequestParam(name = "s") String query){
			        //return shopService.search(query);
			        //이름정렬
			        return shopService.getOrderName(query);
			    }
			```
			4. 결과
			- <img src="https://user-images.githubusercontent.com/60174144/106151206-74bb1e80-61bf-11eb-86a8-50bfea8e3ebd.png" width="50%" height="50%">

3. ### 2주차 PR요청 완료

## 3주차
### [예외 처리, 스프링 부트 테스트 코드 작성하기](https://brunch.co.kr/@springboot/538)
---
1. ### 스프링 테스트 [단위 테스트 vs 통합 테스트]
	0. ### 순서
		- 시작은 test 의존성추가
		```java
		spring-boot-starter-test
		scope는 test
		```
		- Test 코드 순서
		```java
		1. given : 테스트 준비 : 어떤 상황을 준다
		2. when : 테스트 시작 : 어떤 일을 발생시킨다
		3, then : 테스트 단언 : 결과가 맞는지 check (assert 이용)
		```
	1. #### 단위테스트 [아직 완벽히 이해x]
		1. ##### 단위테스트의 5가지 원칙
		```java
		F - Fast ( 테스트 코드를 실행하는 일은 오래 걸리면 안된다.)
		I - Indenpendent ( 독립적으로 실행이 되어야 한다)
		R - Repeatable ( 반복 가능해야 한다)
		S - Self Validating ( 메뉴얼 없이 테스트 코드만 실행해도 성공, 실패 여부를 알 수 있어야 한다.)
		T - Timely ( 바로 사용 가능해야 한다. )
		```
	2. #### 통합테스트
		1. @SpringBootTest
		```java
		- 해당 어노테이션에 의해 ComponentScan이 동작하여 모든 어플리케이션의 Bean을 등록
		- 애플리케이션 실행하는 것과 같게 동작
		```
		2. @SpringBootTest(MovieService.class)
		```java
		- MovieService만 필요한 테스트 이므로 해당 클래스와 관련된 Bean만 스프링 컨테이너에 등록된다.
		- 불필요한 bean등록을 피해 테스트 속도를 높힌다.
		```
		3. @MockBean
		```java
		- 임시객체인 Mock객체를 편하게 사용할 수 있도록 제공
		- ApplicationContext 에 들어있는 Bean을 해당 Mock 객체로 교체한다.
		- 모든 @Test마다 자동으로 리셋된다
		```
		4. **Mockito, BDDMockito 공부 필요**
		5. Test Code
		```java
		@SpringBootTest(classes = MovieService.class)
		class MovieServiceTest {

		    @Autowired
		    private MovieService movieService;

		    @MockBean
		    private MovieRepository movieRepository;

		    @DisplayName("평점 순으로 정렬되는지 검사")
		    @Test
		    void shouldSortedInOrderOfGrade(){
		        //given
		        String query = "테스트";
		        String expectedTopRankingMovieTitle = "평점1위";
		        given(movieRepository.findByQuery(anyString())).willReturn(this.getStubMovies());
		
		        //when
		        List<Movie> movies = movieService.search(query);
		
		        //then
		        Assertions.assertEquals(movies.stream().findFirst().get().getTitle(),expectedTopRankingMovieTitle);
		    }
		
		    List<Movie> getStubMovies(){
		        return Arrays.asList(
		                Movie.builder().title("평점0").link("http://test").userRating(0.0f).build(),
		                Movie.builder().title("평점2위").link("http://test").userRating(9.3f).build(),
		                Movie.builder().title("평점3위").link("http://test").userRating(8.7f).build(),
		                Movie.builder().title("평점1위").link("http://test").userRating(9.7f).build()
		                );
		    }
		}
		```
	
2. ### 과제
	1. #### 영화 검색 서비스 캐싱 역할 만들기
		```java
		1. 사용자에 의한 키워드 검색 시 네이버 Open API 영화 검색 결과를 애플리케이션 내부에 정의한 자료구조로 저장해 놓기
		  - Map을 사용해서 key에는 검색어, Value에는 검색 결과를 저장
		2. 사용자에 의한 키워드 검색 시 특정 키워드에 해당하는 데이터가 자료구조에 이미 저장되어 있다면, 네이버 오픈 API를 호출하지 않고,
		저장된(캐싱된) 데이터를 사용하도록 없으면 네이버 Open API 호출
		3. 관리자에 의한 캐시 데이터를 강제로 업데이트(갱신)하는 RestController API 만들기
		  - RestAPI의 갱신해주는 API를 호출하면, 네이버 오픈 API를 호출 후 캐시에 저장
		```

		1. ##### MovieService 코드 수정
		```java
		@Service
		public class MovieService {

		    //캐싱용으로 사용 할 자료구조
		    static HashMap<String, List<Movie>> hm = new HashMap<>();
		
		    @Autowired
		    MovieRepository movieRepository;
		
		    public MovieService(MovieRepository movieRepository){
		        this.movieRepository = movieRepository;
		    }

		    public List<Movie> search(final String query){
		        List<Movie> movies = null;
		        if(hm.containsKey(query)){ // 자료구조에 있는지 검색
		            //있다면 받아오기
		            movies = hm.get(query);
		            //System.out.println(query+"결과 캐시에서 가져옴");
		        }
		        else{ //없으면 네이버 Open API 호출
		            MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
		            movies = movieGroup.getListOrderRating();
		            hm.put(query,movies);
		            //System.out.println(query+"결과 네이버 OpenAPI 에서 가져옴");
		        }
		        return movies;
		    }
		```
		2. ##### 관리자 요청받을 컨트롤러 생성
		| Methods | Urls | Actions |
		|---|:---:|:---:|
		| GET | /cache/movies/{query} | query에 해당하는 영화 캐시 데이터 Update 후 데이터 반환 |
		```java		
		@RestController
		@RequestMapping("/cache")
		public class CacheAPIController {
		
		    @Autowired
		    MovieService movieService;

		    //url 패턴으로 받기		
		    @GetMapping("/movies/{query}")
		    public List<Movie> movieDataUpdate(@PathVariable String query){
		        return movieService.cacheUpdate(query);
		    }
		}
		```
		3. ##### MovieService에 관리자 Update 내용 추가
		```java
		    //관리자 강제 Update용
		    public List<Movie> cacheUpdate(final String query) {
		        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
		        List<Movie> movies = movieGroup.getListOrderRating();
		        hm.put(query, movies);
		        //System.out.println(query+" 결과 Update");
		        return movies;
		    }
		```
		
## 4주차
### AOP와 스프링캐시 추상화, Redis 연동 
---
1. ### [AOP](https://brunch.co.kr/@springboot/542)
	```java
	* AOP(Aspect-Oriented Programming)란
	 - OOP를 보완하는 수단으로, 흩어진 Aspect를 모듈화 할 수 있는 프로그래밍 기법
	 - 공통된 기능을 재사용하는 기법이다.
	 - 스프링프레임워크의 (@RestController 등등의) 어노테이션 자체가 AOP 프레임워크이다.
	
	* AOP의 장점
	 1. 어플리케이션 전체에 흩어진 공통기능 (Crosscutting Concerns)이 하나의 장소에서 관리 된다는 점
	 2. 다른 서비스 모듈들이 본인의 목적에만 충실하고  그 외 사항들은 신경쓰지 않아도 된다는 점 

	* AOP 주요개념 용어 (Spring에서만 사용되는것이 아닌 AOP 프레임워크 전체에서 사용되는 공용어)
	 1. 타겟(Target)
	    - 부가기능을 부여할 대상

	 2. Aspect
	    - 포인트컷과 어드바이스의 결합이다
	    - 객체지향 모듈이 Object라 불리는 것과 같이 부가기능 모듈을 Aspect라고 부른다
  
	 3. 어드바이스(Advice)
	    - 실질적으로 부가기능을 담은 구현체
	    - 횡단 관심에 해당하는 공통 기능의 코드, 독립된 클래스의 메소드로 작성한다
	    - 타겟 object에 종속되지 않아 순수하게 부가 기능에만 집중
	    - Aspect가 무엇을, 언제 할지를 정의
	    - 어드바이스 정의는 동작시점을 기준으로 5가지 있다.
	        1) 메소드 실행 전 @Before 
	        2) 메소드 실행 후 @After
	        3) 메소드 정상적으로 실행 된 후 @AfterReturning
	        4) 메소드 예외가 발생한 경우 @AfterThrowing 
	        5) 메소드 호출 이전, 이후, 예외발생등 모든 시점에서 동작 @Around

	4. PointCut
	   - 부가기능이 적용될 대상(메소드)를 선정하는 방법
	   - Advice를 적용할 JoinPoint를 선별하는 기능을 정의한 모듈을 말한다
	   - PointCut 정의는 @Around안에서 정의하며
	     표현식으로는 execution, @annotation, bean 3가지가 있다.
	   - ||, &&, ! 을 사용하여 포인트컷 조합 가능하다.

	5. JoinPoint
	  - 클라이언트가 호출하는 모든 비즈니스 메소드, 조인포인트 중에서 포인트컷되기 때문에 
	    포인트컷의 후보로 생각할 수 있다.
	  - 다른 AOP 프레임워크와 달리 spring에서는 메소드 조인포인트만 제공

	6. Proxy
	  - Target을 감싸서 Target의 요청을 대신 받아주는 Wrapping Object
	  - 클라이언트에서 타겟을 호출하게 되면 타겟이 아닌 타겟을 감싸고 있는 프록시가 호출되어, 
	    타겟 메소드 실행전에 선처리, 타겟 메소드 실행 후, 후처리를 실행시키도록 구성
	
	* 스프링 AOP
	 - 프록시 기반의 AOP 구현체
	 - 스프링 빈에만 AOP 적용가능
	 - 스프링 IoC와 연동하여 엔터프라이즈 애플리케이션에서 가장 흔한 문제에 대한 해결책을 제공하는것이 목적
	 
	* 애노테이션으로 포인트컷 정의해서 AOP 사용하기
	 step 0. AOP의존성 추가
	 step 1. 애노테이션 A 정의 (@Target, @Retention 사용)
	 step 2. Aspect class (@Component, @Aspect 붙여서 정의) 만들고 메서드에 @Around(@annotation으로 포인트컷정의)를 통해 어드바이스 정의
	 step 3. 함수에 해당 A 어노테이션 사용
	 
	```
	
	1. #### 메서드의 수행시간을 계산하는 간단한 AOP 구현하기
		0. ##### 의존성 추가
		```java
		org.springframwork.boot:spring-boot-starter-aop
		```
		1. ##### Step1 애노테이션 정의
		```java
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PerformanceTimeRecord {
		}
		```
		2. ##### Step2 Aspect class 정의
		```java
		@Slf4j
		@Aspect
		@Component
		public class RecodeAspectProvider {
		
		    //PointCut = @Around에 정의한 애노테이션을 사용한 메소드에만 해당 코드가
		    // 메소드 수행 전처리 후처리 사용된다
		    @Around("@annotation(com.example.demo.provider.timePerformanceTimeRecord)")
		    public Object logExcutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		        System.out.println("time 체크 시작");
		        long start = System.currentTimeMillis();

		        //가장 중요한 구문으로 실제 애노테이션이 붙은 메소드의 내용이 실행된다.
		        Object proceed = joinPoint.proceed();

		        long executionTime = System.currentTimeMillis() - start;
		        System.out.println("결과 시간 = " + executionTime);

		        //@Slf4j로 log 사용
		        log.info(joinPoint.getSignature()+ "executed in "+ executionTime + "ms");

		        //반드시 proceed의 결과를 return 해주어야 한다.
		        return proceed;
		    }
		}
		```
		3. ##### Step3 Service단 함수에 정의한 애노테이션 사용
		```java
		@Service
		public class MovieService {

		    @PerformanceTimeRecord
		    public void aopTest(){
		        System.out.println("AOP Test");
		    }
		}
		```
		4. ##### 결과
		- <img src="https://user-images.githubusercontent.com/60174144/106862344-564aab00-670a-11eb-9e47-af0c967ca8ca.png" width="50%" height="50%">


2. ### AOP를 이용한 영화, 쇼핑 검색 데이터 캐싱 직접 구현하기
	1. #### Concern	
		- <img src="https://user-images.githubusercontent.com/60174144/107015991-bdd32a00-67e0-11eb-8d5b-1105e5a08757.png" width="50%" height="50%">

	2. #### ConcurrentMap을 사용하여 메모리기반 저장소 cache와 CacheManager 구현하기(커스텀)
		- <img src="https://user-images.githubusercontent.com/60174144/107031618-d0a42980-67f5-11eb-8ca1-0efdd6dd4c68.png" width="50%" height="50%">		
	
	3. #### Aspect에서 CustomCacheManager 사용하여 구현
		1. 애노테이션 정의
		```java
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface LookAsideCaching {
		     String value(); //cacheName
		     String key() default "NONE"; //cacheKey
		}
		```		
		2. [Aspect 정의 - 코드참조](https://github.com/hwangyoungjin/SpringGroupStudy/blob/main/src/main/java/com/example/demo/provider/cache/CachingAspectProvider.java)
			- <img src="https://user-images.githubusercontent.com/60174144/107031800-22e54a80-67f6-11eb-9d4c-931255349f18.png" width="50%" height="50%">
			- [joinPoint 메소드 사용참조](https://codedragon.tistory.com/9013)
		3. Service에 애노테이션 사용
		```java
		    //value는 cacheName 
		    @LookAsideCaching(value = "cache::search-movies",key = "query")
		    public List<Movie> search(final String query){
		        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
		        return movieGroup.getListOrderRating();
		    }
		```
	4. #### PSA(Portable Service Abstraction)
		- <img src="https://user-images.githubusercontent.com/60174144/107018896-5b7c2880-67e4-11eb-9687-8f10fdfa4a62.png" width="50%" height="50%">
		

3. ### [스프링에서 제공하는 캐시 추상화](https://brunch.co.kr/@springboot/543)
	1. #### 스프링에서 제공하는 캐시 스프링부트에서 사용하기
		1. ##### Step1. spring-boot-starter-cache 의존성 추가
		```java
		org.springframwork.boot:spring-boot-starter-cache

		* 의존성 추가시
		 1. CacheManager를 빈으로 등록 ()
		 2. Redis, EhCache등 에서 지원하는 캐시 라이브러리를 사용하지 않는경우 
		    ConcurrentMapCacheManager를 제공하며 
		    저장소로는 메모리를 기반으로 하여 ConcurrentHashMap를 사용
		 3. ConcurrentMapCacheManager가 빈으로 등록된다. 
		* 의존성 추가 후 Redis 의존성 까지 있는경우
		 1. RedisCacheManager를 빈으로 등록
		
	
		```
		2. ##### Step2. 캐시 기능을 사용하고 싶은 프로젝트에 @EnableCaching 추가
		```java
		@SpringBootApplication
		@EnableCaching
		public class DemoApplication {
		    public static void main(String[] args) {
		        SpringApplication.run(DemoApplication.class, args);
		    }
		}
		```
		3. ##### Step3. @Cacheable, @CacheEvict
		```java
		* 캐시하고 싶은 메서드에 @Cacheable 
		* 캐시를 제거하고자하는 메서드에는 @CacheEvict를 쓴다. => 보통 remove나 delete 함수에 사용

		@Service
		public class MovieService {

		    @Autowired
		    MovieRepository movieRepository;

		    public MovieService(MovieRepository movieRepository){
		        this.movieRepository = movieRepository;
		    }

		    @Cacheable(value = "cache::movies::query") //cache::movies::query + 파라미터query값으로 key값 생성된다.
		    public List<Movie> search(final String query){
		        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
		        return movieGroup.getListOrderRating();
		    }
		```

		
4. ### Redis 연동
	1. #### 의존성 추가하여 Redis 사용하기
	```java
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'

	* 주로 캐시, 메시지브로커, 키-벨류 스토어등으로 사용

	* redis 사용의 장점
	 1) ConcurrentMapCacheManager의 단점인 휘발성 메모리를 해결
	 2) 서버간의 데이터 불일치 해결
	 3) 스케일업 환경으로 애플리케이션의 메모리가 부족해지는 현상 해결

	* Redis 연동시
	 1) 스프링 데이터 Redis에는 RedisCacheManager를 제공
	 2) RedisCacheManager는 RedisTemplate를 통해 Redis 서버를 가지고 동작

	* Redis 주요 커맨드
	 - keys * : 모든 key를 검색
	 - get "key값" : key값에 해당하는 value를 검색

	* 도커에서 Redis 실행시
	 - docker run -p 6379:6379 --name redis_boot -d redis
	 - docker exec -i -t redis_boot redid-cil

	* redis포트가 6379라면 스프링 부트에서 따로 포트 설정 할 필요X
	 - 포트번호가 6379가 아니고 3030이라면 application.properties 파일에 포트 설정
	    # Redis Setting
	    spring.cache.type=redis
	    spring.redis.host=localhost
	    spring.redis.port=3030
	```
	2. #### 프로젝트에 의존성만 추가하여 redis 적용
	- <img src="https://user-images.githubusercontent.com/60174144/107064711-32c55480-681f-11eb-8c04-2f00ac1a2096.png" width="50%" height="50%">
			
# COSMO'S

![COSMO'S](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/logo.png)

## 프로젝트 개요

최근 몇 년간 원격 근무와 온라인 교육의 수요등이 급증하면서, 효율적인 원격 커뮤니케이션 도구에 대한 필요성이 커졌습니다.    

기존의 화상회의 프로그램들은 다양한 기능을 제공하지만, 개발자에게 특정된 다양한 요구사항을 모두 만족시키지 못하는 경우가 많았습니다.    

이러한 시장의 필요를 충족시키기 위해, 웹소켓 기술을 이용하여 실시간으로 채팅 및 화상회의를 하며 개발환경을 제공하는 시스템을 개발하기로 결정하였습니다.

## 제작 기간 및 타임라인
![Timeline](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/timeline.png)

## 프로젝트 구조 및 기술 스택
![architecture](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/architecture.png)

## 창의적 구현 및 특이점

webRTC기능 및 웹소켓 기능을 구현하기 위해 Openvidu라는 오픈소스 라이브러리를 사용했습니다.  
  
이를 통해 직접 구현하는 것 보다 안정성이 높은 서비스를 구현할 수 있었습니다.  
또한, Openvidu에서 제공하는 해상도 조절, 카메라 끄기, 채팅 기능 등 다양한 기능을 손쉽고 빠르게 구현할 수 있었습니다.

## 트러블슈팅 경험
### 1. 배포 환경에서의 CORS 문제
- Spring Security의 설정때문에 CORS문제가 발생했습니다.
  
- allowedOrigins에 "*" 을 넣어 모든 요청을 허용하도록 설정하였으나,  
Spring boot 2.4 이상부터는 와일드카드를 사용할 수 없다는 것을 검색을 통해 알게 되었습니다.

- 아래 **개선된 코드**와 같이 직접 도메인을 작성하여 해결하였습니다.

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://groomcosmos.site")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
~~~

</div>
</details>

</br>

### 2. NGNIX 커스텀 헤더 문제
- 팀원분의 JWT 설정중, 보안을 강화하려는 취지로 헤더와 토큰에 각각 access_token, refresh_token을 커스텀하여 설정하였습니다.

- Postman을 사용한 테스트에는 아무런 문제가 없어,  `@AuthenticationPrincipal CustomUserDetails customUserDetails)` 을 사용해 로그인 한 유저 정보를 받아오도록 설정하였습니다.

- 하지만, 실제 배포 환경에서 Request header에는 access_token이 존재하나, Spring 서버에서 `CustomUserDetails`가 null 이라는 것을 알게 되었습니다.
  
- NGINX를 이용한 http인 백엔드 서버 ↔ https인 프론트 서버 간의 통신을 위해 설정한 리버스 프록시 설정에 문제가 있으리라 생각하였습니다.

- 아래 **기존 설정** 처럼 `proxy_set_header`를 통해 직접적으로 명시해 주었습니다.

<details>
<summary><b>기존 설정</b></summary>
<div markdown="1">

~~~nginx
    location /api/ {
        proxy_pass http://backend-server-ip;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        proxy_set_header Cookie $http_cookie;
        proxy_set_header access_token $http_access_token;

        proxy_set_header X-Access-Token $http_x_access_token;
    }
~~~

</div>
</details>

</br>

- 하지만 여전히 해결되지 않았고, 조금 더 검색해 본 결과, NGINX에서는 header에 underscore(_) 사용시 기본적으로 무시한다는 것을 알게 되었습니다.
  
- `underscores_in_headers on` 을 NGINX에 설정해 준 결과 해결되었습니다.

</br>

### 3. 기본적인 에러 로깅 문제
- 2번에서의 NGINX 문제 해결중, 예외처리로 항상 `INTERNAL_SERVER_ERROR` 로 설정해 둔 것이 문제가 되었습니다.

- 오류가 발생했으나, 항상 http 500 문제가 발생하여 어떤 부분이 문제인지 확인하기 어려웠습니다.

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java
    // 세션 생성
    @PostMapping("/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody VideoChatDto videoChatDto,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            String sessionId = videoChatService.initializeSession(videoChatDto, customUserDetails);
            return ResponseEntity.ok(sessionId);
        }  catch (Exception e) {
            // 기타 예외 처리
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
~~~

</div>
</details>

</br>

- 어떤 부분에서 왜 에러가 발생하는지 직접적으로 확인하기 위해 에러 로깅을 세분화하고, 주석을 작성하여 `@AuthenticationPrincipal CustomUserDetails customUserDetails` 부분의 null값이 문제였다는 것을 알게 되었습니다

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
    // 세션 생성
    @PostMapping("/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody VideoChatDto videoChatDto,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (videoChatDto == null) {
            // videoChatDto가 null인 경우의 에러 메시지
            return new ResponseEntity<>("Request body (videoChatDto) is null", HttpStatus.BAD_REQUEST);
        }

        if (videoChatDto.getProperties() == null) {
            // properties 필드가 null인 경우의 에러 메시지
            return new ResponseEntity<>("'properties' field in videoChatDto is null", HttpStatus.BAD_REQUEST);
        }

        try {
            String sessionId = videoChatService.initializeSession(videoChatDto, customUserDetails);
            return ResponseEntity.ok(sessionId);
        } catch (OpenViduJavaClientException e) {
            // OpenViduJavaClientException 발생 시 처리
            return new ResponseEntity<>("OpenVidu Java Client error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (OpenViduHttpException e) {
            // OpenViduHttpException 발생 시 처리
            if (e.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                return new ResponseEntity<>("OpenVidu authorization error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>("OpenVidu HTTP error: " + e.getMessage(), HttpStatus.valueOf(e.getStatus()));
            }
        } catch (Exception e) {
            // 기타 예외 처리
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
~~~

</div>
</details>

</br>

<details>
<summary><h3>4. CI/CD 설정 중 EC2가 동작하지 않는 이슈</h3></summary>
<div markdown="1">

- Jenkins를 이용하여 CI/CD를 설정하고 테스트 하던 도중, EC2가 계속해서 동작하지 않는 이슈가 발생했습니다.
- 아마존 프리티어를 이용하고 있었기에, 1GB의 램으로는 build 하는대에 문제가 발생한다는 것을 알았습니다.
- HDD의 일정 공간을 RAM처럼 사용하는 SWAP메모리를 통해 해결하였습니다.

</div>
</details>




## 회고

### 잘 했던 점(Keep)
webRTC를 이용한 화상회의는 안정적으로 구현하기 어렵다고 판단되어 오픈소스 라이브러리를 찾아본것이 잘 했던 점이라고 생각됩니다.  
  
또한 프로젝트 초기에 CI/CD 구현을 완료하여 Github에 push하는 것 만으로 배포가 되어, 실제 구현 환경에 맞게 테스트 할 수 있었습니다.  

### 문제 및 아쉬웠던 점(Problem)
배포 환경에서의 문제를 생각하지 않고 코드를 작성하다 보니 실제 배포 환경에서 문제가 많이 생겼습니다.  
또한, 예외처리 및 에러코드를 적절히 사용하지 않아 문제가 발생했을 시 어떤 부분이 문제인지 확인하기 어려운 경우가 많았습니다.  
이로 인해 프로젝트 일정이 매우 지연되었습니다.

### 다음 프로젝트에 적용할 점 (Try)
배포 환경에서의 문제를 미리 생각하고 코드를 작성해야 하며,  
예외처리 및 에러코드를 적절히 사용하여 프론트 환경 및 콘솔에서 로그를 통해 어떤 부분이 문제인지 빠르게 확인하여 수정하도록 하겠습니다.


   

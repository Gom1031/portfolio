# COSMO'S

![COSMO'S](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/logo.png)

## 프로젝트 개요

최근 몇 년간 원격 근무와 온라인 교육의 수요등이 급증하면서, 효율적인 원격 커뮤니케이션 도구에 대한 필요성이 커졌습니다.    

기존의 화상회의 프로그램들은 다양한 기능을 제공하지만, 개발자에게 특정된 다양한 요구사항을 모두 만족시키지 못하는 경우가 많았습니다.    

이러한 시장의 필요를 충족시키기 위해, 웹소켓 기술을 이용하여 실시간으로 채팅 및 화상회의를 하며 개발환경을 제공하는 시스템을 개발하기로 결정하였습니다.

## 창의적 구현 및 특이점

webRTC기능 및 웹소켓 기능을 구현하기 위해 Openvidu라는 오픈소스 라이브러리를 사용했습니다.  
  
이를 통해 직접 구현하는 것 보다 안정성이 높은 서비스를 구현할 수 있었습니다.  
또한, Openvidu에서 제공하는 해상도 조절, 카메라 끄기, 채팅 기능 등 다양한 기능을 손쉽고 빠르게 구현할 수 있었습니다.

## 회고

### 잘 했던 점(Keep)
직접 구현하기 어렵다고 판단되어 오픈소스 라이브러리를 찾아본것이 잘 했던 점이라고 생각됩니다.  
  
또한 프로젝트 초기에 CI/CD 구현을 완료하여 Github에 push하는 것 만으로 배포가 되어, 실제 구현 환경에 맞게 테스트 할 수 있었습니다.  

### 문제 및 아쉬웠던 점(Problem)
배포 환경에서의 문제를 생각하지 않고 코드를 작성하다 보니 실제 배포 환경에서 문제가 많이 생겼습니다.  
또한, 예외처리 및 에러코드를 적절히 사용하지 않아 문제가 발생했을 시 어떤 부분이 문제인지 확인하기 어려운 경우가 많았습니다.  
이로 인해 프로젝트 일정이 매우 지연되었습니다.

### 다음 프로젝트에 적용할 점 (Try)
배포 환경에서의 문제를 미리 생각하고 코드를 작성해야 하며,  
예외처리 및 에러코드를 적절히 사용하여 프론트 환경 및 콘솔에서 로그를 통해 어떤 부분이 문제인지 빠르게 확인하여 수정하도록 하겠습니다.


---

### Architecture & Technology Stack
![architecture](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/architecture.png)

---

### Timeline
![Timeline](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/timeline.png)

---

### ERD
![ERD](https://github.com/Gom1031/portfolio/blob/main/%EA%B5%AC%EB%A6%84_1%EC%B0%A8_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/images/ERD.png)
   

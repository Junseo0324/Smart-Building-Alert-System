# 스마트 빌딩 알림 시스템 - SBAS
건물의 재난 상황을 탐지해 사용자에게 실시간 알림을 전송하는 어플리케이션

<br>

## 서비스 소개

- 스마트 빌딩 알림 시스템은 아두이노 모듈을 활용하여 다양한 센서 데이터를 수집하고, 재난 상황을 모니터링하는 시스템입니다.
- 온도, 가스 누출(CO2), 지진과 같은 재난 상황을 실시간으로 탐지하여, 문제가 발생할 경우 사용자에게 즉시 알림을 전송합니다.
- 사용자는 안드로이드 앱을 통해 재난 상황에 대한 정보를 실시간으로 받아볼 수 있습니다.

<br>

## 기능 소개

- Firebase Cloud Messaging 을 통한 **실시간 재난 알림 처리**
- 아두이노 모듈로 실시간 재난 상황을 탐지하고 , REST API를 통해 DB에 센서 데이터 저장
- 사용자가 안드로이드 앱을 통해 시간별 센서 데이터 확인 가능

<br>

## 기술 스택

- **Android**: Kotlin, Firebase Cloud Messaging , View binding , SharedPreferences , Retrofit
- **Backend**: Java, Spring boot, MySQL

<br>


## 서비스 UI
<img src = "images/appinfo-1.png" width ="30%" /> <img src = "images/appinfo-2.png" width= "30%"/>

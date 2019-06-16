# Sky Talk
Sky Talk는 카카오톡 PC버전을 모티브로한 간단한 채팅 프로그램입니다.
Socket API를 이용하여 Client/Server 구조로 설계, Java언어로 작성되었습니다.

# Sky Talk 주요기능
### - 채팅
1대1 채팅, 채팅방 생성하여 멀티 채팅

### - 이모티콘 전송
채팅방에서 이모티콘 전송 가능

### - 알림
메시지 수신 시 알림, 새로운 채팅방 생성 시 알림

### - 개인 프로필 설정
상태 이미지와 상태 이미지 변경 가능


# 시스템 구성도
<img src="/image/Architecture.png">

# 주요 기능 프로토콜
서버와 프로토콜의 통신에서 일반 메시지를 주고받는것과 시그널을 주고받는것을 구분하기 위해 User클래스에
final로 시그널을 정의해놓고 통신 시 보내는 메시지 앞에 붙여서 구분 후 적절하게 처리한다

<img src="/image/protocol1.png">
<img src="/image/protocol2.png">

# 시현 화면
<img src="/image/program_capture.png">


### 역할 분담
김하늘 : 프로그램 UI(Java swing), 알림, 개인 프로필 설정

이예지 : 채팅, 친구관리, 채팅방 관리 

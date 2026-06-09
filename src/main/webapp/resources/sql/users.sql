CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 번호',
    username    VARCHAR(50)  NOT NULL UNIQUE   COMMENT '사용자 아이디',
    password    VARCHAR(255) NOT NULL          COMMENT '비밀번호 (암호화 해시값 저장용)',
    email       VARCHAR(100) NOT NULL UNIQUE   COMMENT '이메일 주소',
    nickname    VARCHAR(50)  NOT NULL          COMMENT '서비스 내 닉네임',
    created_at  DATETIME     DEFAULT NOW()     COMMENT '가입 일시',
    updated_at  DATETIME     DEFAULT NOW() ON UPDATE NOW() COMMENT '수정 일시'
) ;

desc users;


select * from users;

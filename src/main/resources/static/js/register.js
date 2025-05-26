// 전화번호 인증 관련 변수
let verificationCode = null;
let isPhoneVerified = false;

// 전화번호 인증번호 발송 (개발/테스트용)
async function sendVerificationCode() {
    const phone = document.getElementById('phone').value;
    if (!phone) {
        alert('전화번호를 입력해주세요.');
        return;
    }

    // 실제 서버 요청 대신, 임의의 인증번호 생성 및 자동 입력
    verificationCode = String(Math.floor(100000 + Math.random() * 900000));
    document.getElementById('verificationCode').value = verificationCode;
    alert('테스트용 인증번호가 자동 입력되었습니다: ' + verificationCode);

    // 실제 서버 요청이 필요하다면 아래 코드 주석 해제
    /*
    try {
        const response = await fetch('/api/auth/send-verification', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ phone })
        });

        if (response.ok) {
            alert('인증번호가 발송되었습니다.');
            verificationCode = await response.text();
        } else {
            alert('인증번호 발송에 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('인증번호 발송 중 오류가 발생했습니다.');
    }
    */
}

// 인증번호 확인
function verifyCode() {
    const inputCode = document.getElementById('verificationCode').value;
    if (!inputCode) {
        alert('인증번호를 입력해주세요.');
        return;
    }

    if (inputCode === verificationCode) {
        alert('인증이 완료되었습니다.');
        isPhoneVerified = true;
        document.getElementById('verificationCode').readOnly = true;
    } else {
        alert('인증번호가 일치하지 않습니다.');
    }
}

// 주소 검색
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('address').value = data.address;
            document.getElementById('detailAddress').focus();
        }
    }).open();
}

// 비밀번호 유효성 검사
function validatePassword(password) {
    const minLength = 8;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumbers = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    return password.length >= minLength && 
           hasUpperCase && 
           hasLowerCase && 
           hasNumbers && 
           hasSpecialChar;
}

// 비밀번호 확인 검사
function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('passwordConfirm').value;
    
    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return false;
    }
    return true;
}

// 생년월일 유효성 검사
function validateBirthdate(birthdate) {
    const today = new Date();
    const birth = new Date(birthdate);
    const age = today.getFullYear() - birth.getFullYear();
    
    if (age < 14) {
        alert('14세 이상만 가입 가능합니다.');
        return false;
    }
    return true;
}

// 폼 제출 처리
async function handleRegister(event) {
    event.preventDefault();

    // 필수 입력값 검사
    const requiredFields = ['lastName', 'firstName', 'email', 'password', 'passwordConfirm', 'phone', 'birthdate'];
    for (const field of requiredFields) {
        const element = document.getElementById(field);
        if (!element.value) {
            alert(`${element.previousElementSibling.textContent}을(를) 입력해주세요.`);
            element.focus();
            return false;
        }
    }

    // 비밀번호 유효성 검사
    const password = document.getElementById('password').value;
    if (!validatePassword(password)) {
        alert('비밀번호는 8자 이상이며, 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.');
        return false;
    }

    // 비밀번호 확인
    if (!checkPasswordMatch()) {
        return false;
    }

    // 전화번호 인증 확인
    if (!isPhoneVerified) {
        alert('전화번호 인증을 완료해주세요.');
        return false;
    }

    // 생년월일 유효성 검사
    const birthdate = document.getElementById('birthdate').value;
    if (!validateBirthdate(birthdate)) {
        return false;
    }

    // 이용약관 동의 확인
    const requiredCheckboxes = document.querySelectorAll('.terms-agreement input[required]');
    for (const checkbox of requiredCheckboxes) {
        if (!checkbox.checked) {
            alert('필수 이용약관에 동의해주세요.');
            return false;
        }
    }

    // 폼 데이터 수집
    const formData = {
        lastName: document.getElementById('lastName').value,
        firstName: document.getElementById('firstName').value,
        nickname: document.getElementById('nickname').value,
        email: document.getElementById('email').value,
        password: password,
        phone: document.getElementById('phone').value,
        gender: document.querySelector('input[name="gender"]:checked').value,
        birthdate: birthdate,
        address: {
            postcode: document.getElementById('postcode').value,
            address: document.getElementById('address').value,
            detailAddress: document.getElementById('detailAddress').value
        },
        marketingConsent: document.querySelector('.terms-agreement input:last-child').checked
    };

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/auth/login';
        } else {
            const error = await response.json();
            alert(error.message || '회원가입 중 오류가 발생했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('회원가입 중 오류가 발생했습니다.');
    }

    return false;
} 
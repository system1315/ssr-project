async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });
        
        if (response.ok) {
            window.location.href = '/board/list';
        } else {
            const error = await response.text();
            alert(error);
        }
    } catch (error) {
        alert('로그인 중 오류가 발생했습니다.');
    }
    
    return false;
} 
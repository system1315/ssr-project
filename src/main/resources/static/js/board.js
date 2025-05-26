function deleteBoard(id) {
    if (!confirm('게시글을 삭제하시겠습니까?')) return;

    fetch(`/board/delete/${id}`, {
        method: 'POST'
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            window.location.href = '/board/list';
        } else if (result === 'unauthorized') {
            alert('게시글을 삭제할 권한이 없습니다.');
        }
    })
    .catch(error => {
        console.error('게시글 삭제 실패:', error);
    });
} 
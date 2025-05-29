// 댓글 목록 조회 (페이징 적용)
async function loadComments(boardId, page = 1, size = 10) {
    try {
        const response = await fetch(`/api/comments/board/${boardId}/paged?page=${page}&size=${size}`);
        const data = await response.json();
        displayComments(data.content);
        displayCommentPagination(data, boardId, size);
    } catch (error) {
        console.error('댓글 로딩 실패:', error);
    }
}

// 댓글 표시
function displayComments(comments) {
    const commentList = document.getElementById('commentList');
    commentList.innerHTML = '';
    const currentUser = document.getElementById('currentUser').textContent;
    
    comments.forEach(comment => {
        const commentElement = document.createElement('div');
        commentElement.className = 'comment-item mb-3 p-3 border rounded';
        commentElement.innerHTML = `
            <div class="d-flex justify-content-between align-items-start">
                <div>
                    <strong class="comment-author">${comment.author}</strong>
                    <small class="text-muted ms-2">${new Date(comment.createdDate).toLocaleString()}</small>
                </div>
                ${comment.author === currentUser ? `
                    <div class="btn-group">
                        <button onclick="editComment(${comment.id})" class="btn btn-primary btn-sm">
                            <i class="bi bi-pencil-square"></i> 수정
                        </button>
                        <button onclick="deleteComment(${comment.id})" class="btn btn-danger btn-sm">
                            <i class="bi bi-trash"></i> 삭제
                        </button>
                    </div>
                ` : ''}
            </div>
            <div class="comment-content mt-2" id="comment-content-${comment.id}">${comment.content}</div>
        `;
        commentList.appendChild(commentElement);
    });
}

// 댓글 페이지네이션 표시
function displayCommentPagination(pageData, boardId, size) {
    const pagination = document.getElementById('commentPagination');
    pagination.innerHTML = '';
    if (pageData.totalPages <= 1) return;
    const ul = document.createElement('ul');
    ul.className = 'pagination justify-content-center';

    // 이전 버튼
    const prevLi = document.createElement('li');
    prevLi.className = 'page-item' + (pageData.currentPage === 1 ? ' disabled' : '');
    prevLi.innerHTML = `<a class="page-link" href="#">&laquo;</a>`;
    prevLi.onclick = (e) => {
        e.preventDefault();
        if (pageData.currentPage > 1) loadComments(boardId, pageData.currentPage - 1, size);
    };
    ul.appendChild(prevLi);

    // 페이지 번호 (5개 단위)
    let start = Math.max(1, pageData.currentPage - 2);
    let end = Math.min(pageData.totalPages, start + 4);
    if (end - start < 4) start = Math.max(1, end - 4);
    for (let i = start; i <= end; i++) {
        const li = document.createElement('li');
        li.className = 'page-item' + (i === pageData.currentPage ? ' active' : '');
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.onclick = (e) => {
            e.preventDefault();
            loadComments(boardId, i, size);
        };
        ul.appendChild(li);
    }

    // 다음 버튼
    const nextLi = document.createElement('li');
    nextLi.className = 'page-item' + (pageData.currentPage === pageData.totalPages ? ' disabled' : '');
    nextLi.innerHTML = `<a class="page-link" href="#">&raquo;</a>`;
    nextLi.onclick = (e) => {
        e.preventDefault();
        if (pageData.currentPage < pageData.totalPages) loadComments(boardId, pageData.currentPage + 1, size);
    };
    ul.appendChild(nextLi);

    pagination.appendChild(ul);
}

// 댓글 작성 (작성 후 1페이지로 이동)
async function addComment(boardId) {
    const content = document.getElementById('commentContent').value;
    const author = document.getElementById('currentUser').textContent;
    
    if (!content.trim()) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    if (!author) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch(`/api/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                boardId: boardId,
                content: content,
                author: author
            })
        });
        
        if (response.ok) {
            document.getElementById('commentContent').value = '';
            loadComments(boardId, 1, 10); // 작성 후 1페이지로 이동
        } else if (response.status === 401) {
            alert('로그인이 필요합니다.');
        }
    } catch (error) {
        console.error('댓글 작성 실패:', error);
    }
}

// 댓글 수정 (수정 후 현재 페이지 유지)
async function editComment(commentId) {
    const contentElement = document.getElementById(`comment-content-${commentId}`);
    const currentContent = contentElement.textContent;
    
    const newContent = prompt('댓글을 수정하세요:', currentContent);
    if (newContent === null || newContent === currentContent) return;

    try {
        const response = await fetch(`/api/comments/${commentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: newContent
            })
        });
        
        if (response.ok) {
            // 현재 페이지 유지
            const boardId = getBoardId();
            const currentPage = getCurrentCommentPage();
            loadComments(boardId, currentPage, 10);
        } else if (response.status === 401) {
            alert('로그인이 필요합니다.');
        } else if (response.status === 403) {
            alert('댓글을 수정할 권한이 없습니다.');
        }
    } catch (error) {
        console.error('댓글 수정 실패:', error);
    }
}

// 댓글 삭제 (삭제 후 현재 페이지 유지)
async function deleteComment(commentId) {
    if (!confirm('댓글을 삭제하시겠습니까?')) return;

    try {
        const response = await fetch(`/api/comments/${commentId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            // 현재 페이지 유지
            const boardId = getBoardId();
            const currentPage = getCurrentCommentPage();
            loadComments(boardId, currentPage, 10);
        } else if (response.status === 401) {
            alert('로그인이 필요합니다.');
        } else if (response.status === 403) {
            alert('댓글을 삭제할 권한이 없습니다.');
        }
    } catch (error) {
        console.error('댓글 삭제 실패:', error);
    }
}

// 게시글 ID 가져오기
function getBoardId() {
    const pathParts = window.location.pathname.split('/');
    return pathParts[pathParts.length - 1];
}

// 현재 댓글 페이지 가져오기 (페이지네이션에서 active 클래스)
function getCurrentCommentPage() {
    const active = document.querySelector('#commentPagination .page-item.active');
    if (active) {
        return parseInt(active.textContent.trim());
    }
    return 1;
}

// 페이지 로드 시 댓글 목록 로드 (1페이지, 10개)
document.addEventListener('DOMContentLoaded', () => {
    const boardId = getBoardId();
    if (boardId) {
        loadComments(boardId, 1, 10);
    }
});
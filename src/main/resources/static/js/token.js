/**
 *  토큰이 있는지 확인한 후에 유지를 위해서 사용하는 js
 */
function extendToken() 
{
    if (confirm("로그인 상태를 유지하시겠습니까?")) 
    {
        fetch('/token/api', 
        {
            method: 'POST',
            credentials: 'same-origin' // 요청 시에 쿠키를 함께 전송
        })
        .then(response => {
            if (!response.ok) 
            {
                throw new Error('Failed to extend token');
            }
            // 토큰이 성공적으로 연장된 경우 처리
            console.log('로그인이 성공적으로 연장되었습니다.');
        })
        .catch(error => {
            console.error('토큰 연장 실패:', error);
        });
    }
}

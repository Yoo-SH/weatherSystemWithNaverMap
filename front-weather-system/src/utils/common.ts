// 현재 url 경로 가져오기
export const getCurrentPath = (): string => {
    // 기본 경로(pathname)만 가져옴 (쿼리스트링 제외)
    const path = window.location.pathname;
    
    // 경로의 마지막 슬래시(/) 제거
    return path.endsWith('/') ? path.slice(0, -1) : path;
};
package io.Yoo_SH.backend_weather_system.util;

import java.util.Collections;
import java.util.List;

/**
 * 페이지네이션 처리를 위한 유틸리티 클래스
 */
public class PaginationUtil {

    /**
     * 주어진 데이터 목록을 페이지네이션 처리하여 반환합니다.
     *
     * @param allItems   전체 데이터 목록
     * @param pageNo     요청된 페이지 번호 (1-based)
     * @param numOfRows  페이지당 행 수
     * @return 페이지네이션 결과 객체
     */
    public static <T> PaginationResult<T> paginate(List<T> allItems, int pageNo, int numOfRows) {
        int totalCount = allItems.size();
        
        // 페이징 처리를 위한 인덱스 계산
        int fromIndex = (pageNo - 1) * numOfRows;
        int toIndex = Math.min(fromIndex + numOfRows, totalCount);
        
        // 유효하지 않은 페이지 번호일 경우 첫 페이지로 조정
        if (fromIndex >= totalCount) {
            fromIndex = 0;
            toIndex = Math.min(numOfRows, totalCount);
            pageNo = 1;
        }
        
        // 현재 페이지 데이터 추출
        List<T> pagedItems = (totalCount > 0 && fromIndex < totalCount) 
            ? allItems.subList(fromIndex, toIndex) 
            : Collections.emptyList();
        
        return new PaginationResult<>(
            pagedItems,
            pageNo,
            numOfRows,
            totalCount
        );
    }
    
    /**
     * 페이지네이션 결과를 담는 클래스
     */
    public static class PaginationResult<T> {
        private final List<T> items;
        private final int pageNo;
        private final int numOfRows;
        private final int totalCount;
        
        public PaginationResult(List<T> items, int pageNo, int numOfRows, int totalCount) {
            this.items = items;
            this.pageNo = pageNo;
            this.numOfRows = numOfRows;
            this.totalCount = totalCount;
        }
        
        public List<T> getItems() {
            return items;
        }
        
        public int getPageNo() {
            return pageNo;
        }
        
        public int getNumOfRows() {
            return numOfRows;
        }
        
        public int getTotalCount() {
            return totalCount;
        }
    }
} 
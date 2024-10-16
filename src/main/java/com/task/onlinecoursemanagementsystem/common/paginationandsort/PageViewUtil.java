package com.task.onlinecoursemanagementsystem.common.paginationandsort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class PageViewUtil {

    public static <T> Page<T> create(List<T> content) {
        return new Page<T>() {
            @Override
            public int getTotalPages() {
                return content.size();
            }

            @Override
            public long getTotalElements() {
                return content.size();
            }

            @Override
            public <U> Page<U> map(Function<? super T, ? extends U> converter) {
                return (Page<U>) PageViewUtil.create(content.stream().map(converter::apply).toList());
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return content.size();
            }

            @Override
            public int getNumberOfElements() {
                return content.size();
            }

            @Override
            public List<T> getContent() {
                return content;
            }

            @Override
            public boolean hasContent() {
                return !content.isEmpty();
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return content.iterator().hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<T> iterator() {
                return content.iterator();
            }
        };
    }

}

package com.general.template.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginationUtils {

    public static <T, U extends Comparable<U>> Page<T> pagination(List<T> data, Page<T> page, Function<T, U> compareFunc) {
        Comparator<T> comparator = null;
        if (Objects.nonNull(compareFunc)) {
            comparator = Comparator.comparing(compareFunc);
        }
        return pagination(data, page, comparator);
    }

    public static <T, U extends Comparable<U>> Page<T> pagination(List<T> data, Page<T> page, Comparator<T> comparator) {
        if (Objects.nonNull(comparator)) {
            data = data.stream().sorted(comparator).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(data)) {
            List<List<T>> partition = Lists.partition(data, (int) page.getSize());
            if ((page.getCurrent() - 1) < partition.size()) {
                List<T> records = partition.get((int) page.getCurrent() - 1);
                page.setRecords(records);
            }
        }
        page.setTotal(data.size());
        return page;
    }

}

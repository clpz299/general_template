package com.general.template.core;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@ApiModel(value = "PageDTO", description = "分页请求")
public class PageDTO implements Serializable {

    /**
     * 每页大小
     */
    @Min(1)
    @Max(100)
    @ApiModelProperty(value = "每页大小", example = "10")
    @Builder.Default
    int pageSize = Constants.PAGE_SIZE;

    /**
     * 页数
     */
    @Min(1)
    @Max(100)
    @ApiModelProperty(value = "页数", example = "1")
    @Builder.Default
    int pageNo = Constants.PAGE_NO;

    /**
     * 排序 ASC 数组
     */
    @Singular
    @ApiModelProperty(value = "排序 ASC 数组", example = "updateUser")
    List<String> ascs;

    /**
     * 排序 DESC 数组
     */
    @Singular
    @ApiModelProperty(value = "排序 DESC 数组", example = "updateUser")
    List<String> descs;

    /**
     * 默认排序
     */
    @Setter(value = AccessLevel.NONE)
    @Getter(value = AccessLevel.NONE)
    private List<OrderItem> defaultOrder = new ArrayList<>();


    public PageDTO() {
        this.pageSize = Constants.PAGE_SIZE;
        this.pageNo = Constants.PAGE_NO;
    }

    /**
     * 获取dao层分页对象
     *
     * @param <T>
     * @return
     */
    public <T> Page<T> toPage() {
        //@FIXME: 不支持升序倒序的乱序排序
        Stream<OrderItem> ascsStream = Stream.empty();
        Stream<OrderItem> descsStream = Stream.empty();
        Page<T> page = new Page<>(this.pageNo, this.pageSize);

        if (Objects.nonNull(ascs)) {
            ascsStream = ascs.stream()
                    .map(name -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                    .map(OrderItem::asc);
        }
        if (Objects.nonNull(descs)) {
            descsStream = ascs.stream()
                    .map(name -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                    .map(OrderItem::desc);
        }

        List<OrderItem> orderItems = Stream.concat(ascsStream, descsStream).collect(Collectors.toList());
        // 处理默认排序
        if (orderItems.isEmpty()) {
            orderItems = defaultOrder;
        }

        page.setOrders(orderItems);

        return page;
    }

    /**
     * 设置正序排序字段，优先级比传入参数低，用作默认值
     * @param asc
     */
    public PageDTO asc(String... asc) {
        String[] columns = Arrays.asList(asc).stream()
                .map(name -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                .toArray(String[]::new);
        defaultOrder.addAll(OrderItem.ascs(columns));
        return this;
    }

    /**
     * 设置倒序排序字段，优先级比传入参数低，用作默认值
     * @param desc
     */
    public PageDTO desc(String... desc) {
        String[] columns = Arrays.asList(desc).stream()
                .map(name -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                .toArray(String[]::new);
        defaultOrder.addAll(OrderItem.descs(columns));
        return this;
    }

}

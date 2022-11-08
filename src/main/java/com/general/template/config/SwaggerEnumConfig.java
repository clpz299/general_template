package com.general.template.config;

import com.fasterxml.classmate.ResolvedType;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import com.google.common.base.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Annotations;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.schema.ApiModelProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Component
@ConditionalOnClass(EnableSwagger2.class)
public class SwaggerEnumConfig implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        try {
            reFormatting(context);
        } catch (NoSuchMethodException e) {
            log.error("Swagger 枚举缺少desc字段.", e);
        } catch (Exception e) {
            log.error("Swagger 枚举解析错误.", e);
            throw new RuntimeException(e);
        }
    }

    private void reFormatting(ModelPropertyContext context)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // 获取字段的类型
        final Class fieldType = context.getBeanPropertyDefinition().get().getRawPrimaryType();

        if (!Enum.class.isAssignableFrom(fieldType)) {
            return;
        }

        Optional<ApiModelProperty> annotation = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation
                    .or(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = annotation.or(Annotations.findPropertyAnnotation(
                    context.getBeanPropertyDefinition().get(),
                    ApiModelProperty.class));
        }

        StringBuilder text = new StringBuilder();
        Class<Enum> clazz = (Class<Enum>) fieldType;

        // 获取所有枚举实例
        Enum[] enumConstants = clazz.getEnumConstants();
        Method desc = clazz.getMethod("getDesc");

        for (Enum e : enumConstants) {
            text.append(e.name())
                    .append(": ")
                    .append(desc.invoke(e))
                    .append(" ");
        }

        if (annotation.isPresent()) {
            ApiModelProperty apiModelProperty = annotation.get();
            context.getBuilder().example(apiModelProperty.example())
                    .required(apiModelProperty.required());

            text.deleteCharAt(text.length() - 1)
                    .insert(0, "(")
                    .insert(0, apiModelProperty.value())
                    .append(")");
        }

        final ResolvedType resolvedType = context.getResolver().resolve(fieldType);
        context.getBuilder().description(text.toString()).type(resolvedType);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}

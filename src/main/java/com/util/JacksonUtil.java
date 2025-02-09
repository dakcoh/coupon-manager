package com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Jackson을 활용한 JSON 변환 유틸 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT) // Pretty Printing 활성화
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // 알 수 없는 필드 무시
            .registerModule(new JavaTimeModule());

    /**
     * 객체(Object)를 JSON 문자열(String)로 변환
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }

    /**
     * JSON 문자열을 특정 객체(Class)로 변환
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON 역직렬화 실패", e);
        }
    }
}

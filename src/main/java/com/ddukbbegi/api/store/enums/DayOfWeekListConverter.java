package com.ddukbbegi.api.store.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 가게의 정기 휴무일 정보를 List<DayOfWeek> 형태로 관리하기 위해 사용하는 JPA AttributeConverter
 * <p>
 * - 자바의 List<DayOfWeek> <-> DB의 String("SUN,MON") 형태로 변환한다.
 * - 예시) { DayOfWeek.SUN, DayOfWeek.MON } -> "MON,SUN"
 */
@Converter
public class DayOfWeekListConverter implements AttributeConverter<List<DayOfWeek>, String> {

    // Entity -> DB 저장 시 호출
    // List<DayOfWeek> -> "SUN,MON"
    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }

        List<DayOfWeek> sortedAttribute = attribute.stream()
                .distinct()
                .sorted(Comparator.comparingInt(DayOfWeek::ordinal))
                .toList();

        return sortedAttribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }
    
    // DB -> Entity 로딩 시 호출
    // "SUN,MON" -> List<DayOfWeek>
    @Override
    public List<DayOfWeek> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
                        .map(DayOfWeek::valueOf)
                        .toList();
    }
}

package uz.pdp.springsecurity.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableDto {
    private Map<UUID,Integer> integerMap;
}

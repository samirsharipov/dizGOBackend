package uz.dizgo.erp.payload;

import java.util.UUID;

public record CustomerGet(UUID id, String firstName, String lastName) {
}
